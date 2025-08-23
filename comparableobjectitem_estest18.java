package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains unit tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the clone() method creates a new object instance that is
     * a separate entity from the original but holds equal values.
     */
    @Test
    public void cloneShouldReturnADistinctButEqualObject() throws CloneNotSupportedException {
        // Arrange: Create an original item.
        ComparableObjectItem originalItem = new ComparableObjectItem("Key", "Value");

        // Act: Create a clone of the original item.
        ComparableObjectItem clonedItem = (ComparableObjectItem) originalItem.clone();

        // Assert: The clone should be a different object instance...
        assertNotSame("The cloned object should be a new instance.", originalItem, clonedItem);

        // ...but it should be equal in content to the original.
        assertEquals("The cloned object should be equal to the original.", originalItem, clonedItem);
    }
}