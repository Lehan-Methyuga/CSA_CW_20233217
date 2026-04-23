package lk.edu.campus.lehan.core.resource;

import lk.edu.campus.lehan.core.dao.ReadingDAO;
import lk.edu.campus.lehan.core.dao.SensorDAO;
import lk.edu.campus.lehan.core.model.Sensor;
import lk.edu.campus.lehan.core.model.SensorReading;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */

/**
 * Note: No \@Path at class level because it acts as a Sub-Resource routed
 * dynamically.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    private String sensorId;
    private ReadingDAO readingDAO = ReadingDAO.getInstance();
    private SensorDAO sensorDAO = SensorDAO.getInstance();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getHistoricalReadings() {
        Sensor sensor = sensorDAO.getSensorById(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<SensorReading> history = readingDAO.getReadingsForSensor(sensorId);
        return Response.ok(history).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = sensorDAO.getSensorById(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // State Constraint Validation
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus()) || "OFFLINE".equalsIgnoreCase(sensor.getStatus())) {
            throw new lk.edu.campus.lehan.core.exception.SensorUnavailableException(
                    "Sensor is currently offline or in maintenance.");
        }

        SensorReading created = readingDAO.appendReading(sensorId, reading);

        // Required Side Effect: Update currentValue on parent Sensor object
        sensor.setCurrentValue(created.getValue());

        return Response.status(Response.Status.CREATED).entity(created).build();
    }
}
