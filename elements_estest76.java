package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling prepend() with a null HTML string throws an IllegalArgumentException.
     * The underlying validation logic should prevent null arguments.
     */
    @Test
    public void prependWithNullHtmlStringShouldThrowIllegalArgumentException() {
        // Arrange: Create a non-empty Elements collection.
        // A single element is sufficient for this test.
        Elements elements = new Elements(new Element("p"));

        // Act & Assert
        try {
            elements.prepend(null);
            fail("Expected an IllegalArgumentException to be thrown for a null argument.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is clear and correct.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}