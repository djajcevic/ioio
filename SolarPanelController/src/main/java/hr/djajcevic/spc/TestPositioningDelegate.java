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
        return false;
    }

    @Override
    boolean moveXPanelRight(final SystemInformation systemInformation) {
        axisXLooper.moveRight();
        return false;
    }

    @Override
    boolean moveXPanelFarRight(final SystemInformation systemInformation) {
        return false;
    }

    @Override
    boolean moveXPanelFarLeft(final SystemInformation systemInformation) {
        return false;
    }

    @Override
    boolean moveYPanelUp(final SystemInformation systemInformation) {
        axisYLooper.moveLeft();
        return false;
    }

    @Override
    boolean moveYPanelDown(final SystemInformation systemInformation) {
        return false;
    }

    @Override
    boolean centerYPanel(final SystemInformation systemInformation) {
        return false;
    }

    @Override
    int calibrationStepRange() {
        return 0;
    }
}
