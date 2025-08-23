package org.jsoup.helper;

import org.junit.Test;

/**
 * Tests for the {@link Validate} class.
 */
public class ValidateTest {

    /**
     * Tests that noNullElements() does not throw an exception when given an array
     * that contains only non-null elements.
     */
    @Test
    public void noNullElementsSucceedsForArrayWithNonNulls() {
        // Arrange: Create an array containing a non-null element.
        Object[] validArray = new Object[]{""};

        // Act: Call the method under test.
        // The test will pass if this line executes without throwing an exception.
        Validate.noNullElements(validArray, "This message should not be seen");

        // Assert: No explicit assertion is needed. The absence of an exception is the success condition.
    }
}