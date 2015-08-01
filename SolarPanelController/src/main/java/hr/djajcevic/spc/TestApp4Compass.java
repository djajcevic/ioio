package hr.djajcevic.spc;

import ioio.lib.api.TwiMaster;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.pc.IOIOConsoleApp;

import java.io.*;
import java.util.Arrays;

/**
 * @author djajcevic | 03.05.2015.
 */
public class TestApp4Compass extends IOIOConsoleApp {


    static {
        System.setProperty("ioio.SerialPorts", "/dev/tty.usbmodem1421");
//        System.setProperty("ioio.SerialPorts", "/dev/tty.usbmodem1411");
    }

    private InputStream inputStream;
    private int bufferSize = 64;
    private OutputStream outputStream;
    private TwiMaster twiMaster;


    @Override
    protected void run(final String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));
        boolean abort = false;
        String line;
        while (!abort && (line = reader.readLine()) != null) {
            System.out.println("Waiting input");
            abort = line.equals("q!");
        }
    }

    @Override
    public IOIOLooper createIOIOLooper(final String connectionType, final Object extra) {
        return new BaseIOIOLooper() {
            @Override
            protected void setup() throws ConnectionLostException, InterruptedException {
                super.setup();

                twiMaster = ioio_.openTwiMaster(2, TwiMaster.Rate.RATE_100KHz, false);
                byte[] readData = new byte[6];

                // set scale to gauss = 1.3 (register value = 1, scale = 0.92)
                twiMaster.writeRead(0x1E, false, new byte[]{0x01, 0x01}, 2, readData, 6);
                System.out.println(Arrays.toString(readData));
                readData = new byte[6];

                // set CONTINIOUS mode
                twiMaster.writeRead(0x1E, false, new byte[]{0x02, 0x00}, 2, readData, 6);
                System.out.println(Arrays.toString(readData));
            }

            @Override
            public void loop() throws ConnectionLostException, InterruptedException {


                // /dev/tty.usbmodem1421
                try {
                    doYourStuff();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Thread.sleep(1000);
            }
        };
    }

    private void doYourStuff() throws ConnectionLostException, InterruptedException, IOException {

        System.out.println("---------------------------------------------");
        byte[] readData = new byte[6];
        System.out.println("Reading data...");
        twiMaster.writeRead(0x1E, false, new byte[]{0x03}, 1, readData, readData.length);
        System.out.println("Read data: " + Arrays.toString(readData));
        int[] rawData = new int[3];
        rawData[0] = (readData[0] << 8) | readData[1];
        rawData[1] = (readData[2] << 8) | readData[3];
        rawData[2] = (readData[4] << 8) | readData[5];

        double[] scaledData = new double[3];
        scaledData[0] = rawData[0] * 0.92;
        scaledData[1] = rawData[1] * 0.92;
        scaledData[2] = rawData[2] * 0.92;

        System.out.println("Raw data: " + Arrays.toString(rawData));
        System.out.println("Scaled data: " + Arrays.toString(scaledData));

        double heading = Math.atan2(scaledData[2], scaledData[0]);
        if (heading < 0)
            heading += 2 * Math.PI;
        if (heading > 2 * Math.PI)
            heading -= 2 * Math.PI;
        double headingDegrees = heading * 180 / Math.PI;
        System.out.println("Heading: " + heading + "rad, " + headingDegrees + "deg");


        Thread.sleep(50);
    }

    public static void main(String[] args) throws Exception {
        new TestApp4Compass().go(args);
    }
}
