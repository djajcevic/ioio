package hr.djajcevic.spc;

import lombok.Data;

/**
 * @author djajcevic | 24.06.2015.
 */
@Data
public class SystemInformation {

    /**
     * Flag indicating if system is parked (parking position)
     */
    private boolean parked;

    /**
     * Flag indicating if system is parked (parking position) at X axis
     */
    private boolean parkedX;

    /**
     * Flag indicating if system is parked (parking position) at Y axis
     */
    private boolean parkedY;

    /**
     * Flag indicating if compass is available
     */
    private boolean compassAvailable;

    /**
     * Flag indicating if system (panel) is facing north
     */
    private boolean onNorth;

    /**
     * Flag indicating if GPS is available
     */
    private boolean gpsAvailable;

    /**
     * Current system longitude
     */
    private Double longitude;

    /**
     * Current system latitude
     */
    private Double latitude;

    /**
     * Flags indicating servo availability
     */
    private boolean xServoAvailable, yServoAvailable;

    /**
     * Target azimuth or panel x position
     */
    private Double targetAzimut;

    /**
     * Target height or y panel position2
     */
    private Double targetHeight;

    /**
     * Flag indicating target azimuth has been reached and panel is at right x position
     */
    private Boolean targetAzimuthReached;

    /**
     * Flag indicating target height has been reached and panel is at right y position
     */
    private Boolean targetHeightReached;

    /**
     * Panel information
     */
    ServoInformation servoInformation;


}