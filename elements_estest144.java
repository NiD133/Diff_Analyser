package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for attribute-related methods in the {@link Elements} class.
 *
 * Note: The original test class name `Elements_ESTestTest144` and method name `test143`
 * suggest they were auto-generated. They have been renamed for clarity and maintainability.
 */
public class ElementsAttributeTest {

    @Test
    public void hasAttrShouldReturnTrueAfterSettingAttributeWithEmptyKey() {
        // Arrange: Create a document with a standard structure (html, head, body).
        Document doc = Document.createShell("");
        Elements elements = doc.getAllElements();
        String emptyAttributeKey = "";
        String anyValue = "some-value";

        // Act: Set an attribute with an empty string as its key on all elements.
        elements.attr(emptyAttributeKey, anyValue);

        // Assert: Verify that the Elements collection correctly reports the presence of the attribute.
        boolean hasAttribute = elements.hasAttr(emptyAttributeKey);
        assertTrue("hasAttr should return true for an empty attribute key that was just set.", hasAttribute);
    }
}