package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// Note: The class name and inheritance are preserved from the original test suite.
public class Elements_ESTestTest77 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that the {@link Elements#not(String)} method throws an
     * {@link IllegalArgumentException} when passed a null query string.
     * This ensures that input validation is correctly handled before processing.
     */
    @Test
    public void notWithNullQueryThrowsIllegalArgumentException() {
        // Arrange: Create an empty Elements collection. The state of the collection
        // is irrelevant for this test, as input validation should occur first.
        Elements elements = new Elements();

        // Act & Assert
        try {
            elements.not(null);
            fail("Expected an IllegalArgumentException for a null query, but none was thrown.");
        } catch (IllegalArgumentException e) {
            // Assert that the exception message is as expected, confirming the validation logic.
            assertEquals("String must not be empty", e.getMessage());
        }
    }
}