package hr.djajcevic.spc.ioio.looper;

import ioio.lib.api.DigitalInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author djajcevic | 01.07.2015.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxisLooper extends BaseIOIOLooper {

    private PwmOutput signalPinOutput;
    private DigitalOutput directionPinOutput;
    private DigitalInput servoStepFeedbackInput;
    private DigitalInput startReachedInput;

    public enum Axis {
        X, Y
    }

    private Axis axis;

    private int signalPin = -1;

    private int directionPin = -1;

    private int servoStepFeedbackPin = -1;

    private int startReachedPin = -1;

    private int servoMaxSteps;

    public AxisLooper(Axis axis, int signalPin, int directionPin, int servoStepFeedbackPin, int startReachedPin, int servoMaxSteps) {
        this.axis = axis;
        this.signalPin = signalPin;
        this.directionPin = directionPin;
        this.servoStepFeedbackPin = servoStepFeedbackPin;
        this.startReachedPin = startReachedPin;
        this.servoMaxSteps = servoMaxSteps;
    }


    private int step = 0;

    private boolean atSomewhere = true, atEnd = false, atStart = false;

    @Override
    protected void setup() throws ConnectionLostException, InterruptedException {
        assert signalPin > 0;
        assert directionPin > 0;
        assert servoStepFeedbackPin > 0;
        assert startReachedPin > 0;

        signalPinOutput = ioio_.openPwmOutput(signalPin, 1);
        directionPinOutput = ioio_.openDigitalOutput(this.directionPin);
        servoStepFeedbackInput = ioio_.openDigitalInput(servoStepFeedbackPin);
        startReachedInput = ioio_.openDigitalInput(startReachedPin);

        switch (axis) {
            case X:
                // create x digital outputs
                // check if at start or end
                break;
            case Y:
                // create y digital outputs
                // check if at start or end
                break;
        }
    }

    @Override
    public void loop() throws ConnectionLostException, InterruptedException {
//        while (step == 0) {
//            Thread.sleep(20);
//        }
    }

    public void moveLeft() {

        try {
            directionPinOutput.write(true);
            while (servoStepFeedbackInput.read() == false) {
                signalPinOutput.setDutyCycle(1.0f);
                atStart = startReachedInput.read();
            }
            signalPinOutput.setDutyCycle(0.0f);
            step--;
        } catch (ConnectionLostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(axis + ":" + step);
    }

    public void moveRight() {
        if (step == servoMaxSteps - 10) {
            atEnd = true;
            atStart = false;
            return;
        }
        try {
            directionPinOutput.write(false);
            while (servoStepFeedbackInput.read() == false) {
                signalPinOutput.setDutyCycle(1.0f);
            }
            signalPinOutput.setDutyCycle(0.0f);
            step++;
        } catch (ConnectionLostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(axis + ":" + step);
    }
}
