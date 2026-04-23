package lk.edu.campus.lehan.core.dao;

import lk.edu.campus.lehan.core.model.Room;
import lk.edu.campus.lehan.core.model.Sensor;
import lk.edu.campus.lehan.core.model.SensorReading;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */


/**
 * Thread-safe Mock Database using ConcurrentHashMap instances.
 * This effectively acts as the singleton "persistence layer" per the lecture
 * patterns!
 */
public class MockDatabase {
    // Stores Room entities indexed by Room ID
    public static final ConcurrentHashMap<String, Room> ROOMS = new ConcurrentHashMap<>();

    // Stores Sensor entities indexed by Sensor ID
    public static final ConcurrentHashMap<String, Sensor> SENSORS = new ConcurrentHashMap<>();

    // Stores SensorReadings. Key is SensorID, value is a list of historical
    // readings.
    // Thread safety of the List is provided in the ReadingDAO logic (via
    // CopyOnWriteArrayList)
    public static final ConcurrentHashMap<String, List<SensorReading>> READINGS_HISTORY = new ConcurrentHashMap<>();
}
