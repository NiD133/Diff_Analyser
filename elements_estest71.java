package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements#removeAttr(String)} method.
 */
public class ElementsRemoveAttrTest {

    @Test
    public void removeAttrShouldThrowIllegalArgumentExceptionForNullKey() {
        // Arrange: Create an Elements object. The specific elements don't matter,
        // as the validation happens before any element is processed.
        Document doc = new Document("");
        Elements elements = doc.getAllElements();

        // Act & Assert: Verify that calling removeAttr with a null key throws an exception.
        try {
            elements.removeAttr(null);
            fail("Expected an IllegalArgumentException to be thrown for a null attribute key, but it was not.");
        } catch (IllegalArgumentException e) {
            // This is the expected behavior.
            // Verify the exception message to ensure it's thrown for the right reason.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}