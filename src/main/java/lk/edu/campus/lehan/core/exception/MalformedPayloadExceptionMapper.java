package lk.edu.campus.lehan.core.exception;

import lk.edu.campus.lehan.core.model.ErrorMessage;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */
@Provider
public class MalformedPayloadExceptionMapper implements ExceptionMapper<MalformedPayloadException> {

    @Override
    public Response toResponse(MalformedPayloadException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), 400);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .build();
    }
}
