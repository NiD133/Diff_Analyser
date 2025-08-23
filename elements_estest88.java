package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements} class, focusing on attribute-related methods.
 */
public class ElementsTest {

    /**
     * Verifies that calling the attr(String) method with a null attribute key
     * throws an IllegalArgumentException, as null keys are not permitted.
     */
    @Test
    public void attrWithNullKeyThrowsIllegalArgumentException() {
        // Arrange: Create an Elements object. The content is not important for this test.
        Document doc = Parser.parseBodyFragment("<div></div>", "");
        Elements elements = doc.getAllElements();

        try {
            // Act: Attempt to get an attribute using a null key.
            elements.attr(null);
            fail("Expected an IllegalArgumentException to be thrown for a null attribute key.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the correct exception was thrown with the expected message.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}