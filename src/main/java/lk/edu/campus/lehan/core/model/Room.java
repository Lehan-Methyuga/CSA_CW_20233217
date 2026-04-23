package lk.edu.campus.lehan.core.model;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */

public class Room {
    private String id; // Unique identifier, e.g., "LIB-301"
    private String name; // Human-readable name, e.g., "Library Quiet Study"
    private int capacity; // Maximum occupancy for safety regulations

    // Initialized as a thread-safe list to prevent race conditions during
    // concurrent sensor registration
    private List<String> sensorIds = new CopyOnWriteArrayList<>();

    // No-argument constructor required for JAXB / Jackson
    public Room() {
    }

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<String> getSensorIds() {
        return sensorIds;
    }

    // Usually Jackson will call the setter, we preserve thread-safety during
    // replacement
    public void setSensorIds(List<String> sensorIds) {
        this.sensorIds = new CopyOnWriteArrayList<>(sensorIds != null ? sensorIds : List.of());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Room room = (Room) o;
        return capacity == room.capacity &&
                Objects.equals(id, room.id) &&
                Objects.equals(name, room.name) &&
                Objects.equals(sensorIds, room.sensorIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, capacity, sensorIds);
    }
}
