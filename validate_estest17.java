package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Validate} helper class.
 */
public class ValidateTest {

    /**
     * Verifies that {@link Validate#noNullElements(Object[], String)} throws an
     * IllegalArgumentException when the provided array contains a null element.
     */
    @Test
    public void noNullElementsShouldThrowExceptionForArrayContainingNull() {
        // Arrange: Create an array that contains a null element and define the expected error message.
        Object[] arrayWithNull = { "one", null, "three" };
        String expectedMessage = "Array must not contain any null objects";

        // Act & Assert: Call the method and verify the expected exception and its message.
        try {
            Validate.noNullElements(arrayWithNull, expectedMessage);
            fail("Expected an IllegalArgumentException to be thrown, but no exception occurred.");
        } catch (IllegalArgumentException e) {
            // Success: The correct exception type was thrown.
            // Now, verify that the exception message is what we expect.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}