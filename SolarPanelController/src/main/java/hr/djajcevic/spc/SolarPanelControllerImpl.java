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

    public SolarPanelControllerImpl(final PositioningDelegate positioningDelegate) {
        this.positioningDelegate = positioningDelegate;
        systemInformation = new SystemInformation();
    }

    @Override
    public void checkSystem() {
        systemCheckedAndRepositioned = false;

        boolean proceed = checkSystemInformation();

        if (false == proceed) return;

        if (false == systemCheckedAndRepositioned) return;

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
        // park system so we can now that system is at start position
        parkSystem();

        int moveByAmount = positioningDelegate.calibrationStepRange();

        // move to calibration position
        for (int i = 0; i < moveByAmount; i++) {
            positioningDelegate.moveXPanelRight(systemInformation);
        }

        // do calibrate system
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
        positioningDelegate.moveXPanelFarRight(systemInformation);
        positioningDelegate.moveXPanelFarLeft(systemInformation);
    }

    @Override
    public void parkSystem() {
        positioningDelegate.moveXPanelFarLeft(systemInformation);
        positioningDelegate.centerYPanel(systemInformation);
        systemInformation.setParked(true);
    }

    @Override
    public void doPosition() {

        checkSystem();

        if (systemCheckedAndRepositioned == false) {
            System.err.println("System not checked or repositioned");
            return;
        }

        final SunPositionData spa = new SunPositionData();
        spa.delta_ut1 = 0;
        spa.delta_t = 67;
//        spa.latitude = 45.837;
//        spa.longitude = 16.0389;
        spa.latitude = systemInformation.getLatitude();
        spa.longitude = systemInformation.getLongitude();
//        spa.elevation = 150;
//        spa.pressure = 820;
        spa.elevation = systemInformation.getElevation();
        spa.pressure = systemInformation.getPressure();
        SunPositionCalculator.calculateSunPosition(spa);

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
        doPosition();

        Thread.sleep(1000); // let other do their stuff
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
