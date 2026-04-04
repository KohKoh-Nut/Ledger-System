package model.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class IdManagerTest {

    @Test
    @DisplayName("Should generate unique IDs for a large sample size")
    void testUniqueness() {
        int sampleSize = 1000;
        Set<String> generatedIds = new HashSet<>();

        for (int i = 0; i < sampleSize; i++) {
            String id = IdManager.generateUniqueId();

            // If add() returns false, the ID already existed in our local set
            boolean isUnique = generatedIds.add(id);
            assertTrue(isUnique, "Duplicate ID detected at iteration " + i);
        }

        assertEquals(sampleSize, generatedIds.size());
    }

    @Test
    @DisplayName("Should produce a valid UUID string format")
    void testUuidFormat() {
        String id = IdManager.generateUniqueId();

        // UUIDs are 36 characters long (32 hex chars + 4 hyphens)
        assertNotNull(id);
        assertEquals(36, id.length());
        assertTrue(id.contains("-"), "ID should follow standard UUID hyphenated format");
    }
}