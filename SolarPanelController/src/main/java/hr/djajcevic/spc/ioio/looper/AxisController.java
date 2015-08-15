package hr.djajcevic.spc.ioio.looper;

import hr.djajcevic.spc.ioio.looper.compas.CompassData;
import hr.djajcevic.spc.ioio.looper.exception.InvalidPanelStateException;
import hr.djajcevic.spc.ioio.looper.exception.PanelReachedEndPosition;
import hr.djajcevic.spc.ioio.looper.exception.PanelReachedStartPosition;
import hr.djajcevic.spc.ioio.looper.exception.ServoMotorUnavailableException;
import hr.djajcevic.spc.util.Configuration;
import ioio.lib.api.*;
import ioio.lib.api.exception.ConnectionLostException;
import lombok.Getter;

/**
 * @author djajcevic | 09.08.2015.
 */
public class AxisController {

    public static final int PULSE_WIDTH = 1000;
    public static final int MAXIMUM_FAILED_STEPS = 2;
    private Delegate delegate;
    private IOIO ioio;
    private Axis axis;

    private int controlPin;
    private int directionPin;
    private int startPositionIndicatorPin;
    private int endPositionIndicatorPin;
    private int stepIndicatorPin;
    private int servoFreq;

    private PwmOutput controlPinOutput;
    private DigitalOutput directionPinOutput;
    private DigitalInput startPositionIndicatorPinInput;
    private DigitalInput endPositionIndicatorPinInput;
    private DigitalInput stepIndicatorPinInput;

    @Getter
    private int currentStep;
    @Getter
    private int maxSteps;

    @Getter
    private boolean atStart, atEnd;

    private boolean initialized;
    private boolean forceStop;
    private int failedSteps;

    public AxisController(final Delegate delegate, final IOIO ioio, Axis axis) {
        assert delegate != null;
        assert ioio != null;
        assert axis != null;

        this.delegate = delegate;
        this.ioio = ioio;
        this.axis = axis;
    }

    public void initialize() throws ConnectionLostException {
        loadConfiguration();
        initializeIOIOPins();

        initialized = true;
    }

    private void loadConfiguration() {
        currentStep = Configuration.getStatusInteger("servo." + axis + ".currentStep", 0);
        maxSteps = Configuration.getConfigInt("servo." + axis + ".maxSteps");
        controlPin = Configuration.getConfigInt("servo." + axis + ".pin.control");
        directionPin = Configuration.getConfigInt("servo." + axis + ".pin.direction");
        servoFreq = Configuration.getConfigInt("servo." + axis + ".freq");
        startPositionIndicatorPin = Configuration.getConfigInt("servo." + axis + ".pin.startPositionIndicator");
        endPositionIndicatorPin = Configuration.getConfigInt("servo." + axis + ".pin.endPositionIndicator");
        stepIndicatorPin = Configuration.getConfigInt("servo." + axis + ".pin.stepIndicator");
    }

    private void initializeIOIOPins() throws ConnectionLostException {
        checkAndClosePins(controlPinOutput, directionPinOutput, startPositionIndicatorPinInput, endPositionIndicatorPinInput, stepIndicatorPinInput);
        controlPinOutput = ioio.openPwmOutput(controlPin, servoFreq);
        directionPinOutput = ioio.openDigitalOutput(directionPin);
        startPositionIndicatorPinInput = ioio.openDigitalInput(startPositionIndicatorPin);
        endPositionIndicatorPinInput = ioio.openDigitalInput(endPositionIndicatorPin);
        stepIndicatorPinInput = ioio.openDigitalInput(stepIndicatorPin);
    }

    private void checkAndClosePins(Closeable... pins) {
        for (Closeable pin : pins) {
            if (pin != null) {
                pin.close();
            }
        }
    }

    public void move(boolean positiveDirection, int steps) throws ConnectionLostException, InterruptedException {
        System.out.println("Initialized " + axis + " movement in " + (positiveDirection ? "positive" : "negative") + " direction with " + steps + " step target");

        if (!initialized) {
            throw new RuntimeException("Controller not initialized!");
        }

        checkStarEndIndicators();

        failedSteps = 0;

        for (int step = 0; step < Math.abs(steps); step++) {
            try {
                performMovement(positiveDirection);
            } catch (PanelReachedStartPosition e) {
                delegate.reachedStartPosition();
                break;
            } catch (PanelReachedEndPosition e) {
                delegate.reachedStartPosition();
                break;
            }
        }
    }

