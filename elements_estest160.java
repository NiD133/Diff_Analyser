package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the Elements class.
 * The original test class name is preserved as per the instructions.
 * A more conventional name would be ElementsTest.
 */
public class Elements_ESTestTest160 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that the toString() method on an Elements collection returns the
     * combined outer HTML of all elements it contains.
     *
     * This specific test checks the case where the collection contains just the
     * single root <html> element of a default document.
     */
    @Test
    public void toStringReturnsOuterHtmlOfContainedElements() {
        // Arrange: Create a default document structure (<html><head></head><body></body></html>)
        // and select its single root element, <html>.
        Document doc = Parser.parse("", "");
        Elements elements = doc.children(); // This will contain only the <html> element

        // The expected output is the outer HTML of the <html> element,
        // formatted by Jsoup's default pretty-printer.
        String expectedHtml = "<html>\n <head></head>\n <body></body>\n</html>";

        // Act: Call the method under test.
        String actualHtml = elements.toString();

        // Assert: The result should match the element's full outer HTML.
        assertEquals(expectedHtml, actualHtml);
    }
}