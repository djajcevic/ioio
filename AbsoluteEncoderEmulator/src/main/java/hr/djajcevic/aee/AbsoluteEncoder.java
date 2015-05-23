package hr.djajcevic.aee;

/**
 * @author djajcevic | 17.05.2015.
 */
public class AbsoluteEncoder implements Encoder {

    private static final long serialVersionUID = -5235820051302274750L;

    private String name;
    private int position;

    public AbsoluteEncoder(String name, int position) {
        this.name = name;
        this.position = position;
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
        System.out.println(name + ": " + position);
        return position;
    }

}
