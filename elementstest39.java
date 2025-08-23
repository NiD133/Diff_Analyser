package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Test suite for {@link Elements#set(int, Element)}.
 */
public class ElementsTest {

    @Test
    public void setReplacesElementInListAndDom() {
        // Arrange
        // Jsoup automatically closes the <p> tags, resulting in three sibling paragraphs.
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three");
        Elements paragraphs = doc.select("p");
        Element paragraphToReplace = paragraphs.get(1); // The "<p>Two</p>" element

        Element newParagraph = doc.createElement("p").text("New").attr("id", "new");

        // Act
        Element replacedElement = paragraphs.set(1, newParagraph);

        // Assert
        // 1. Verify the method returns the element that was replaced.
        assertSame(paragraphToReplace, replacedElement,
            "The set() method should return the original element at the specified index.");

        // 2. Verify the Elements collection is updated.
        assertSame(newParagraph, paragraphs.get(1),
            "The element at the specified index in the list should be the new element.");
        assertEquals(3, paragraphs.size(),
            "The size of the Elements collection should remain unchanged.");

        // 3. Verify the underlying DOM is also updated.
        String expectedHtml = String.join("\n",
            "<p>One</p>",
            "<p id=\"new\">New</p>",
            "<p>Three</p>"
        );
        assertEquals(expectedHtml, doc.body().html(),
            "The DOM structure should reflect the replacement.");
    }
}