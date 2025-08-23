package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Elements} class.
 */
// Renamed from ElementsTestTest17 for clarity and standard convention.
public class ElementsTest {

    @Test
    void wrapShouldEncloseEachSelectedElement() {
        // Arrange: Set up the initial HTML document.
        String initialHtml = "<p><b>This</b> is <b>jsoup</b></p>";
        Document doc = Jsoup.parse(initialHtml);
        String expectedHtml = "<p><i><b>This</b></i> is <i><b>jsoup</b></i></p>";

        // Act: Select all <b> elements and wrap them with an <i> tag.
        doc.select("b").wrap("<i></i>");

        // Assert: Verify the document's body HTML matches the expected result.
        String actualHtml = doc.body().html();
        assertEquals(expectedHtml, actualHtml);
    }
}