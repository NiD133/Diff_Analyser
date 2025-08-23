package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.w3c.dom.DOMException;

/**
 * Test suite for the {@link W3CDom} class.
 */
public class W3CDomTest {

    /**
     * Verifies that converting a Jsoup document to a W3C DOM fails with a DOMException
     * when an element contains an attribute with an invalid XML Qualified Name (QName).
     * <p>
     * The attribute name "xmlns:" is used for this test. It is invalid because it declares
     * a namespace prefix but is missing the required local name part (e.g., "xmlns:prefix").
     * The W3C DOM standard strictly enforces valid QNames for attributes.
     * </p>
     */
    @Test(expected = DOMException.class)
    public void convertJsoupDocumentWithInvalidAttributeNameThrowsDOMException() {
        // Arrange: Create a Jsoup document and add an attribute with an invalid name.
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Parser.parseBodyFragment("<div>Hello</div>", "");

        // The "xmlns:" attribute name is not a valid QName.
        jsoupDoc.attr("xmlns:", "https://jsoup.org/");

        // Act: Attempt to convert the Jsoup document to a W3C DOM.
        // This action is expected to throw a DOMException when it encounters the invalid attribute.
        w3cDom.fromJsoup(jsoupDoc);

        // Assert: The test implicitly asserts that a DOMException is thrown.
        // If no exception is thrown, the test will fail.
    }
}