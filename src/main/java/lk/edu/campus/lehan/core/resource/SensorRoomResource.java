package lk.edu.campus.lehan.core.resource;

import lk.edu.campus.lehan.core.dao.RoomDAO;
import lk.edu.campus.lehan.core.dao.SensorDAO;
import lk.edu.campus.lehan.core.model.Room;
import lk.edu.campus.lehan.core.model.Sensor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.EntityTag;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoomResource {
    private RoomDAO roomDAO = RoomDAO.getInstance();
    private SensorDAO sensorDAO = SensorDAO.getInstance();

    @GET
    public Response getAllRooms() {
        return Response.ok(roomDAO.getAllRooms()).build();
    }

    @POST
    public Response createRoom(Room room) {
        if (room == null || room.getName() == null || room.getName().trim().isEmpty() || room.getCapacity() <= 0) {
            throw new lk.edu.campus.lehan.core.exception.MalformedPayloadException(
                    "Room name cannot be empty and capacity must be positive.");
        }
        Room created = roomDAO.createRoom(room);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId, @Context Request request) {
        Room room = roomDAO.getRoomById(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        EntityTag eTag = new EntityTag(Integer.toString(room.hashCode()));
        Response.ResponseBuilder builder = request.evaluatePreconditions(eTag);

        if (builder != null) {
            // Preconditions are met, return 304 Not Modified
            return builder.build();
        }

        // Return 200 OK with the ETag
        return Response.ok(room).tag(eTag).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getRoomById(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Business Logic Constraint: A room cannot be deleted if it has active sensors
        // assigned to it.
        List<Sensor> assignedSensors = sensorDAO.getAllSensors().stream()
                .filter(s -> roomId.equals(s.getRoomId()))
                .collect(Collectors.toList());

        boolean hasActiveSensors = assignedSensors.stream()
                .anyMatch(s -> "ACTIVE".equalsIgnoreCase(s.getStatus()));

        if (hasActiveSensors || !assignedSensors.isEmpty()) {
            throw new lk.edu.campus.lehan.core.exception.RoomNotEmptyException(
                    "Cannot delete room containing sensors.");
        }

        roomDAO.deleteRoom(roomId);
        return Response.noContent().build();
    }
}
