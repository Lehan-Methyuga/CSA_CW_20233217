package lk.edu.campus.lehan.core.dao;

import lk.edu.campus.lehan.core.model.SensorReading;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */


public class ReadingDAO {
    // Singleton Instance
    private static final ReadingDAO INSTANCE = new ReadingDAO();

    private final ConcurrentMap<String, List<SensorReading>> historyStore = MockDatabase.READINGS_HISTORY;

    private ReadingDAO() {
    }

    public static ReadingDAO getInstance() {
        return INSTANCE;
    }

    public List<SensorReading> getReadingsForSensor(String sensorId) {
        List<SensorReading> readings = historyStore.get(sensorId);
        if (readings == null) {
            return new ArrayList<>(); // return empty list if none exist
        }
        return new ArrayList<>(readings);
    }

    public SensorReading appendReading(String sensorId, SensorReading reading) {
        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        // Concurrent putIfAbsent to ensure a Thread-Safe nested list is initialized
        // safely
        historyStore.putIfAbsent(sensorId, new CopyOnWriteArrayList<>());

        // At this point we safely append to the CopyOnWriteArrayList
        historyStore.get(sensorId).add(reading);

        return reading;
    }
}
