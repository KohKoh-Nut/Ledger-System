package model.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A value object representing a normalized String name.
 * Automatically converts input to Title Case (e.g., "fast food" -> "Fast Food").
 */
public final class Name implements Comparable<Name> {
    
    /**
     * Maps a name key to the Name object.
     */
    private static final Map<String, Name> KEY_NAME_POOL = new HashMap<>();
    
    private final String name;
    
    /**
     * Private constructor to enforce the use of the static factory method
     * {@link #of(String)} or {@link #of(String, int)}.
     */
    private Name(String name) {
        this.name = name;
    }
    
    /**
     * Factory method that cached or creates a normalized Name instance.
     * <p>
     * It splits the input by whitespace, capitalizes the first letter of each word,
     * and joins them back with a single space.
     * </p>
     *
     * @param name The raw string input from the user or database.
     * @return A shared Name instance with Title Case formatting.
     */
    public static Name of(String name) {
        if (name == null || name.isBlank())
            return of("_");
        
        // Stream pipeline: Split -> Capitalize -> Join
        String result = Arrays.stream(name.split("\\s+")) // Split by any whitespace
                              .filter(word -> !word.isEmpty())           // Guard against empty strings
                              .map(word -> word.substring(0, 1)
                                               .toUpperCase() +
                                      word.substring(1)
                                          .toLowerCase()) // Force Title Case
                              .collect(Collectors.joining(" "));
        
        return KEY_NAME_POOL.computeIfAbsent(result, Name::new);
    }
    
    /**
     * Factory method that creates a normalized Name instance, truncated to a maximum length.
     * <p>
     * This is useful for UI/CLI constraints where a long name might break table alignment.
     * </p>
     *
     * @param name      The raw string input from the user or database.
     * @param maxLength The maximum allowed characters.
     * @return A normalized Name, truncated if it exceeds maxLength.
     */
    public static Name of(String name, int maxLength) {
        return of(name).truncate(maxLength);
    }
    
    /**
     * Truncate the name to maxLength if exceeded.
     *
     * @param maxLength The maximum allowed characters.
     * @return A truncated Name.
     */
    public Name truncate(int maxLength) {
        if (name.length() <= maxLength)
            return this;
        return of(name.substring(0, maxLength));
    }
    
    @Override
    public int compareTo(Name other) {
        return this.name.compareTo(other.name);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
