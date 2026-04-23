package lk.edu.campus.lehan.core.exception;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */
public class MalformedPayloadException extends RuntimeException {
    public MalformedPayloadException(String message) {
        super(message);
    }
}
