package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the hashCode is correctly recalculated when the object
     * component is updated from null to a non-null value.
     */
    @Test
    public void hashCode_shouldChange_whenObjectIsUpdatedFromNull() {
        // Arrange: Create an item with a null object and calculate its initial hash code.
        String comparable = "TestKey";
        ComparableObjectItem item = new ComparableObjectItem(comparable, null);
        int initialHashCode = item.hashCode();

        // Act: Update the item with a non-null object and get the new hash code.
        Object newObject = "TestObject";
        item.setObject(newObject);
        int updatedHashCode = item.hashCode();

        // Assert: The hash code should be different after the update.
        assertNotEquals("The hash code should change when the object is updated from null to a non-null value.",
                        initialHashCode, updatedHashCode);
    }
}