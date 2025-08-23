package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for attribute manipulation methods in {@link Elements}.
 */
@DisplayName("Elements attribute methods")
public class ElementsAttributeTest {

    private static final String HTML = "<p title=foo><p title=bar><p class=foo><p class=bar>";

    @Test
    @DisplayName("attr() gets value from first element and hasAttr() checks for presence")
    void attr_getsValueFromFirstElementAndHasAttrChecksPresence() {
        // Arrange
        Document doc = Jsoup.parse(HTML);
        Elements elementsWithTitle = doc.select("p[title]");

        // Assert
        assertEquals(2, elementsWithTitle.size(), "Should find two elements with 'title' attribute.");
        
        // hasAttr() checks if *any* of the selected elements have the attribute.
        assertTrue(elementsWithTitle.hasAttr("title"), "hasAttr('title') should be true.");
        assertFalse(elementsWithTitle.hasAttr("class"), "hasAttr('class') should be false as no selected elements have it.");
        
        // attr() gets the attribute value from the *first* element in the collection.
        assertEquals("foo", elementsWithTitle.attr("title"), "attr('title') should return the value from the first element.");
    }

    @Test
    @DisplayName("removeAttr() modifies the DOM but not the original Elements collection")
    void removeAttr_modifiesDocumentButNotOriginalElementsCollection() {
        // Arrange
        Document doc = Jsoup.parse(HTML);
        Elements elementsWithTitle = doc.select("p[title]");
        assertEquals(2, elementsWithTitle.size());

        // Act
        elementsWithTitle.removeAttr("title");

        // Assert
        // The original Elements collection is a static list and is not re-evaluated after modification.
        assertEquals(2, elementsWithTitle.size(), "The original Elements collection size should remain unchanged.");
        
        // However, the underlying document is modified. A new selection reflects this change.
        Elements updatedSelection = doc.select("p[title]");
        assertTrue(updatedSelection.isEmpty(), "A new selection for '[title]' should be empty after removal.");
    }

    @Test
    @DisplayName("attr(key, value) sets an attribute on all selected elements")
    void attr_setsAttributeOnAllSelectedElements() {
        // Arrange
        Document doc = Jsoup.parse(HTML);

        // Act: Select all <p> elements and set the 'style' attribute on them.
        Elements allParagraphs = doc.select("p").attr("style", "classy");

        // Assert
        assertEquals(4, allParagraphs.size(), "The method should return all 4 paragraph elements.");
        
        // Verify the attribute was set on all elements.
        for (Element p : allParagraphs) {
            assertEquals("classy", p.attr("style"));
        }
        
        // Verify original attributes are preserved by checking the last element.
        Element lastParagraph = allParagraphs.last();
        assertNotNull(lastParagraph);
        assertEquals("classy", lastParagraph.attr("style"), "The new 'style' attribute should be present on the last element.");
        assertEquals("bar", lastParagraph.attr("class"), "The original 'class' attribute should be preserved on the last element.");
    }
}