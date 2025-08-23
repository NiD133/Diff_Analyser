package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for the {@link Validate} helper class.
 */
public class ValidateTest {

    /**
     * Tests that the noNullElements method throws a NullPointerException
     * when the input array itself is null.
     */
    @Test(expected = NullPointerException.class)
    public void noNullElementsShouldThrowNpeForNullArray() {
        // Arrange: The input array is null.
        Object[] nullArray = null;
        String message = "The array must not contain null elements";

        // Act: Calling the method with a null array.
        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
        Validate.noNullElements(nullArray, message);
    }
}