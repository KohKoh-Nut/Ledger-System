package model.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class NameTest {

    @Test
    @DisplayName("Should normalize strings to Title Case")
    void testNormalization() {
        assertAll("Title Case Checks",
                () -> assertEquals("Fast Food", Name.of("fast food").toString()),
                () -> assertEquals("Nus", Name.of("NUS").toString()), // Tests toLowerCase logic
                () -> assertEquals("Java Programming", Name.of("  java   programming  ").toString())
        );
    }

    @Test
    @DisplayName("Should truncate long names correctly")
    void testTruncation() {
        Name original = Name.of("Entertainment");

        assertEquals("Enter", original.truncate(5).toString());
        assertEquals("Entertainment", original.truncate(20).toString(), "Should return 'this' if within limit");
    }

    @Test
    @DisplayName("Should compare names alphabetically")
    void testComparison() {
        Name alpha = Name.of("Alpha");
        Name beta = Name.of("Beta");

        assertTrue(alpha.compareTo(beta) < 0);
        assertEquals(0, alpha.compareTo(Name.of("alpha")));
    }

    @Test
    @DisplayName("Should handle empty or null-like input safely")
    void testEdgeCases() {
        // Checking your stream filter for whitespace-only strings
        assertDoesNotThrow(() -> Name.of("    "));
        assertEquals("_", Name.of("").toString());
    }
}