    public void move(boolean positiveDirection) throws ConnectionLostException, InterruptedException {
        System.out.println("Initialized " + axis + " movement in " + (positiveDirection ? "positive" : "negative") + " direction");

        if (!initialized) {
            throw new RuntimeException("Controller not initialized!");
        }

        checkStarEndIndicators();

        failedSteps = 0;

        while (!delegate.shouldStop(positiveDirection, currentStep)) {
            try {
                if (!performMovement(positiveDirection)) {
                    break;
                }
            } catch (PanelReachedStartPosition e) {
                delegate.reachedStartPosition();
                break;
            } catch (PanelReachedEndPosition e) {
                delegate.reachedStartPosition();
                break;
            }
        }

        controlPinOutput.setPulseWidth(0);
    }

    /**
     * @param positiveDirection
     * @return if movement can continue
     * @throws ConnectionLostException
     * @throws InterruptedException
     */
    private boolean performMovement(boolean positiveDirection) throws ConnectionLostException, InterruptedException, PanelReachedStartPosition, PanelReachedEndPosition {
        if (failedSteps == MAXIMUM_FAILED_STEPS) {
            throw new ServoMotorUnavailableException("Too many failed steps on " + this + " controller!");
        }

        if (!movementValid(positiveDirection)) {
            return false;
        }

        directionPinOutput.write(positiveDirection);
        controlPinOutput.setPulseWidth(PULSE_WIDTH);
        checkStarEndIndicators();
        if (stepIndicatorPinInput.read()) {
            if (movementValid(positiveDirection)) {
                currentStep = positiveDirection ? currentStep + 1 : currentStep - 1;
                delegate.stepCompleted(currentStep);
            }
        } else {
            failedSteps++;
        }
        return true;
    }

    private boolean movementValid(final boolean positiveDirection) {
        if (atStart && !positiveDirection) {
            throw new PanelReachedStartPosition(axis);
        }

        if (atEnd && positiveDirection) {
            throw new PanelReachedEndPosition(axis);
        }

        if (atStart && positiveDirection) {
            return true;
        } else if (atEnd && !positiveDirection) {
            return true;
        } else if (!atStart && !atEnd) {
            return true;
        } else {
            return false;
        }
    }

    private void checkStarEndIndicators() throws ConnectionLostException, InterruptedException {
        atStart = startPositionIndicatorPinInput.read();
        atEnd = endPositionIndicatorPinInput.read();

        if (atStart && atEnd) {
            throw new InvalidPanelStateException("Invalid start/end position!");
        }

        if (atStart) {
            currentStep = 0;
            delegate.reachedStartPosition();
            atEnd = false;
            throw new PanelReachedStartPosition(axis);
        } else if (atEnd) {
            currentStep = maxSteps;
            delegate.reachedEndPosition();
            atStart = false;
            throw new PanelReachedEndPosition(axis);
        } else {
            if (currentStep == maxSteps) {
                atEnd = true;
            } else if (currentStep == 0) {
                atStart = true;
            }
        }
    }

    @Override
    public String toString() {
        return "AxisController{" +
                "axis=" + axis +
                ", controlPin=" + controlPin +
                ", directionPin=" + directionPin +
                ", startPositionIndicatorPin=" + startPositionIndicatorPin +
                ", endPositionIndicatorPin=" + endPositionIndicatorPin +
                ", stepIndicatorPin=" + stepIndicatorPin +
                ", servoFreq=" + servoFreq +
                ", currentStep=" + currentStep +
                ", maxSteps=" + maxSteps +
                ", atStart=" + atStart +
                ", atEnd=" + atEnd +
                ", initialized=" + initialized +
                '}';
    }

    public void update(final CompassData data) {
        switch (axis) {
            case X:
                currentStep = data.getHeadingDegrees() != null ? data.getHeadingDegrees().intValue() : 0;
                Configuration.saveCurrentXStep(currentStep);
                break;
            case Y:
                break;
        }
    }

    public enum Axis {
        X, Y
    }

    public interface Delegate {

        void stepCompleted(int currentStep);

        void reachedStartPosition();

        void reachedEndPosition();

        boolean shouldStop(final boolean positiveDirection, int currentStep);

    }
}
