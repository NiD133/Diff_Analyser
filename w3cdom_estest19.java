package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.DOMException;

/**
 * Test suite for the {@link W3CDom} class, focusing on conversion from Jsoup to W3C DOM.
 */
public class W3CDomTest {

    /**
     * Verifies that converting a Jsoup document with an invalid XML element tag name
     * to a W3C Document throws a {@link DOMException}.
     * <p>
     * Jsoup's parser is more lenient and can create elements with tag names that are
     * not compliant with the stricter W3C DOM specification. This test ensures that
     * such non-compliance is correctly reported during the conversion process.
     */
    @Test(expected = DOMException.class)
    public void fromJsoupThrowsExceptionForInvalidTagName() {
        // Arrange: Create a Jsoup document and add an element with a tag name
        // containing characters that are illegal in W3C DOM element names.
        W3CDom w3cDomConverter = new W3CDom();
        Document jsoupDocument = Document.createShell("");

        // The tag name "::/~/%3V}" contains multiple invalid characters (:, /, ~, %, etc.).
        String invalidTagName = "::/~/%3V}";
        jsoupDocument.prependElement(invalidTagName);

        // Act: Attempt to convert the Jsoup document to a W3C DOM.
        // This action is expected to throw a DOMException due to the invalid tag name.
        w3cDomConverter.fromJsoup(jsoupDocument);

        // Assert: The test is successful if a DOMException is thrown, which is
        // handled by the @Test(expected) annotation.
    }
}