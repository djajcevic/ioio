package hr.djajcevic.spc;

/**
 * @author djajcevic | 24.06.2015.
 */
public class SystemInformation {

    private boolean onNorth;
    private double longitude, latitude;

    private boolean parked;

    private boolean compassAvailable;
    private boolean gpsAvailable;

    private boolean xServoAvailable, yServoAvailable;

    public boolean isOnNorth() {
        return onNorth;
    }

    public void setOnNorth(final boolean onNorth) {
        this.onNorth = onNorth;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public boolean isParked() {
        return parked;
    }

    public void setParked(final boolean parked) {
        this.parked = parked;
    }

    public boolean isCompassAvailable() {
        return compassAvailable;
    }

    public void setCompassAvailable(final boolean compassAvailable) {
        this.compassAvailable = compassAvailable;
    }

    public boolean isGpsAvailable() {
        return gpsAvailable;
    }

    public void setGpsAvailable(final boolean gpsAvailable) {
        this.gpsAvailable = gpsAvailable;
    }

    public boolean isxServoAvailable() {
        return xServoAvailable;
    }

    public void setxServoAvailable(final boolean xServoAvailable) {
        this.xServoAvailable = xServoAvailable;
    }

    public boolean isyServoAvailable() {
        return yServoAvailable;
    }

    public void setyServoAvailable(final boolean yServoAvailable) {
        this.yServoAvailable = yServoAvailable;
    }
}