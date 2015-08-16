package hr.djajcevic.spc.ioio.looper.process;

import hr.djajcevic.spc.ioio.looper.AxisController;
import hr.djajcevic.spc.ioio.looper.compas.CompassReader;
import hr.djajcevic.spc.ioio.looper.exception.CompassDataNotAvailable;
import hr.djajcevic.spc.ioio.looper.exception.UnknownPanelCurrentStep;
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

        try {
            gpsReader.readData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Configuration.saveGPSData(managerRepository.getGpsData());
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
