package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling toggleClass with a null class name throws an IllegalArgumentException.
     * This is the expected behavior as the class name must not be null.
     */
    @Test
    public void toggleClassWithNullClassNameThrowsIllegalArgumentException() {
        // Arrange: Create a simple document and select an element.
        Document doc = Parser.parse("<p class='existing'>Test</p>");
        Elements elements = doc.select("p");

        // Act & Assert: Attempt to toggle a null class and verify the exception.
        try {
            elements.toggleClass(null);
            fail("Expected an IllegalArgumentException to be thrown for a null class name.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is clear and helpful.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}