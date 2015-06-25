package hr.djajcevic.spc;

/**
 * @author djajcevic | 24.06.2015.
 */
public class SolarPanelControllerImpl implements SolarPanelController {

    private SystemInformation systemInformation;

    private PositioningDelegate positioningDelegate;

    private PositioningListener positioningListener;

    /**
     * Flag that indicates if system has been initially checked and panel is parked.
     */
    private boolean systemCheckedAndRepositioned = false;

    public SolarPanelControllerImpl(final PositioningDelegate positioningDelegate) {
        this.positioningDelegate = positioningDelegate;
    }

    @Override
    public void checkSystem() {
        boolean proceed = checkSystemInformation();

        if (false == proceed) return;

        if (false == systemCheckedAndRepositioned) return;

        parkSystem();

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

    @Override
    public void parkSystem() {
        boolean reachedTarget = false;
        do {
            positioningDelegate.moveXPanelLeft(systemInformation);
        } while (systemInformation.isParkedX());

        do {
            positioningDelegate.moveXPanelLeft(systemInformation);
        } while (systemInformation.isParkedY());

        systemInformation.setParked(true);
    }

    @Override
    public void doPosition() {

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
}
