package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for the {@link Elements} class, focusing on exception handling.
 */
public class ElementsExceptionTest {

    /**
     * Verifies that calling nextAll() with a syntactically invalid CSS query
     * throws an IllegalStateException.
     */
    @Test
    public void nextAllWithInvalidQueryThrowsException() {
        // Arrange: Create a simple Elements object. The content is not important for this test.
        Document doc = Parser.parse("<div></div>");
        Elements elements = doc.select("div");
        String invalidQuery = "lkr.7#E@P2PwMVQQ";

        // Act & Assert: Expect an IllegalStateException when the invalid query is used.
        IllegalStateException thrown = assertThrows(
            IllegalStateException.class,
            () -> elements.nextAll(invalidQuery)
        );

        // Verify that the exception message is clear and helpful.
        assertEquals("Could not parse query '" + invalidQuery + "': unexpected token at '@P2PwMVQQ'", thrown.getMessage());
    }
}