package hr.djajcevic.spc;

import hr.djajcevic.spc.ioio.looper.AxisLooper;
import ioio.lib.api.DigitalInput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * @author djajcevic | 01.07.2015.
 */
public class TestPositioningDelegate extends PositioningDelegate {

    AxisLooper axisXLooper = new AxisLooper(AxisLooper.Axis.X, 1, 2, 10, 12, 8);
    AxisLooper axisYLooper = new AxisLooper(AxisLooper.Axis.Y, 3, 4, 11, 13, 6);
    PwmOutput signalTone;
    private IOIO ioio;
    private DigitalInput systemSleepPin;

    @Override
    public void setup(IOIO ioio) throws ConnectionLostException, InterruptedException {
        this.ioio = ioio;
        axisXLooper.setup(ioio);
        axisYLooper.setup(ioio);
        signalTone = ioio.openPwmOutput(7, 500);
        systemSleepPin = ioio.openDigitalInput(18);
    }

    @Override
    boolean systemSleeping() {
        try {
            return systemSleepPin.read();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ConnectionLostException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    boolean moveXPanelLeft(final SystemInformation systemInformation) {
        axisXLooper.moveLeft();
        systemInformation.getServoInformation().setXServoPosition(axisXLooper.getStep());
        return false;
    }

    @Override
    boolean moveXPanelRight(final SystemInformation systemInformation) {
        axisXLooper.moveRight();
        systemInformation.getServoInformation().setXServoPosition(axisXLooper.getStep());
        return false;
    }

    @Override
    boolean moveXPanelFarRight(final SystemInformation systemInformation) {
        while (!axisXLooper.isAtEnd()) {
            axisXLooper.moveRight();
        }
        systemInformation.getServoInformation().setXServoPosition(axisXLooper.getStep());
        return false;
    }

    @Override
    boolean moveXPanelFarLeft(final SystemInformation systemInformation) {
        while (!axisXLooper.isAtStart()) {
            axisXLooper.moveLeft();
        }
        systemInformation.getServoInformation().setXServoPosition(axisXLooper.getStep());
        return false;
    }

    @Override
    boolean moveYPanelUp(final SystemInformation systemInformation) {
        axisYLooper.moveLeft();
        systemInformation.getServoInformation().setYServoPosition(axisYLooper.getStep());
        return false;
    }

    @Override
    boolean moveYPanelDown(final SystemInformation systemInformation) {
        return false;
    }

    @Override
    boolean centerYPanel(final SystemInformation systemInformation) {
        int halfStep = axisYLooper.getServoMaxSteps() / 2;
        int step = axisYLooper.getStep();

        if (axisYLooper.isAtEnd() || step > halfStep) {
            while (step > halfStep && !axisXLooper.isAtEnd()) {
                step = axisYLooper.getStep();
                axisYLooper.moveLeft();
            }
        }
        if (axisYLooper.isAtStart() || step < halfStep) {
            while (step < halfStep) {
                step = axisYLooper.getStep();
                axisYLooper.moveRight();
            }
        }
        systemInformation.getServoInformation().setYServoPosition(step);
        return false;
    }

    @Override
    int calibrationStepRange() {
        return 3;
    }
}
