package hr.djajcevic.spc.ioio.looper.process;

import hr.djajcevic.spc.ioio.looper.AxisController;
import hr.djajcevic.spc.ioio.looper.compas.CompassReader;
import hr.djajcevic.spc.ioio.looper.exception.CompassDataNotAvailable;
import hr.djajcevic.spc.ioio.looper.exception.UnknownPanelCurrentStep;
import hr.djajcevic.spc.ioio.looper.gps.GPSData;
import hr.djajcevic.spc.ioio.looper.gps.GPSReader;
import hr.djajcevic.spc.util.Configuration;
import ioio.lib.api.exception.ConnectionLostException;

import java.io.IOException;

/**
 * @author djajcevic | 11.08.2015.
 */
public class CalibrationProcessManager extends AbstractProcessManager {

    private AxisController xAxisController;
    private AxisController yAxisController;

    private GPSReader gpsReader;
    private CompassReader compassReader;

    @Override
    public void initialize() throws ConnectionLostException, InterruptedException {
        xAxisController = managerRepository.getXAxisController();
        yAxisController = managerRepository.getYAxisController();
        gpsReader = managerRepository.getGpsReader();
        compassReader = managerRepository.getCompassReader();
    }

    @Override
    public void performManagementActions() throws ConnectionLostException, InterruptedException {
        GPSData oldGPSData = new GPSData();
        Configuration.loadGPSData(oldGPSData);

        boolean needsCalibration = false;

        try {
            gpsReader.readData();
            Configuration.saveGPSData(managerRepository.getGpsData());

            if (oldGPSData.getLatitude() == null) {
                // initial action
                needsCalibration = true;
            } else {
                double latitudeDiff = oldGPSData.getLatitude() - managerRepository.getGpsData().getLatitude();
                double longitudeDiff = oldGPSData.getLongitude() - managerRepository.getGpsData().getLongitude();
                needsCalibration = Math.abs(latitudeDiff) > 0.5 || Math.abs(longitudeDiff) > 0.5;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (needsCalibration) {
            // 30 degrees left
            xAxisController.move(false, 30);
            // 30 degrees down
            yAxisController.move(false, 30);
            // 30 degrees right
            xAxisController.move(true, 30);
            // 30 degrees up
            yAxisController.move(true, 30);

            Configuration.saveCurrentXStep(xAxisController.getCurrentStep());
            Configuration.saveCurrentYStep(yAxisController.getCurrentStep());
        }

        compassReader.readData();
        Configuration.saveCompassData(managerRepository.getCompassData());

        if (!managerRepository.getCompassData().isDataValid()) {
            throw new CompassDataNotAvailable();
        }

        Double headingDegrees = managerRepository.getCompassData().getHeadingDegrees();
        int heading = headingDegrees.intValue();

        double difference = Math.abs(heading - xAxisController.getCurrentStep());

        if (difference > 10) {
            throw new UnknownPanelCurrentStep();
        }

        xAxisController.update(managerRepository.getCompassData());
    }

}
