package hr.djajcevic.spc.ioio.looper.process;

import hr.djajcevic.spc.calculator.SunPositionCalculator;
import hr.djajcevic.spc.calculator.SunPositionData;
import hr.djajcevic.spc.ioio.looper.AxisController;
import hr.djajcevic.spc.ioio.looper.compas.CompassData;
import hr.djajcevic.spc.ioio.looper.compas.CompassReader;
import hr.djajcevic.spc.ioio.looper.exception.*;
import hr.djajcevic.spc.ioio.looper.gps.GPSData;
import hr.djajcevic.spc.ioio.looper.gps.GPSReader;
import hr.djajcevic.spc.util.Configuration;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.IOIOLooper;
import lombok.Getter;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author djajcevic | 11.08.2015.
 */
public class SystemManager implements IOIOLooper, GPSReader.Delegate, CompassReader.Delegate {

    @Getter
    Set<SystemManagerListener> listeners = new LinkedHashSet<SystemManagerListener>();
    private IOIO ioio;
    @Getter
    private AxisController xAxisController;
    @Getter
    private AxisController yAxisController;
    @Getter
    private CompassReader compassReader;
    @Getter
    private CompassData compassData;
    @Getter
    private GPSReader gpsReader;
    @Getter
    private GPSData gpsData;

    private CalibrationProcessManager calibrationManager;
    private ParkingProcessManager parkingManager;
    private PositioningProcessManager positioningManager;

    private boolean parked;
    private boolean parkedX;
    private boolean parkedY;

    @Getter
    private SunPositionData sunPositionData;

    private void initialize() throws ConnectionLostException, InterruptedException {

        xAxisController = new AxisController(new AxisController.Delegate() {
            @Override
            public void stepCompleted(final int currentStep) {
                for (SystemManagerListener listener : listeners) {
                    listener.xAxisStepCompleted(currentStep);
                }
            }

            @Override
            public void reachedStartPosition() {
                for (SystemManagerListener listener : listeners) {
                    listener.xAxisReachedStartPosition();
                }
                parkedX = true;
            }

            @Override
            public void reachedEndPosition() {
                for (SystemManagerListener listener : listeners) {
                    listener.xAxisReachedEndPosition();
                }
            }

            @Override
            public boolean shouldStop(final boolean positiveDirection, final int currentStep) {
                return false;
            }
        }, ioio, AxisController.Axis.X);

        yAxisController = new AxisController(new AxisController.Delegate() {
            @Override
            public void stepCompleted(final int currentStep) {
                for (SystemManagerListener listener : listeners) {
                    listener.yAxisStepCompleted(currentStep);
                }
            }

            @Override
            public void reachedStartPosition() {
                for (SystemManagerListener listener : listeners) {
                    listener.yAxisReachedStartPosition();
                }
                parkedY = true;
            }

            @Override
            public void reachedEndPosition() {
                for (SystemManagerListener listener : listeners) {
                    listener.yAxisReachedEndPosition();
                }
            }

            @Override
            public boolean shouldStop(final boolean positiveDirection, final int currentStep) {
                return false;
            }
        }, ioio, AxisController.Axis.Y);

        xAxisController.initialize();
        yAxisController.initialize();

        gpsReader = new GPSReader(ioio, this);
        compassReader = new CompassReader(ioio, this);

        gpsReader.initialize();
        compassReader.initialize();

        calibrationManager = new CalibrationProcessManager();
        calibrationManager.setManagerRepository(this);
        calibrationManager.initialize();

        parkingManager = new ParkingProcessManager();
        parkingManager.setManagerRepository(this);
        parkingManager.initialize();

        sunPositionData = new SunPositionData();

        positioningManager = new PositioningProcessManager();
        positioningManager.setManagerRepository(this);
        positioningManager.initialize();

        parked = Configuration.getStatusBoolean("system.parked", false);
    }


    @Override
    public void positionLocked(final GPSData data) {
        gpsData = data;
        for (SystemManagerListener listener : listeners) {
            listener.gpsPositionLocked(data);
        }
    }

    @Override
    public void dataReady(final CompassData data) {
        compassData = data;
        for (SystemManagerListener listener : listeners) {
            listener.compassDataReady(data);
        }
    }

