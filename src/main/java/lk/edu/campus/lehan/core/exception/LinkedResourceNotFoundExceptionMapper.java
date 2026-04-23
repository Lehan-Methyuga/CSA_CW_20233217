package lk.edu.campus.lehan.core.exception;

import lk.edu.campus.lehan.core.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */


@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        // 422 Unprocessable Entity
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 422);
        return Response.status(422).entity(errorMessage).build();
    }
}
