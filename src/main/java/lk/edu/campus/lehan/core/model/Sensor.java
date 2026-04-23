package lk.edu.campus.lehan.core.model;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */


public class Sensor {
    private String id; // Unique identifier, e.g., "TEMP-001"
    private String type; // Category, e.g., "Temperature", "Occupancy", "CO2"
    private String status; // Current state: "ACTIVE", "MAINTENANCE", or "OFFLINE"

    // The currentValue is volatile to ensure threads consistently see the latest
    // value on concurrent POSTs to readings
    private volatile double currentValue;
    private String roomId; // Foreign key linking to the Room

    // No-argument constructor for JAXB/Jackson
    public Sensor() {
    }

    public Sensor(String id, String type, String status, String roomId) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.roomId = roomId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
