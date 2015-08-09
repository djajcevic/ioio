package hr.djajcevic.spc.ioio.looper.exception;

/**
 * @author djajcevic | 09.08.2015.
 */
public class InvalidPanelStateException extends RuntimeException {

    private static final long serialVersionUID = 5132636246664468682L;

    public InvalidPanelStateException(final String message) {
        super(message);
    }
}
