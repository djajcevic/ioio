package hr.djajcevic.spc;

/**
 * @author djajcevic | 25.06.2015.
 */
public interface PositioningDelegate {

    boolean moveXPanelLeft(SystemInformation systemInformation);

    boolean moveXPanelRight(SystemInformation systemInformation);

    boolean moveYPanelUp(SystemInformation systemInformation);

    boolean moveYPanelDown(SystemInformation systemInformation);


}
