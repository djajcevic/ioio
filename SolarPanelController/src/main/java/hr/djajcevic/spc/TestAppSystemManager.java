package hr.djajcevic.spc;

import hr.djajcevic.spc.ioio.looper.process.SystemManager;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.pc.IOIOConsoleApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author djajcevic | 03.05.2015.
 */
public class TestAppSystemManager extends IOIOConsoleApp {


    static {
//        System.setProperty("ioio.SerialPorts", "/dev/tty.usbmodem1421");
        System.setProperty("ioio.SerialPorts", "/dev/tty.usbmodem1411");
    }

    SystemManager systemManager;

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
                systemManager = new SystemManager();
                systemManager.setup(ioio_);
            }

            @Override
            public void loop() throws ConnectionLostException, InterruptedException {


                systemManager.loop();

                Thread.sleep(1000);

                System.exit(0);
            }
        };
    }

    public static void main(String[] args) throws Exception {
        new TestAppSystemManager().go(args);
    }
}
