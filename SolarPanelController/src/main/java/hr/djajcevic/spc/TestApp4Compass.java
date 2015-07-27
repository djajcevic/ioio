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
                twiMaster.writeRead(1, false, new byte[]{0x60}, 1, readData, 6);
                System.out.println(Arrays.toString(readData));
                readData = new byte[6];
                twiMaster.writeRead(2, false, new byte[]{0x02}, 1, readData, 6);
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

        byte[] readData = new byte[6];
//        twiMaster.writeRead(30, false, new byte[]{0x03}, 1, readData, readData.length);
        twiMaster.writeRead(30, false, null, 0, readData, readData.length);
        int[] rawData = new int[3];
        rawData[0] = (readData[0] << 8) | readData[1];
        rawData[1] = (readData[2] << 8) | readData[3];
        rawData[2] = (readData[4] << 8) | readData[5];
        System.out.println(Arrays.toString(rawData));


        Thread.sleep(50);
    }

    public static void main(String[] args) throws Exception {
        new TestApp4Compass().go(args);
    }
}
