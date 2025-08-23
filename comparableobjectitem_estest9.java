package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the
     * 'x' (comparable) argument is null, as this is a documented constraint.
     */
    @Test
    public void constructorShouldThrowIllegalArgumentExceptionForNullXArgument() {
        // Arrange: Define the invalid null 'x' argument. The 'y' argument is irrelevant
        // for this test, so we can use any value, including null.
        Comparable<?> nullX = null;
        Object anyY = "any-object";

        try {
            // Act: Attempt to create an item with the null 'x' value.
            new ComparableObjectItem(nullX, anyY);
            
            // Assert: If the constructor does not throw an exception, the test fails.
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the thrown exception has the expected message.
            assertEquals("The exception message does not match the expected contract.",
                         "Null 'x' argument.", e.getMessage());
        }
    }
}