package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;

/**
 * Contains tests for the {@link ComparableObjectItem} class, focusing on the getObject() method.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the getObject() method returns the exact object instance
     * that was provided to the constructor.
     */
    @Test
    public void getObject_shouldReturnTheObjectSetInConstructor() {
        // Arrange: Create distinct, concrete objects for the comparable (x) and the object (y).
        // Using concrete types like String and Date makes the test's setup easy to understand.
        String key = "2023-Q1";
        Date value = new Date();
        ComparableObjectItem item = new ComparableObjectItem(key, value);

        // Act: Retrieve the object from the item.
        Object retrievedObject = item.getObject();

        // Assert: Verify that the retrieved object is the same instance as the one used during creation.
        // assertSame is used to check for object identity, which is the expected contract of a getter.
        assertSame("The object returned by getObject() should be the same instance provided to the constructor.",
                value, retrievedObject);
    }

    /**
     * Verifies that getObject() correctly returns a null value if null was
     * passed to the constructor.
     */
    @Test
    public void getObject_shouldReturnNullWhenConstructedWithNull() {
        // Arrange
        String key = "2023-Q2";
        ComparableObjectItem item = new ComparableObjectItem(key, null);

        // Act
        Object retrievedObject = item.getObject();

        // Assert
        assertNull("getObject() should return null if the item was constructed with a null object.",
                retrievedObject);
    }
}