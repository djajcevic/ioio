package hr.djajcevic.spc.ioio.looper.process;

import hr.djajcevic.spc.ioio.looper.compas.CompassData;
import hr.djajcevic.spc.ioio.looper.exception.SystemException;
import hr.djajcevic.spc.ioio.looper.gps.GPSData;
import ioio.lib.api.IOIO;

/**
 * @author djajcevic | 12.08.2015.
 */
public interface SystemManagerListener {

    void boardConnected(IOIO ioio);

    void boardDisconnected();

    void incompatibleBoard(IOIO ioio);

    void xAxisStepCompleted(int currentStep);
    void yAxisStepCompleted(int currentStep);

    void xAxisReachedStartPosition();
    void yAxisReachedStartPosition();

    void xAxisReachedEndPosition();
    void yAxisReachedEndPosition();


    void gpsPositionLocked(GPSData data);

    void compassDataReady(CompassData data);

    void performingParkDueTo(SystemException error);
}
