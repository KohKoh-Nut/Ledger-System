package model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Utility class for generating and tracking unique identifiers.
 * Ensures that no two components in the system share the same ID during a single session.
 */
public class IdManager {
    // ----- Attributes -----

    /**
     * Global registry of IDs assigned during the current execution.
     */
    private static final Set<String> USED_IDS = new HashSet<>();

    // ----- CONSTRUCTOR -----

    // Private constructor prevents instantiation of this utility class
    private IdManager() {}

    // ----- MANAGER -----

    /**
     * Generates a cryptographically strong, unique UUID string.
     * * @return A unique 128-bit identifier in string format.
     */
    public static String generateUniqueId() {
        String id;
        do {
            id = UUID.randomUUID().toString();
        } while (USED_IDS.contains(id));

        USED_IDS.add(id);
        return id;
    }
}