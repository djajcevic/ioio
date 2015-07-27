package hr.djajcevic.spc;

import ioio.lib.api.IOIO;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.pc.IOIOConsoleApp;

import java.io.*;

/**
 * @author djajcevic | 03.05.2015.
 */
public class TestApp3GPS extends IOIOConsoleApp {


    static {
        System.setProperty("ioio.SerialPorts", "/dev/tty.usbmodem1421");
//        System.setProperty("ioio.SerialPorts", "/dev/tty.usbmodem1411");
    }

    private Uart gps;
    private InputStream inputStream;
    private int bufferSize = 64;
    private OutputStream outputStream;


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

                gps = ioio_.openUart(11, IOIO.INVALID_PIN, 9600, Uart.Parity.NONE, Uart.StopBits.ONE);
                inputStream = gps.getInputStream();
                outputStream = gps.getOutputStream();
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
        int availableBytes = inputStream.available();
        byte[] readBuffer = new byte[64];

        if (availableBytes > 0) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String result = null;

            while ((result = reader.readLine()) != null) {
                System.out.println(result);
                if (!result.startsWith("$GPGGA")) {
                    continue;
                }
                String[] split = result.split(",");
                if (split.length > 4) {
                    String latString = split[2];
                    if (latString.length() > 0) {
                        double lat = Double.parseDouble(latString) / 100;
                        double longitude = Double.parseDouble(split[4]) / 100;
                        System.out.println("Lat:" + lat + "; Long: " + longitude);
                        break;
                    } else {
                        System.out.println("Locating satellites...");
                    }
                }
            }
            reader.close();
        } else {
            readBuffer = new byte[64];
            outputStream.write(readBuffer);
            outputStream.flush();
        }



        Thread.sleep(50);
    }

    public static void main(String[] args) throws Exception {
        new TestApp3GPS().go(args);
    }
}
