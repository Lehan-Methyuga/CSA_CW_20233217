package lk.edu.campus.lehan.core.exception;

import lk.edu.campus.lehan.core.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */


@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    @Override
    public Response toResponse(SensorUnavailableException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 403);
        return Response.status(Response.Status.FORBIDDEN).entity(errorMessage).build();
    }
}
