package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Validate} helper class.
 */
public class ValidateTest {

    /**
     * Verifies that {@link Validate#noNullElements(Object[])} throws an
     * IllegalArgumentException when the input array contains a null element.
     */
    @Test
    public void noNullElementsShouldThrowExceptionWhenArrayContainsNull() {
        // Arrange: Create an array that includes a null element.
        Object[] arrayWithNulls = new Object[]{"item1", null, "item3"};

        try {
            // Act: Call the method under test.
            Validate.noNullElements(arrayWithNulls);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the exception message is correct.
            String expectedMessage = "Array must not contain any null objects";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}