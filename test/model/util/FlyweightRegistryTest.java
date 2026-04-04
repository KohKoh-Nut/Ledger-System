package model.util;

import model.data.Name;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class FlyweightRegistryTest {

    @Test
    @DisplayName("Should register and retrieve objects by name")
    void testRegistrationAndRetrieval() {
        FlyweightRegistry<String> registry = new FlyweightRegistry<>();
        Name food = Name.of("Food");
        String id = "uuid-123";
        String obj = "FoodCategoryObject";

        registry.register(food, id, obj);

        assertEquals(obj, registry.getByName(food), "Registry should return the stored object");
        assertNull(registry.getByName(Name.of("NonExistent")), "Should return null for unknown names");
    }

    @Test
    @DisplayName("Should update mapping correctly on rename")
    void testUpdateName() {
        FlyweightRegistry<Integer> registry = new FlyweightRegistry<>();
        Name oldName = Name.of("Old");
        Name newName = Name.of("New");
        String id = "id-1";

        registry.register(oldName, id, 100);
        registry.updateName(oldName, newName, id);

        assertNull(registry.getByName(oldName), "Old name should be removed from mapping");
        assertEquals(100, registry.getByName(newName), "New name should point to the original object");
    }
}