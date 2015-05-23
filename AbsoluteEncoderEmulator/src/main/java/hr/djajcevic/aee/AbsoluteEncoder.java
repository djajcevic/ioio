package hr.djajcevic.aee;

import java.util.Arrays;

/**
 * @author djajcevic | 17.05.2015.
 */
public class AbsoluteEncoder implements Encoder {

    private static final long serialVersionUID = -5235820051302274750L;

    private String name;
    private int position;
    private int resolution;

    public AbsoluteEncoder(String name, int position, int resolution) {
        this.name = name;
        this.position = position;
        this.resolution = resolution;
    }

    @Override
    public int rotate(final Direction direction) {
        switch (direction) {
            case FORWARD:
                ++position;
                break;
            case BACKWARD:
                --position;
                break;
            default:

                break;
        }
        System.out.println(name + ": " + position + ": GRAY " + Arrays.toString(GrayCodeUtil.grayEncode(position, resolution)));
        return position;
    }

}
