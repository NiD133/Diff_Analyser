package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for {@link Validate}.
 */
public class ValidateTest {

    /**
     * Tests that {@link Validate#noNullElements(Object[])} handles an empty array gracefully.
     * An empty array contains no null elements, so no exception should be thrown.
     */
    @Test
    public void noNullElementsWithEmptyArrayDoesNotThrowException() {
        // Arrange
        Object[] emptyArray = new Object[0];

        // Act
        Validate.noNullElements(emptyArray);

        // Assert
        // The test passes if the method completes without throwing an exception.
    }
}