package lk.edu.campus.lehan.core.resource;

import lk.edu.campus.lehan.core.dao.RoomDAO;
import lk.edu.campus.lehan.core.dao.SensorDAO;
import lk.edu.campus.lehan.core.model.Room;
import lk.edu.campus.lehan.core.model.Sensor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {
    private SensorDAO sensorDAO = SensorDAO.getInstance();
    private RoomDAO roomDAO = RoomDAO.getInstance();

    @GET
    public Response getSensors(@QueryParam("type") String type) {
        List<Sensor> sensors;
        if (type != null && !type.trim().isEmpty()) {
            sensors = sensorDAO.getSensorsByType(type); // Filtered Retrieval constraint
        } else {
            sensors = sensorDAO.getAllSensors();
        }
        return Response.ok(sensors).build();
    }

    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor == null || sensor.getType() == null || sensor.getType().trim().isEmpty()
                || sensor.getStatus() == null || sensor.getStatus().trim().isEmpty()) {
            throw new lk.edu.campus.lehan.core.exception.MalformedPayloadException(
                    "Sensor payload invalid: type and status must be provided and non-empty.");
        }

        // Dependency Validation constraint
        if (sensor.getRoomId() == null || roomDAO.getRoomById(sensor.getRoomId()) == null) {
            throw new lk.edu.campus.lehan.core.exception.LinkedResourceNotFoundException(
                    "Linked Room ID not found in system.");
        }

        Sensor created = sensorDAO.createSensor(sensor);

        // Establish bi-directional link by adding sensor to room's list
        Room room = roomDAO.getRoomById(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().add(created.getId());
        }

        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    // Sub-Resource Locator Pattern mapping requests deeper into the tree
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingsSubResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
