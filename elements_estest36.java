package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

// The original class name is kept, but in a real-world scenario, 
// it would be renamed to something more descriptive, like "ElementsHtmlMethodTest".
public class Elements_ESTestTest36 {

    /**
     * Tests that the html() method correctly concatenates the inner HTML of each element in the collection,
     * separated by newlines.
     */
    @Test
    public void htmlShouldReturnCombinedInnerHtmlOfAllElements() {
        // Arrange: Create a document with multiple elements to select.
        String html = "<div>" +
                      "  <p>First paragraph</p>" +
                      "  <p>Second paragraph</p>" +
                      "  <p><b>Third</b> paragraph</p>" +
                      "</div>";
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act: Get the combined inner HTML of the selected paragraphs.
        String combinedHtml = paragraphs.html();

        // Assert: The result should be the inner HTML of each element, joined by a newline.
        String expectedHtml = "First paragraph\n" +
                              "Second paragraph\n" +
                              "<b>Third</b> paragraph";
        assertEquals(expectedHtml, combinedHtml);
    }
}