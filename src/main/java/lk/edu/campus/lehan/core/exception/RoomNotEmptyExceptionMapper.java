package lk.edu.campus.lehan.core.exception;

import lk.edu.campus.lehan.core.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */


@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {
    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 409);
        return Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
    }
}
