package hr.djajcevic.spc;

/**
 * @author djajcevic | 24.06.2015.
 */
public interface SolarPanelController {

    /**
     * Move panel to park position (far left or right)
     */
    void parkSystem();

    /**
     * Retrieve required data
     */
    void checkSystem();

    /**
     * Position panel to new position
     */
    void doPosition();

    /**
     * Update system information. This is the critical point of
     * positioning procedure. System adopts with each change.
     *
     * @param systemInformation
     */
    void updateSystemInformation(SystemInformation systemInformation);

}
