package hr.djajcevic.spc;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.pc.IOIOConsoleApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author djajcevic | 03.05.2015.
 */
public class TestApp2 extends IOIOConsoleApp {

    static {
//        System.setProperty("ioio.SerialPorts", "/dev/tty.usbmodem1421");
        System.setProperty("ioio.SerialPorts", "/dev/tty.usbmodem1411");
    }

    private DigitalOutput statLed;
    private DigitalOutput pin1;
    private DigitalOutput pin3;
    private boolean firstLedOn;
    private PwmOutput pwmOutput;

    public static void main(String[] args) throws Exception {
        new TestApp2().go(args);
    }

    @Override
    protected void run(final String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));
        boolean abort = false;
        String line;
        while (true) {
//            System.out.println("Waiting input");
//            abort = line.equals("q!");
        }
    }

    @Override
    public IOIOLooper createIOIOLooper(final String connectionType, final Object extra) {
        return new SolarPanelControllerImpl(null);
    }

    private void doYourStuff() throws ConnectionLostException, InterruptedException {
        statLed.write(false);
        Thread.sleep(1000);
        statLed.write(true);
    }
}
