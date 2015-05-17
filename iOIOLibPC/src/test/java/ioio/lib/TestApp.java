package ioio.lib;

import ioio.lib.api.DigitalInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.pc.IOIOConsoleApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * @author djajcevic | 03.05.2015.
 */
public class TestApp extends IOIOConsoleApp {

    private DigitalOutput statLed;
    private DigitalOutput pin1;
    private DigitalOutput pin20;
    private DigitalInput pin7;
    private boolean firstLedOn;

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
                statLed = ioio_.openDigitalOutput(IOIO.LED_PIN, true);
                pin1 = ioio_.openDigitalOutput(1);
                pin7 = ioio_.openDigitalInput(7, DigitalInput.Spec.Mode.PULL_DOWN);
            }

            @Override
            public void loop() throws ConnectionLostException, InterruptedException {
                statLed.write(true);

                // /dev/tty.usbmodem1421
                doYourStuff();

                Thread.sleep(1000);
                statLed.write(false);
                Thread.sleep(1000);
            }
        };
    }

    private void doYourStuff() throws ConnectionLostException, InterruptedException {
        boolean pin7 = this.pin7.read();
        System.out.println(new Date() + ": " + pin7);
        pin1.write(pin7);
    }

    public static void main(String[] args) throws Exception {
        new TestApp().go(args);
    }
}