    @Override
    public void setup(final IOIO ioio) throws ConnectionLostException, InterruptedException {
        this.ioio = ioio;
        initialize();
        for (SystemManagerListener listener : listeners) {
            listener.boardConnected(ioio);
        }
    }

    @Override
    public void loop() throws ConnectionLostException, InterruptedException {
        safePark();
        calibrate();
        calculateNextPosition();
        doPosition();
    }

    private boolean checkPosition() {
        try {
            GPSData oldGPSData = new GPSData();
            Configuration.loadGPSData(oldGPSData);

            gpsReader.readData();
            Configuration.saveGPSData(getGpsData());

            double latitudeDiff = oldGPSData.getLatitude() - getGpsData().getLatitude();
            double longitudeDiff = oldGPSData.getLongitude() - getGpsData().getLongitude();
            if (Math.abs(latitudeDiff) > 0.5) {
                return false;
            } else if (Math.abs(longitudeDiff) > 0.5) {
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            notifyDelegatesForSystemError(new SystemException(e));
            return false;
        } catch (ConnectionLostException e) {
            notifyDelegatesForSystemError(new SystemException(e));
            return false;
        }
    }

    private void safePark() {
        if (!parked) {
            park();
            Configuration.setStatus("system.parked", "true", true);
            parked = true;
        }
    }

    private void park() {
        System.out.println("Parking system...");
        try {
            parkingManager.performManagementActions();
        } catch (ConnectionLostException e) {
            notifyDelegatesForSystemError(new ParkingException(e));
        } catch (InterruptedException e) {
            notifyDelegatesForSystemError(new ParkingException(e));
        }
        System.out.println("Parking finished.");
    }

    private void calibrate() {
        System.out.println("Calibrating system...");
        try {
            calibrationManager.performManagementActions();
        } catch (UnknownPanelCurrentStep e) {
            System.out.println("Received UnknownPanelCurrentStep exception, parking system to recalibrate it.");
            notifyDelegatesForParkingActionDueToException(e);
            park();
        } catch (ConnectionLostException e) {
            notifyDelegatesForSystemError(new CalibrationException(e));
        } catch (InterruptedException e) {
            notifyDelegatesForSystemError(new CalibrationException(e));
        }
        System.out.println("System calibration finished.");
    }

    private void calculateNextPosition() {
        System.out.println("Calculating next doPosition...");

        sunPositionData.latitude = gpsData.getLatitude();
        sunPositionData.longitude = gpsData.getLongitude();
        sunPositionData.setTime(gpsData.getTime());
        sunPositionData.elevation = gpsData.getAltitude();

        try {
            SunPositionCalculator.calculateSunPosition(sunPositionData);
        } catch (Exception e) {
            notifyDelegatesForSystemError(e);
        }

        System.out.println("Azimuth: " + sunPositionData.azimuth + ", Zenith: " + sunPositionData.zenith);

        System.out.println("Calculation finished.");
    }

    private void doPosition() {
        System.out.println("Positioning system...");
        try {
            positioningManager.performManagementActions();
        } catch (CurrentTimeBeforeSunriseException e) {
            notifyDelegatesForParkingActionDueToException(e);
            safePark();
        } catch (CurrentTimeAfterSunsetException e) {
            notifyDelegatesForParkingActionDueToException(e);
            safePark();
        } catch (ConnectionLostException e) {
            notifyDelegatesForSystemError(new PositioningException(e));
        } catch (InterruptedException e) {
            notifyDelegatesForSystemError(new PositioningException(e));
        }
        System.out.println("Positioning finished.");
    }

    @Override
    public void disconnected() {
        for (SystemManagerListener listener : listeners) {
            listener.boardDisconnected();
        }
    }

    @Override
    public void incompatible() {
        incompatible(ioio);
    }

    @Override
    public void incompatible(final IOIO ioio) {
        for (SystemManagerListener listener : listeners) {
            listener.incompatibleBoard(ioio);
        }
    }

    private void notifyDelegatesForSystemError(final Exception e) {
        for (SystemManagerListener listener : listeners) {
            listener.systemError(e);
        }
    }

    private void notifyDelegatesForParkingActionDueToException(final SystemException e) {
        for (SystemManagerListener listener : listeners) {
            listener.performingParkDueTo(e);
        }
    }
}
