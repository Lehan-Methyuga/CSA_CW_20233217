package lk.edu.campus.lehan.core.dao;

import lk.edu.campus.lehan.core.model.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Client Server Architecture CW (Lehan Methyuga - 20233217)
 */

public class RoomDAO {
    // Singleton Instance
    private static final RoomDAO INSTANCE = new RoomDAO();

    private final ConcurrentMap<String, Room> roomStore = MockDatabase.ROOMS;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private RoomDAO() {
        // Optional: Some initial mock data
        Room mockLib = new Room("LIB-301", "Library Quiet Study", 50);
        roomStore.put(mockLib.getId(), mockLib);
    }

    public static RoomDAO getInstance() {
        return INSTANCE;
    }

    public List<Room> getAllRooms() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(roomStore.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    public Room getRoomById(String id) {
        lock.readLock().lock();
        try {
            return roomStore.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Room createRoom(Room room) {
        lock.writeLock().lock();
        try {
            if (room.getId() == null || room.getId().trim().isEmpty()) {
                room.setId("ROOM-" + UUID.randomUUID().toString().substring(0, 8));
            }
            // Thread-safe update
            roomStore.put(room.getId(), room);
            return room;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Room updateRoom(Room room) {
        lock.writeLock().lock();
        try {
            if (roomStore.containsKey(room.getId())) {
                roomStore.put(room.getId(), room);
                return room;
            }
            return null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean deleteRoom(String id) {
        lock.writeLock().lock();
        try {
            return roomStore.remove(id) != null;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
