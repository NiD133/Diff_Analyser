package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements#expectFirst(String)} method.
 */
public class ElementsExpectFirstTest {

    /**
     * Verifies that calling expectFirst() with an invalid CSS selector query
     * throws an IllegalStateException. The underlying QueryParser should fail
     * to parse the invalid syntax.
     */
    @Test
    public void expectFirstWithInvalidQueryThrowsIllegalStateException() {
        // Arrange: An empty Elements list is sufficient as the query parsing
        // happens before any elements are evaluated.
        Elements elements = new Elements();
        String invalidQuery = ")XEN M$jX~$HL\"kq-_^";
        String expectedErrorMessage = "Could not parse query '" + invalidQuery + "': unexpected token at '" + invalidQuery + "'";

        // Act & Assert
        try {
            elements.expectFirst(invalidQuery);
            fail("Expected an IllegalStateException to be thrown for the invalid query.");
        } catch (IllegalStateException e) {
            // Verify that the exception message correctly identifies the parsing error.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}