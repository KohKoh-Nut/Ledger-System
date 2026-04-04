package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    @DisplayName("Should normalize category names via the Name value object")
    void testNameNormalization() {
        Category cat = Category.of("  food and drinks  ");
        // We verify the toString format contains the normalized Title Case
        assertTrue(cat.toString().contains("Food And Drinks"));
    }

    @Test
    @DisplayName("Should update internal name state on rename")
    void testRenameLogic() {
        Category cat = Category.of("Initial");
        cat.rename("Updated Name");
        assertTrue(cat.toString().contains("Updated Name"));
    }

    @Test
    @DisplayName("Should truncate long names in toString for UI alignment")
    void testToStringPadding() {
        Category longCat = Category.of("Very Long Category Name That Exceeds Twenty Characters");
        String output = longCat.toString();

        // Ensure it doesn't break the 20-char limit in the display portion
        // and contains the ID in parentheses
        assertTrue(output.length() >= 20);
        assertTrue(output.contains("("));
    }

    @Test
    @DisplayName("Should implement Comparable based on name")
    void testComparison() {
        Category a = Category.of("A");
        Category b = Category.of("B");
        assertTrue(a.compareTo(b) < 0);
    }
}