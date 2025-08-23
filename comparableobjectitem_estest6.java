package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains unit tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the getObject() method returns the exact object instance
     * that was provided to the constructor.
     */
    @Test
    public void getObject_shouldReturnTheObjectProvidedInConstructor() {
        // Arrange: Create a ComparableObjectItem with a known comparable key and a specific object value.
        // Using concrete objects makes the test's intent clear and avoids mocking complexity.
        String key = "2023-Q1";
        Object value = new Object(); // A distinct object to serve as the item's value.
        
        ComparableObjectItem item = new ComparableObjectItem(key, value);

        // Act: Retrieve the object from the item.
        Object retrievedValue = item.getObject();

        // Assert: The retrieved object should be the same instance as the one
        // passed to the constructor.
        assertSame("The object returned by getObject() should be the same instance as the one provided.",
                value, retrievedValue);
    }
}