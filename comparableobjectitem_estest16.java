package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the equals() method returns false when a ComparableObjectItem
     * is compared with an object of a different, unrelated type.
     */
    @Test
    public void equals_whenComparedWithDifferentType_shouldReturnFalse() {
        // Arrange: Create an instance of ComparableObjectItem and an instance of a different class.
        ComparableObjectItem item = new ComparableObjectItem("Key", "Value");
        Object otherObject = new Object();

        // Act: Compare the two objects.
        boolean isEqual = item.equals(otherObject);

        // Assert: The result should be false, as per the equals() contract.
        assertFalse(isEqual);
    }
}