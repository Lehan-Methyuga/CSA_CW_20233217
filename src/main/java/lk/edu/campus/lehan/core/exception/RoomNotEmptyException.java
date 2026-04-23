package lk.edu.campus.lehan.core.exception;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */


public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException(String message) {
        super(message);
    }
}
