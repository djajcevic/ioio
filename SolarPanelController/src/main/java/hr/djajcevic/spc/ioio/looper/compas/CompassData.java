package hr.djajcevic.spc.ioio.looper.compas;

/**
 * @author djajcevic | 11.08.2015.
 */

import lombok.Data;

@Data
public class CompassData {

    private Double x;
    private Double y;
    private Double z;
    private Double heading;
    private Double headingDegrees;

    public CompassData() {
    }

    public CompassData(final Double x, final Double y, final Double z, final Double heading, final Double headingDegrees) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.heading = heading;
        this.headingDegrees = headingDegrees;
    }

    public boolean isDataValid(){
        return  x != null && y != null && z != null;
    }

}
