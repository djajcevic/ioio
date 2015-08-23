package hr.djajcevic.spc.ioio.looper.gps;

import hr.djajcevic.spc.ioio.looper.IOIOReader;
import hr.djajcevic.spc.ioio.looper.exception.UnInitializedException;
import hr.djajcevic.spc.util.Configuration;
import ioio.lib.api.IOIO;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;

import java.io.*;
import java.util.Calendar;

import static hr.djajcevic.spc.ioio.looper.Util.extractCurrentCalendar;
import static hr.djajcevic.spc.ioio.looper.Util.safeNumberValue;

/**
 * @author djajcevic | 11.08.2015.
 */
public class GPSReader implements IOIOReader {

    private Delegate delegate;
    private IOIO ioio;

    private boolean initialized;
    private Uart uartInput;

    private InputStream gpsDataInputStream;
    private OutputStream gpsDataOutputStream;

    public GPSReader(final IOIO ioio, final Delegate delegate) {
        this.ioio = ioio;
        this.delegate = delegate;
    }

    @Override
    public void readData() throws ConnectionLostException, IOException {
        if (!initialized) {
            throw new UnInitializedException("GPSReader not initialized!");
        }

        int availableBytes = gpsDataInputStream.available();
        byte[] readBuffer = new byte[64];

        if (availableBytes > 0) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(gpsDataInputStream));
            String result = null;

            while ((result = reader.readLine()) != null) {
                if (!result.startsWith("$GPGGA")) {
                    continue;
                }
                String[] split = result.split(",");
                if (split.length > 4) {
                    GPSData data = extractGpsData(split);

                    if (data == null) continue;

                    delegate.positionLocked(data);
                }
            }
            reader.close();
        }
    }

    public static GPSData extractGpsData(final String[] split) {
        GPSData data = new GPSData();
        String time = split[1];
        if (time.length() > 0) {
            try {
                Calendar calendar = extractCurrentCalendar(time);
                data.setTime(calendar);
                data.setLatitude(safeNumberValue(split[2], Double.class) / 100);
                data.setLatitudeDirection(split[3]);
                data.setLongitude(safeNumberValue(split[4], Double.class) / 100);
                data.setLongitudeDirection(split[5]);
                data.setNumberOfSatellites(safeNumberValue(split[7], Long.class));
                data.setAltitude(safeNumberValue(split[9], Double.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        } else {
            System.out.println("Locating satellites...");
        }
        return data;
    }

    @Override
    public void initialize() throws ConnectionLostException {
        if (initialized) {
            uartInput.close();
            initialized = false;
        }
        final int gpsPin = Configuration.getConfigInt("gps.pin");
        final int gpsFreq = Configuration.getConfigInt("gps.freq");

        uartInput = ioio.openUart(gpsPin, IOIO.INVALID_PIN, gpsFreq, Uart.Parity.NONE, Uart.StopBits.ONE);
        gpsDataInputStream = uartInput.getInputStream();

        initialized = true;
    }

    public interface Delegate {

        void positionLocked(GPSData data);

    }

}
