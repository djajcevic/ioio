package hr.djajcevic.aee;

/**
 * @author djajcevic | 17.05.2015.
 */
public class AbsoluteEncoder implements Encoder {

    private static final long serialVersionUID = -5235820051302274750L;

    private int position;

    @Override
    public int rotate(final Direction direction) {
        switch (direction) {
            case FORWARD:
                return ++position;
            case BACKWARD:
                return --position;
            default:
                return position;
        }
    }

}
