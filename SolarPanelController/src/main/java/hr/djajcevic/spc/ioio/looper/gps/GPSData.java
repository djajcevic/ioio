package hr.djajcevic.spc.ioio.looper.gps;

import java.util.Calendar;

/**
 * @author djajcevic | 11.08.2015.
 */
public class GPSData {

    private Calendar time;
    private Double latitude;
    private String latitudeDirection;
    private Double longitude;
    private String longitudeDirection;
    private Long numberOfSatellites;
    private Double altitude;

    public Calendar getTime() {
        return time;
    }

    public void setTime(final Calendar time) {
        this.time = time;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(final Double longitude) {
        this.longitude = longitude;
    }

    public String getLongitudeDirection() {
        return longitudeDirection;
    }

    public void setLongitudeDirection(final String longitudeDirection) {
        this.longitudeDirection = longitudeDirection;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }

    public String getLatitudeDirection() {
        return latitudeDirection;
    }

    public void setLatitudeDirection(final String latitudeDirection) {
        this.latitudeDirection = latitudeDirection;
    }

    public Long getNumberOfSatellites() {
        return numberOfSatellites;
    }

    public void setNumberOfSatellites(final Long numberOfSatellites) {
        this.numberOfSatellites = numberOfSatellites;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(final Double altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "GPSData{" +
                "time=" + time.getTime() +
                ", latitude=" + latitude +
                ", latitudeDirection='" + latitudeDirection + '\'' +
                ", longitude=" + longitude +
                ", longitudeDirection='" + longitudeDirection + '\'' +
                ", numberOfSatellites=" + numberOfSatellites +
                ", altitude=" + altitude +
                '}';
    }
}
