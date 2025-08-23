package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the {@link Elements#empty()} method.
 */
public class ElementsTestTest22 {

    @Test
    void emptyRemovesAllChildNodesFromSelectedElements() {
        // Arrange: Set up the initial HTML document.
        String initialHtml = "<div><p>Hello <b>there</b></p> <p>now!</p></div>";
        Document doc = Jsoup.parse(initialHtml);
        
        // Disable pretty-printing to ensure the output HTML is a single line,
        // making the string comparison in the assertion predictable.
        doc.outputSettings().prettyPrint(false);

        // Act: Select all <p> elements and call the empty() method on them.
        doc.select("p").empty();

        // Assert: Verify that all children of the <p> elements have been removed.
        String expectedHtml = "<div><p></p> <p></p></div>";
        String actualHtml = doc.body().html();
        
        assertEquals(expectedHtml, actualHtml, "The empty() method should remove all child nodes from the selected elements.");
    }
}