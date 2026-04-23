package lk.edu.campus.lehan.core.exception;

import lk.edu.campus.lehan.core.model.ErrorMessage;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */


@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        // Preserve framework-generated HTTP responses (e.g., 400/404/405) instead of
        // incorrectly turning them into 500.
        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }

        // Prevent Java Stack Trace Leak
        System.err.println("Unexpected API Server Error: " + exception.getMessage());

        ErrorMessage errorMessage = new ErrorMessage("An internal server error occurred.", 500);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
    }
}
