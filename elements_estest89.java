package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements} class.
 * This class focuses on exception-handling scenarios.
 */
public class ElementsExceptionTest {

    @Test
    public void appendWithNullHtmlShouldThrowIllegalArgumentException() {
        // Arrange: Create an Elements object to test against.
        Document doc = Document.createShell("");
        Elements elements = doc.getAllElements(); // Contains <html>, <head>, <body>

        // Act & Assert: Verify that appending null throws the expected exception.
        try {
            elements.append(null);
            fail("Expected an IllegalArgumentException to be thrown for null HTML input.");
        } catch (IllegalArgumentException e) {
            // Assert that the exception message is correct, confirming the validation logic.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}