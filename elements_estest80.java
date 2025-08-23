package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link Elements} class.
 * The original test class name "Elements_ESTestTest80" was likely auto-generated.
 * A more conventional name would be "ElementsTest".
 */
public class Elements_ESTestTest80 {

    /**
     * Verifies that the is() method throws an IllegalStateException when provided
     * with a syntactically invalid CSS query.
     */
    @Test
    public void isShouldThrowIllegalStateExceptionForInvalidQuery() {
        // Arrange: Create an empty Elements collection and define an invalid query string.
        Elements elements = new Elements();
        String invalidQuery = "{xrk0 ";
        String expectedErrorMessage = "Could not parse query '{xrk0': unexpected token at '{xrk0'";

        // Act & Assert: Attempt to call is() with the invalid query and verify the exception.
        try {
            elements.is(invalidQuery);
            fail("Expected an IllegalStateException to be thrown for the invalid query, but it was not.");
        } catch (IllegalStateException e) {
            // Verify that the exception message is correct, confirming the query parser failed as expected.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}