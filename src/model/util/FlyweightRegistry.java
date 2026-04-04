package model.util;

import model.data.Name;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A generic registry for managing shared object instances (Flyweights).
 * It maintains a bidirectional lookup: finding an ID by name, and an Object by ID.
 * @param <T> The type of object being managed (e.g., Category, Tag).
 */
public final class FlyweightRegistry<T> {

    /**
     * Primary storage: Maps a unique ID to the shared object instance.
     */
    private final Map<String, T> idToObj = new ConcurrentHashMap<>();

    /**
     * Secondary lookup: Maps a display name to its unique ID.
     */
    private final Map<String, String> nameToId = new ConcurrentHashMap<>();

    /**
     * Registers a new object and its associated name-ID mapping in the pool.
     * @param name   The display name used for lookup.
     * @param id     The unique identifier for the object.
     * @param object The shared instance to be stored.
     */
    public void register(Name name, String id, T object) {
        nameToId.put(name.toString(), id);
        idToObj.put(id, object);
    }

    /**
     * Retrieves an object from the pool based on its display name.
     * @param name The name to search for.
     * @return The shared object instance, or null if no mapping exists.
     */
    public T getByName(Name name) {
        String id = nameToId.get(name.toString());
        return id == null ? null : idToObj.get(id);
    }

    /**
     * Updates the name-to-ID mapping, effectively renaming an entry in the pool.
     * @param oldName The previous name to be removed.
     * @param newName The new name to be registered.
     * @param id      The unique ID associated with the object.
     */
    public void updateName(Name oldName, Name newName, String id) {
        nameToId.remove(oldName.toString());
        nameToId.put(newName.toString(), id);
    }
}