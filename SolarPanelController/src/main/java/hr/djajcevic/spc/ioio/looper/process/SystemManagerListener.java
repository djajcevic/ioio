package hr.djajcevic.spc.ioio.looper.process;

import hr.djajcevic.spc.ioio.looper.compas.CompassData;
import hr.djajcevic.spc.ioio.looper.exception.SystemException;
import hr.djajcevic.spc.ioio.looper.gps.GPSData;
import ioio.lib.api.IOIO;

/**
 * @author djajcevic | 12.08.2015.
 */
public interface SystemManagerListener {

    // ioio board
    void boardConnected(IOIO ioio);

    void boardDisconnected();

    void incompatibleBoard(IOIO ioio);

    // positioning

    void message(final String message);

    void xAxisStepCompleted(int currentStep);
    void yAxisStepCompleted(int currentStep);

    void xAxisReachedStartPosition();
    void yAxisReachedStartPosition();

    void xAxisReachedEndPosition();
    void yAxisReachedEndPosition();

    // data
    void gpsPositionLocked(GPSData data);
    void compassDataReady(CompassData data);

    // errors
    void systemError(Exception e);
    void performingParkDueTo(SystemException error);
}
