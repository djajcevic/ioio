package hr.djajcevic.spc.ioio.looper.gps;

import lombok.Data;

import java.util.Calendar;

/**
 * @author djajcevic | 11.08.2015.
 */
@Data
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

}
