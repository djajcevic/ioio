package hr.djajcevic.spc.ioio.looper.process;

import hr.djajcevic.spc.ioio.looper.AxisController;
import hr.djajcevic.spc.ioio.looper.compas.CompassData;
import hr.djajcevic.spc.ioio.looper.compas.CompassReader;
import hr.djajcevic.spc.ioio.looper.gps.GPSData;
import hr.djajcevic.spc.ioio.looper.gps.GPSReader;
import ioio.lib.api.exception.ConnectionLostException;
import lombok.Data;

import java.io.IOException;

/**
 * @author djajcevic | 11.08.2015.
 */
@Data
public class CalibrationManager extends AbstractManager implements GPSReader.Delegate, CompassReader.Delegate {

    private AxisController xAxisController;
    private AxisController yAxisController;

    private GPSReader gpsReader;
    private CompassReader compassReader;

    @Override
    public void initialize() throws ConnectionLostException, InterruptedException {
        xAxisController = managerRepository.getXAxisController();
        yAxisController = managerRepository.getYAxisController();
        gpsReader = new GPSReader(managerRepository.getIoio(), this);
        compassReader = new CompassReader(managerRepository.getIoio(), this);

        gpsReader.initialize();
        compassReader.initialize();
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

        try {
            gpsReader.readData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        compassReader.readData();
    }

    @Override
    public void positionLocked(final GPSData data) {
        managerRepository.setGpsData(data);
    }

    @Override
    public void dataReady(final CompassData data) {
        managerRepository.setCompassData(data);
    }
}
