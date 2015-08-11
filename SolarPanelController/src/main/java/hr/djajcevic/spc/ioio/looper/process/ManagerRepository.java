package hr.djajcevic.spc.ioio.looper.process;

import hr.djajcevic.spc.ioio.looper.AxisController;
import hr.djajcevic.spc.ioio.looper.compas.CompassData;
import hr.djajcevic.spc.ioio.looper.gps.GPSData;
import ioio.lib.api.IOIO;
import lombok.Data;

/**
 * @author djajcevic | 11.08.2015.
 */
@Data
public class ManagerRepository {

    private IOIO ioio;
    private AxisController xAxisController;
    private AxisController yAxisController;
    private CompassData compassData;
    private GPSData gpsData;

}
