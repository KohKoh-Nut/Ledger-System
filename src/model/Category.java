package model;

import model.data.Name;
import model.util.FlyweightRegistry;
import model.util.IdManager;

/**
 * Represents a financial category (e.g., Food, Transport).
 * Implements the Flyweight pattern to ensure memory efficiency by sharing
 * unique Category instances across the application.
 */
public final class Category implements Comparable<Category> {

    /**
     * Maps a name & a unique UUID to the Category object.
     */
    private static final FlyweightRegistry<Category> REGISTRY = new FlyweightRegistry<>();

    private final String id;
    private Name name;

    /**
     * Private constructor to enforce the use of the static factory method {@link #of(String)}.
     */
    private Category(String id, Name name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Factory method to retrieve an existing category by name or create a new one.
     * @param name The display name of the category.
     * @return A shared Category instance.
     */
    public static synchronized Category of(String name) {
        Name normName = Name.of(name);

        Category existing = REGISTRY.getByName(normName);
        if (existing != null) return existing;

        // Create new
        String newId = IdManager.generateUniqueId();
        Category newCategory = new Category(newId, normName);

        REGISTRY.register(normName, newId, newCategory);
        return newCategory;
    }

    /**
     * Updates the category name and synchronizes the lookup maps.
     * @param newName The new name for this category.
     */
    public synchronized void rename(String newName) {
        Name normName = Name.of(newName);

        REGISTRY.updateName(this.name, normName, this.id);
        this.name = normName;
    }

    @Override
    public int compareTo(Category other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return String.format("%-20s", name.truncate(20));
    }
}