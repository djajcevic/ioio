package hr.djajcevic.spc.encoder;

/**
 * @author djajcevic | 24.06.2015.
 */
public class ServoReachedEndException extends RuntimeException {

    public ServoReachedEndException() {
        super();
    }

    public ServoReachedEndException(final String message) {
        super(message);
    }

    public ServoReachedEndException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ServoReachedEndException(final Throwable cause) {
        super(cause);
    }

    protected ServoReachedEndException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
