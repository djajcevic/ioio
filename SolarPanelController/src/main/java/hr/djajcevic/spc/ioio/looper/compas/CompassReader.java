package hr.djajcevic.spc.ioio.looper.compas;

import hr.djajcevic.spc.ioio.looper.IOIOReader;
import hr.djajcevic.spc.ioio.looper.exception.UnInitializedException;
import hr.djajcevic.spc.util.Configuration;
import ioio.lib.api.IOIO;
import ioio.lib.api.TwiMaster;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * @author djajcevic | 11.08.2015.
 */
public class CompassReader implements IOIOReader {

    public static final int SENSOR_ADDRESS = 0x1E;
    public static final byte[] SCALE_CONFIGURATION_DATA = new byte[]{0x01, 0x01};
    public static final byte[] CONTINUOUS_MODE_DATA = new byte[]{0x02, 0x00};
    public static final byte[] READ_SENSOR_DATA = new byte[]{0x03};
    public static final double COMPASS_SCALE = 0.92;

    private Delegate delegate;
    private IOIO ioio;

    private TwiMaster twiMaster;
    private boolean initialized;

    public CompassReader(final IOIO ioio, final Delegate delegate) {
        this.ioio = ioio;
        this.delegate = delegate;
    }

    @Override
    public void initialize() throws ConnectionLostException, InterruptedException {
        final int compassPin = Configuration.getConfigInt("compass.pin");

        twiMaster = ioio.openTwiMaster(compassPin, TwiMaster.Rate.RATE_100KHz, false);
        byte[] readData = new byte[6];

        // set scale to gauss = 1.3 (register value = 1, scale = 0.92)
        twiMaster.writeRead(SENSOR_ADDRESS, false, SCALE_CONFIGURATION_DATA, SCALE_CONFIGURATION_DATA.length, readData, readData.length);
        readData = new byte[6];

        // set CONTINIOUS mode
        twiMaster.writeRead(SENSOR_ADDRESS, false, CONTINUOUS_MODE_DATA, CONTINUOUS_MODE_DATA.length, readData, readData.length);

        initialized = true;
    }

    @Override
    public void readData() throws ConnectionLostException, InterruptedException {
        if (!initialized) {
            throw new UnInitializedException("CompassReader not initialized!");
        }

        byte[] readData = new byte[6];

        twiMaster.writeRead(SENSOR_ADDRESS, false, READ_SENSOR_DATA, READ_SENSOR_DATA.length, readData, readData.length);

        int[] rawData = new int[3];
        rawData[0] = (readData[0] << 8) | readData[1];
        rawData[1] = (readData[2] << 8) | readData[3];
        rawData[2] = (readData[4] << 8) | readData[5];

        double[] scaledData = new double[3];
        scaledData[0] = rawData[0] * COMPASS_SCALE;
        scaledData[1] = rawData[1] * COMPASS_SCALE;
        scaledData[2] = rawData[2] * COMPASS_SCALE;

        double heading = Math.atan2(scaledData[2], scaledData[0]);
        if (heading < 0)
            heading += 2 * Math.PI;
        if (heading > 2 * Math.PI)
            heading -= 2 * Math.PI;
        double headingDegrees = heading * 180 / Math.PI;

        CompassData data = new CompassData(scaledData[0], scaledData[2], scaledData[1], heading, headingDegrees);
        delegate.dataReady(data);
    }

    public interface Delegate {
        void dataReady(CompassData data);
    }


}
