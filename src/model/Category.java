package model;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Represents a financial category (e.g., Food, Transport).
 * Implements the Flyweight pattern to ensure memory efficiency by sharing
 * unique Category instances across the application.
 */
public class Category {

    // ----- ATTRIBUTE -----

    /**
     * Maps a unique UUID to the Category object.
     */
    private static final Map<String, Category> ID_CAT_POOL = new HashMap<>();

    /**
     * Maps a display name to its UUID.
     * Uses WeakHashMap so entries can be garbage collected if the name string is no longer used.
     */
    private static final Map<String, String> NAME_ID_POOL = new WeakHashMap<>();

    private final String id;
    private String name;

    // ----- CONSTRUCTOR -----

    /**
     * Private constructor to enforce the use of the static factory method {@link #of(String)}.
     */
    private Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Factory method to retrieve an existing category by name or create a new one.
     * @param name The display name of the category.
     * @return A shared Category instance.
     */
    public static synchronized Category of(String name) {
        Category category = search(name);

        // If category doesn't exist in the pool, instantiate and cache it
        if (category == null) {
            category = create(name);
        }
        return category;
    }

    /**
     * Internal helper to generate a new category and register it in both maps.
     */
    private static synchronized Category create(String name) {
        String newId = IdManager.generateUniqueId();

        Category category = new Category(newId, name);

        // Link the name to the ID, and the ID to the Object
        NAME_ID_POOL.put(name, newId);
        ID_CAT_POOL.put(newId, category);

        return category;
    }

    // ----- CATEGORY-----

    /**
     * Internal Helper to look up a category in the pool using its name.
     */
    private static synchronized Category search(String name) {
        String key = NAME_ID_POOL.get(name);
        if (key == null) return null;
        return ID_CAT_POOL.get(key);
    }

    /**
     * Updates the category name and synchronizes the lookup maps.
     * @param newName The new name for this category.
     */
    public synchronized void rename(String newName) {
        // Remove old name mapping to prevent wrong lookups
        NAME_ID_POOL.remove(this.name);

        this.name = newName;

        // Register the new name mapping to the existing ID
        NAME_ID_POOL.put(newName, this.id);
    }

    // ----- GENERAL -----

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Category other)) return false;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        // Objects.hash handles null safety and distribution for you
        return java.util.Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, id);
    }
}