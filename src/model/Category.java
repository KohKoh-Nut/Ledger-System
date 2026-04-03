package model;

import model.util.FlyweightRegistry;
import model.util.IdManager;

/**
 * Represents a financial category (e.g., Food, Transport).
 * Implements the Flyweight pattern to ensure memory efficiency by sharing
 * unique Category instances across the application.
 */
public final class Category {

    /**
     * Maps a name & a unique UUID to the Category object.
     */
    private static final FlyweightRegistry<Category> REGISTRY = new FlyweightRegistry<>();

    private final String id;
    private String name;

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
        Category existing = REGISTRY.getByName(name);
        if (existing != null) return existing;

        // Create new
        String newId = IdManager.generateUniqueId();
        Category newCategory = new Category(newId, name.trim());

        REGISTRY.register(name, newId, newCategory);
        return newCategory;
    }

    /**
     * Updates the category name and synchronizes the lookup maps.
     * @param newName The new name for this category.
     */
    public synchronized void rename(String newName) {
        REGISTRY.updateName(this.name, newName, this.id);
        this.name = newName.trim();
    }

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