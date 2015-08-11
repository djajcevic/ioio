package hr.djajcevic.spc.ioio.looper.compas;

/**
 * @author djajcevic | 11.08.2015.
 */
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

    public Double getX() {
        return x;
    }

    public void setX(final Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(final Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(final Double z) {
        this.z = z;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(final Double heading) {
        this.heading = heading;
    }

    public Double getHeadingDegrees() {
        return headingDegrees;
    }

    public void setHeadingDegrees(final Double headingDegrees) {
        this.headingDegrees = headingDegrees;
    }

    @Override
    public String toString() {
        return "CompassData{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", heading=" + heading +
                ", headingDegrees=" + headingDegrees +
                '}';
    }
}
