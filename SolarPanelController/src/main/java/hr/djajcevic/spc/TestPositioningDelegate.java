package hr.djajcevic.spc;

import hr.djajcevic.spc.ioio.looper.AxisLooper;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * @author djajcevic | 01.07.2015.
 */
public class TestPositioningDelegate extends PositioningDelegate {

    AxisLooper axisXLooper = new AxisLooper(AxisLooper.Axis.X);
    AxisLooper axisYLooper = new AxisLooper(AxisLooper.Axis.Y);
    private IOIO ioio;

    @Override
    public void setup(IOIO ioio) throws ConnectionLostException, InterruptedException {
        axisXLooper.setup(ioio);
        axisYLooper.setup(ioio);
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
        int halfStep = axisYLooper.getMaxStep() / 2;
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
