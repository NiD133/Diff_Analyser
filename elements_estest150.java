package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements#select(String)} method.
 */
public class ElementsTest {

    /**
     * Verifies that calling select() with a syntactically invalid CSS query
     * throws an IllegalStateException.
     */
    @Test
    public void selectWithInvalidSelectorSyntaxShouldThrowIllegalStateException() {
        // Arrange: Create an empty Elements collection and define an invalid query.
        Elements elements = new Elements();
        String invalidQuery = "<_+];Lxz(u?A|i1x";

        try {
            // Act: Attempt to select elements using the invalid query.
            elements.select(invalidQuery);
            
            // Assert: If no exception is thrown, the test should fail.
            fail("Expected an IllegalStateException to be thrown due to invalid selector syntax.");
        } catch (IllegalStateException e) {
            // Assert: Verify that the correct exception was thrown and its message is informative.
            String expectedMessage = "Could not parse query";
            assertTrue(
                "The exception message should indicate that the query could not be parsed.",
                e.getMessage().contains(expectedMessage)
            );
            assertTrue(
                "The exception message should include the invalid query string.",
                e.getMessage().contains(invalidQuery)
            );
        }
    }
}