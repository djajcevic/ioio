package hr.djajcevic.spc;

import hr.djajcevic.spc.calculator.SunPositionCalculator;
import hr.djajcevic.spc.calculator.SunPositionData;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.IOIOLooper;

/**
 * @author djajcevic | 24.06.2015.
 */
public class SolarPanelControllerImpl implements SolarPanelController, IOIOLooper {

    private SystemInformation systemInformation;

    private PositioningDelegate positioningDelegate;

    private PositioningListener positioningListener;

    /**
     * Flag that indicates if system has been initially checked and panel is parked.
     */
    private boolean systemCheckedAndRepositioned = false;
    private boolean systemSleeping = true;

    public SolarPanelControllerImpl(final PositioningDelegate positioningDelegate) {
        this.positioningDelegate = positioningDelegate;
        systemInformation = new SystemInformation();
    }

    @Override
    public void checkSystem() {
        systemSleeping = positioningDelegate.systemSleeping();
        if (systemSleeping) {
            return;
        }

        boolean proceed = checkSystemInformation();

        if (false == proceed) return;

        if (true == systemCheckedAndRepositioned) return;

        calibrateSystem();

        findNorth();

        systemCheckedAndRepositioned = true;

    }

    private boolean checkSystemInformation() {
        if (systemInformation == null) return false;

        // GPS // check if GPS information is available
        Double latitude = systemInformation.getLatitude();
        Double longitude = systemInformation.getLongitude();
        if (false == systemInformation.isGpsAvailable() && (latitude == null || longitude == null)) return false;

        // COMPASS // check if compass data is available
        if (false == systemInformation.isCompassAvailable()) return false;

        // checks passed
        return true;
    }

    /**
     * Make system perform symbol 8 movement after parking the system
     */
    private void calibrateSystem() {
        System.out.println("Calibrating");
        // park system so we can now that system is at start position
        parkSystem();

        int moveByAmount = positioningDelegate.calibrationStepRange();

        // move to calibration position
        System.out.println("Move to calibrating position");
        for (int i = 0; i < moveByAmount; i++) {
            positioningDelegate.moveXPanelRight(systemInformation);
        }

        // do calibrate system
        System.out.println("Performing calibration");
        int times = positioningDelegate.calibrationTakes();
        do {
            for (int i = 0; i < moveByAmount; i++) {
                positioningDelegate.moveXPanelLeft(systemInformation);
                positioningDelegate.moveYPanelUp(systemInformation);
            }

            for (int i = 0; i < moveByAmount; i++) {
                positioningDelegate.moveYPanelDown(systemInformation);
                positioningDelegate.moveXPanelRight(systemInformation);
            }

            times--;
        } while (times > 0);

        // return to start position
        parkSystem();
    }

    /**
     * Moves panel by X axis far right and far left. <br />
     * Positioning delegate is responsible for recording panels North position
     * and providing that data through SystemInformation
     */
    public void findNorth() {
        System.out.println("Finding North");
        System.out.println("moveXPanelFarRight");
        positioningDelegate.moveXPanelFarRight(systemInformation);
        System.out.println("moveXPanelFarLeft");
        positioningDelegate.moveXPanelFarLeft(systemInformation);
    }

    @Override
    public void parkSystem() {
        System.out.println("Parking system");
        System.out.println("moveXPanelFarLeft");
        positioningDelegate.moveXPanelFarLeft(systemInformation);
        System.out.println("centerYPanel");
        positioningDelegate.centerYPanel(systemInformation);
        systemInformation.setParked(true);
    }

    @Override
    public void doPosition() {

        checkSystem();

        if (systemSleeping) {
            System.out.println("System at sleep");
        }

        if (systemCheckedAndRepositioned == false) {
            System.err.println("System not checked or repositioned");
            return;
        }

        final SunPositionData spa = new SunPositionData();
        spa.delta_ut1 = 0;
        spa.delta_t = 67;
        spa.latitude = systemInformation.getLatitude();
        spa.longitude = systemInformation.getLongitude();
        spa.elevation = systemInformation.getElevation();
        spa.pressure = systemInformation.getPressure();
        SunPositionCalculator.calculateSunPosition(spa);

        systemInformation.setTargetAzimut(spa.azimuth);
        systemInformation.setTargetHeight(spa.zenith);

//        spa.azimuth
    }

    @Override
    public void updateSystemInformation(final SystemInformation systemInformation) {
        this.systemInformation = systemInformation;
    }

    public PositioningListener getPositioningListener() {
        return positioningListener;
    }

    public void setPositioningListener(final PositioningListener positioningListener) {
        this.positioningListener = positioningListener;
    }

    @Override
    public void setup(final IOIO ioio) throws ConnectionLostException, InterruptedException {
        positioningDelegate.setup(ioio);
    }

    @Override
    public void loop() throws ConnectionLostException, InterruptedException {
        systemInformation.setLatitude(45.837);
        systemInformation.setLongitude(16.0389);
        systemInformation.setGpsAvailable(true);

        systemInformation.setElevation(150);
        systemInformation.setPressure(820);


        systemInformation.setCompassAvailable(true);

        systemInformation.setXServoAvailable(true);
        systemInformation.setYServoAvailable(true);

        System.out.println("Repositioning");
        doPosition();

        Thread.sleep(2000); // let other do their stuff
    }

    @Override
    public void disconnected() {

    }

    @Override
    public void incompatible() {

    }

    @Override
    public void incompatible(final IOIO ioio) {

    }
}
