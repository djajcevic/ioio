package hr.djajcevic.spc;

/**
 * @author djajcevic | 24.06.2015.
 */
public interface PositioningListener {

    void positionChanged(SolarPanelController controller);

    void reachedStartPosition(SolarPanelController controller);

    void reachedEndPosition(SolarPanelController controller);

    void systemError(SolarPanelController controller);

}
