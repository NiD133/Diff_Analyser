package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.w3c.dom.DOMException;

/**
 * Tests the behavior of the {@link W3CDom} helper class when converting jsoup
 * documents that contain elements with invalid tag names for the W3C DOM standard.
 */
public class W3CDomTest {

    /**
     * Verifies that converting a jsoup Document to a W3C Document throws a
     * {@link DOMException} if it contains an element with an invalid tag name.
     * <p>
     * According to the W3C DOM specification, element tag names must be valid
     * qualified names, which do not permit multiple colons. Jsoup's parser is more
     * lenient and can create such elements, but they cannot be represented in a
     * standard W3C DOM.
     * </p>
     */
    @Test(expected = DOMException.class)
    public void fromJsoupThrowsExceptionOnInvalidTagName() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        
        // This HTML fragment creates an element with a tag name containing multiple colons,
        // which is invalid in a strict W3C DOM context.
        String htmlWithInvalidTagName = "<javax.xml.xpath.XPathFactory:jsoup>";
        Document jsoupDocument = Parser.parseBodyFragment(htmlWithInvalidTagName, "");

        // Act: Attempt to convert the jsoup document. This is expected to fail when
        // the W3C builder encounters the invalid tag name.
        w3cDom.fromJsoup(jsoupDocument);

        // Assert: The test expects a DOMException, as declared in the @Test annotation.
    }
}