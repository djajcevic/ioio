package hr.djajcevic.spc.encoder;

/**
 * @author djajcevic | 24.06.2015.
 */
public class ServoReachedStartException extends RuntimeException {

    public ServoReachedStartException() {
        super();
    }

    public ServoReachedStartException(final String message) {
        super(message);
    }

    public ServoReachedStartException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ServoReachedStartException(final Throwable cause) {
        super(cause);
    }

    protected ServoReachedStartException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
