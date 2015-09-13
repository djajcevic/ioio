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
import lombok.Setter;

import java.io.IOException;
import java.util.*;

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

    boolean systemCheckModeOn = false;
    boolean sleepOn = true;

    @Setter
    @Getter
    Delegate delegate;

    @Getter
    private SunPositionData sunPositionData;

    private void initialize() throws ConnectionLostException, InterruptedException {

        notifyDelegatesForSystemMessage("Initializing");

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
        calculateNextPosition();
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

        try {
            if (delegate != null) {
                delegate.beforeLoop(this);
            }

            if (sleepOn) {
                notifyDelegatesForSystemMessage("Sleeping");
                Thread.sleep(1000);
                return;
            }

            if (systemCheckModeOn) {
                checkSystem();
                Thread.sleep(1000);
                return;
            }

            safePark();
            calibrate();
            calculateNextPosition();
            doPosition();
            Thread.sleep(12000);
        } catch (Exception e) {
            notifyDelegatesForSystemError(e);
        }
    }

    private void checkSystem() {
        try {
            notifyDelegatesForSystemMessage("Checking system...");
            notifyDelegatesForSystemMessage("Checking system...");
            notifyDelegatesForSystemMessage("Checking x axis");
            xAxisController.checkConnections();
            notifyDelegatesForSystemMessage("Checking y axis");
            yAxisController.checkConnections();

            notifyDelegatesForSystemMessage("Checking compass");
            compassReader.readData();

            System.out.println(compassData);

            notifyDelegatesForSystemMessage("Checking gps");
            gpsReader.readData();

            System.out.println(gpsData);

            notifyDelegatesForSystemMessage("System checket.");
        } catch (ConnectionLostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        notifyDelegatesForSystemMessage("Parking system...");
        try {
            parkingManager.performManagementActions();
        } catch (ConnectionLostException e) {
            notifyDelegatesForSystemError(new ParkingException(e));
        } catch (InterruptedException e) {
            notifyDelegatesForSystemError(new ParkingException(e));
        }
        notifyDelegatesForSystemMessage("Parking finished.");
    }

    private void calibrate() {
        notifyDelegatesForSystemMessage("Calibrating system...");
        try {
            calibrationManager.performManagementActions();
        } catch (UnknownPanelCurrentStep e) {
            notifyDelegatesForSystemMessage("Received UnknownPanelCurrentStep exception, parking system to recalibrate it.");
            notifyDelegatesForParkingActionDueToException(e);
            park();
        } catch (ConnectionLostException e) {
            notifyDelegatesForSystemError(new CalibrationException(e));
        } catch (InterruptedException e) {
            notifyDelegatesForSystemError(new CalibrationException(e));
        }
        notifyDelegatesForSystemMessage("System calibration finished.");
    }

    private void calculateNextPosition() {
        notifyDelegatesForSystemMessage("Calculating next doPosition...");

        try {
            sunPositionData.latitude = gpsData.getLatitude();
            sunPositionData.longitude = gpsData.getLongitude();
            sunPositionData.setTime(gpsData.getTime());
            sunPositionData.elevation = gpsData.getAltitude();
            SunPositionCalculator.calculateSunPosition(sunPositionData);
        } catch (Exception e) {
            notifyDelegatesForSystemError(e);
        }

        sunPositionData.sunriseCalendar = calculateTimeFromSunPositionDataTime(sunPositionData.sunrise);


        sunPositionData.sunsetCalendar = calculateTimeFromSunPositionDataTime(sunPositionData.sunset);

        System.out.println("Azimuth: " + sunPositionData.azimuth + ", Zenith: " + sunPositionData.zenith);

        notifyDelegatesForSystemMessage("Calculation finished.");
    }

    private void doPosition() {
        notifyDelegatesForSystemMessage("Positioning system...");
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
        notifyDelegatesForSystemMessage("Positioning finished.");
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

    private void notifyDelegatesForSystemMessage(final String message) {
        System.out.println("SystemManager: " + message);
        for (SystemManagerListener listener : listeners) {
            listener.message(message);
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

    public void setSystemCheckModeOn(final boolean systemCheckModeOn) {
        this.systemCheckModeOn = systemCheckModeOn;
    }

    public void setSleepOn(final boolean sleepOn) {
        this.sleepOn = sleepOn;
    }

    public static interface Delegate {
        void beforeLoop(SystemManager systemManager);
    }

    public static Calendar calculateTimeFromSunPositionDataTime(double sunPositionDataTime) {
        Calendar calendar = GregorianCalendar.getInstance();
        TimeZone aDefault = TimeZone.getDefault();
        boolean b = aDefault.inDaylightTime(new Date());
        int correction = b ? 1 : 0;
        calendar.setTimeZone(aDefault);

        double min = 60.0 * (sunPositionDataTime - (int) (sunPositionDataTime));
        double sec = 60.0 * (min - (int) min);
        calendar.set(Calendar.HOUR_OF_DAY, (int) sunPositionDataTime + correction);
        calendar.set(Calendar.MINUTE, (int) min);
        calendar.set(Calendar.SECOND, (int) sec);

        return calendar;
    }
}
