package hr.djajcevic.spc.ioio.looper.exception;

/**
 * @author djajcevic | 26.08.2015.
 */
public class ParkingException extends RuntimeException {
    private static final long serialVersionUID = -5173529710282884019L;

    public ParkingException() {
    }

    public ParkingException(final Throwable cause) {
        super(cause);
    }
}
