package lk.edu.campus.lehan.core.dao;

import lk.edu.campus.lehan.core.model.Sensor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */

public class SensorDAO {
    // Singleton Instance
    private static final SensorDAO INSTANCE = new SensorDAO();

    private final ConcurrentMap<String, Sensor> sensorStore = MockDatabase.SENSORS;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private SensorDAO() {
    }

    public static SensorDAO getInstance() {
        return INSTANCE;
    }

    public List<Sensor> getAllSensors() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(sensorStore.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Sensor> getSensorsByType(String type) {
        lock.readLock().lock();
        try {
            return sensorStore.values().stream()
                    .filter(s -> type.equalsIgnoreCase(s.getType()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public Sensor getSensorById(String id) {
        lock.readLock().lock();
        try {
            return sensorStore.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Sensor createSensor(Sensor sensor) {
        lock.writeLock().lock();
        try {
            if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
                sensor.setId("SENS-" + UUID.randomUUID().toString().substring(0, 8));
            }
            sensorStore.put(sensor.getId(), sensor);
            return sensor;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean deleteSensor(String id) {
        lock.writeLock().lock();
        try {
            return sensorStore.remove(id) != null;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
