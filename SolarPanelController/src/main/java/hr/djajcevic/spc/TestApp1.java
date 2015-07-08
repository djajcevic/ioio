package hr.djajcevic.spc;

import ioio.lib.api.DigitalInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.pc.IOIOConsoleApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author djajcevic | 03.05.2015.
 */
public class TestApp1 extends IOIOConsoleApp {

    private DigitalOutput statLed;
    private DigitalOutput pin1;
    private DigitalOutput pin20;
    private DigitalInput pin7;
    private boolean firstLedOn;

    static {
        System.setProperty("ioio.SerialPorts", "/dev/tty.usbmodem1421");
//        System.setProperty("ioio.SerialPorts", "/dev/tty.usbmodem1411");
    }

    private DigitalOutput systemSleepPin;
    private DigitalOutput xServoStepPin;
    private DigitalOutput yServoStepPin;
    private DigitalInput xServoSignalPin;
    private DigitalInput yServoSignalPin;


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
                systemSleepPin = ioio_.openDigitalOutput(21, DigitalOutput.Spec.Mode.OPEN_DRAIN, true);

                xServoStepPin = ioio_.openDigitalOutput(23, DigitalOutput.Spec.Mode.OPEN_DRAIN, false);
                yServoStepPin = ioio_.openDigitalOutput(24, DigitalOutput.Spec.Mode.OPEN_DRAIN, false);

                xServoSignalPin = ioio_.openDigitalInput(1, DigitalInput.Spec.Mode.PULL_DOWN);
                yServoSignalPin = ioio_.openDigitalInput(3, DigitalInput.Spec.Mode.PULL_DOWN);
            }

            @Override
            public void loop() throws ConnectionLostException, InterruptedException {


                // /dev/tty.usbmodem1421
                doYourStuff();

                Thread.sleep(1000);
            }
        };
    }

    private void doYourStuff() throws ConnectionLostException, InterruptedException {
        systemSleepPin.write(false);
        System.out.println("Started");

        if (xServoSignalPin.read()) {
            statLed.write(false);
            System.out.println("Received X move signal");
            Thread.sleep(1000);
            System.out.println("Sending X step feedback");
            xServoStepPin.write(true);
            Thread.sleep(200);
            xServoStepPin.write(false);
            statLed.write(true);
        }

        if (yServoSignalPin.read()) {
            statLed.write(false);
            System.out.println("Received Y move signal");
            Thread.sleep(1000);
            System.out.println("Sending Y step feedback");
            yServoStepPin.write(true);
            Thread.sleep(200);
            yServoStepPin.write(false);
            statLed.write(true);
        }
    }

    public static void main(String[] args) throws Exception {
        new TestApp1().go(args);
    }
}
