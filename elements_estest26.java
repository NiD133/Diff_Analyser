package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Elements} class.
 */
public class ElementsTest {

    @Test
    public void outerHtmlReturnsOuterHtmlOfAllElementsSeparatedByNewlines() {
        // Arrange
        // 1. Create a standard document structure. The base URI is not relevant for this test.
        Document doc = Document.createShell("");
        Element html = doc.child(0); // The <html> tag
        Element head = html.child(0); // The <head> tag
        Element body = html.child(1); // The <body> tag

        // 2. Define the expected result.
        // The doc.getAllElements() method returns the document root itself, then its descendants.
        // The Elements.outerHtml() method joins the outer HTML of each element with a newline.
        String expectedHtml = String.join("\n",
            doc.outerHtml(),    // The document's outer HTML (which is its children's HTML)
            html.outerHtml(),   // The <html> element's outer HTML
            head.outerHtml(),   // The <head> element's outer HTML
            body.outerHtml()    // The <body> element's outer HTML
        );

        // Act
        // 3. Get all elements and call the method under test.
        Elements allElements = doc.getAllElements();
        String actualHtml = allElements.outerHtml();

        // Assert
        // 4. Verify that the combined outer HTML matches the expected concatenated string.
        assertEquals(expectedHtml, actualHtml);
    }
}