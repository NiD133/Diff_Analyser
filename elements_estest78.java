package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for exception handling in the {@link Elements} class.
 */
public class Elements_ESTestTest78 { // Note: Original class name is kept as requested.

    /**
     * Verifies that the next() method throws an IllegalStateException when provided with a
     * syntactically invalid CSS query. The query parser should reject a selector that
     * ends with a dot without a class name.
     */
    @Test
    public void nextWithIncompleteClassSelectorThrowsIllegalStateException() {
        // Arrange: An empty Elements collection is sufficient, as the query parsing
        // fails before any elements are evaluated.
        Elements elements = new Elements();
        String invalidQuery = "div."; // An incomplete class selector is invalid.

        // Act & Assert
        try {
            elements.next(invalidQuery);
            fail("Expected an IllegalStateException for the invalid CSS query, but no exception was thrown.");
        } catch (IllegalStateException e) {
            // Assert that the correct exception was thrown with the expected message from the query parser.
            String expectedMessage = "CSS identifier expected, but end of input found";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}