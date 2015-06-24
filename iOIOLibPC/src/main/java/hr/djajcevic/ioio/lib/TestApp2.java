package hr.djajcevic.ioio.lib;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
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
public class TestApp2 extends IOIOConsoleApp {

    private DigitalOutput statLed;
    private DigitalOutput pin1;
    private DigitalOutput pin3;
    private boolean firstLedOn;
    private PwmOutput pwmOutput;

    static {
        System.setProperty("ioio.SerialPorts", "/dev/tty.usbmodem1421");
    }

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
                statLed = ioio_.openDigitalOutput(IOIO.LED_PIN);
                pin1 = ioio_.openDigitalOutput(1);
//                pin3 = ioio_.openDigitalOutput(3);
                pwmOutput = ioio_.openPwmOutput(3, 1024);
            }

            @Override
            public void loop() throws ConnectionLostException, InterruptedException {


                // -Dioio.SerialPorts=/dev/tty.usbmodem1421
                doYourStuff();


            }
        };
    }

    private void doYourStuff() throws ConnectionLostException, InterruptedException {
        System.out.println(new Date() + ": " + firstLedOn);
//        pin1.write(firstLedOn);
        firstLedOn = !firstLedOn;
        statLed.write(false);
        pwmOutput.setPulseWidth(10);
        Thread.sleep(1000);
        pwmOutput.setPulseWidth(0);
        statLed.write(true);
        Thread.sleep(1000);
    }

    public static void main(String[] args) throws Exception {
        new TestApp2().go(args);
    }
}
