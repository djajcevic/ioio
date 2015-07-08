package hr.djajcevic.spc;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * @author djajcevic | 25.06.2015.
 */
public abstract class PositioningDelegate {

    abstract boolean moveXPanelLeft(SystemInformation systemInformation);

    abstract boolean moveXPanelRight(SystemInformation systemInformation);

    abstract boolean moveXPanelFarRight(SystemInformation systemInformation);

    abstract boolean moveXPanelFarLeft(SystemInformation systemInformation);

    abstract boolean moveYPanelUp(SystemInformation systemInformation);

    abstract boolean moveYPanelDown(SystemInformation systemInformation);

    abstract boolean centerYPanel(SystemInformation systemInformation);

    /**
     * @return the max amount of steps to use with calibration
     */
    abstract int calibrationStepRange();

    /**
     * @return the amount of calibration takes to make
     */
    int calibrationTakes() {
        return 3;
    }

    abstract void setup(final IOIO ioio) throws ConnectionLostException, InterruptedException;

    abstract boolean systemSleeping();
}
