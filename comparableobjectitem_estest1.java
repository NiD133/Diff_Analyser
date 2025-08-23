package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the getObject() method returns the exact object that was
     * passed into the constructor. This test specifically checks the case
     * where the object is null.
     */
    @Test
    public void getObject_shouldReturnNull_whenConstructedWithNullObject() {
        // Arrange: Create an item with a non-null comparable and a null object.
        // The constructor allows the object (y-value) to be null.
        Comparable<String> xValue = "X1";
        ComparableObjectItem item = new ComparableObjectItem(xValue, null);

        // Act: Retrieve the object from the item.
        Object retrievedObject = item.getObject();

        // Assert: The retrieved object should be the same null value provided at construction.
        assertNull("The object retrieved should be null, as it was set to null in the constructor.", retrievedObject);
    }
}