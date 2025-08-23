package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.w3c.dom.DOMException;

/**
 * Tests for {@link W3CDom.W3CBuilder}.
 */
public class W3CDomBuilderTest {

    /**
     * Verifies that attempting to convert a jsoup Element with an attribute name containing
     * invalid characters (per W3C DOM rules) throws a DOMException.
     * W3C DOM attribute names have character restrictions; for example, they cannot contain '/'.
     */
    @Test(expected = DOMException.class)
    public void traverseElementWithInvalidAttributeNameThrowsDOMException() {
        // Arrange: Create a jsoup element with an attribute name that is invalid in W3C DOM.
        Element jsoupElementWithInvalidAttribute = new Element("test");
        jsoupElementWithInvalidAttribute.attr("invalid/name", "value");

        // The W3CBuilder requires a destination W3C document to build into.
        org.w3c.dom.Document w3cDoc = W3CDom.convert(new Document(""));
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        // Act: Attempt to traverse the jsoup element. This is expected to fail when the
        // builder tries to create a W3C attribute with the invalid name.
        w3cBuilder.traverse(jsoupElementWithInvalidAttribute);

        // Assert: The @Test(expected) annotation asserts that a DOMException is thrown.
    }
}