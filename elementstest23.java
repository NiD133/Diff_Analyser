package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Elements#remove()} method.
 */
// The original class name "ElementsTestTest23" was renamed to "ElementsTest"
// for better clarity and to follow common Java testing conventions.
public class ElementsTest {

    @Test
    void removeShouldDeleteAllSelectedElementsFromDom() {
        // Arrange
        String initialHtml = "<div><p>Hello <b>there</b></p> jsoup <p>now!</p></div>";
        Document doc = Jsoup.parse(initialHtml);
        // Disable pretty-printing to get a compact, predictable HTML string for the assertion.
        doc.outputSettings().prettyPrint(false);

        // Act
        // Select all <p> elements and remove them from the DOM.
        doc.select("p").remove();

        // Assert
        String expectedHtml = "<div> jsoup </div>";
        String actualHtml = doc.body().html();
        assertEquals(expectedHtml, actualHtml, "The <p> elements should be removed, leaving the sibling text node.");
    }
}