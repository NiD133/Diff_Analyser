package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test suite for the {@link Elements} class.
 * This class was improved from an original named ElementsTestTest42.
 */
class ElementsTest {

    @Test
    void removeObjectShouldDoNothingIfObjectIsNotInList() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Elements pElements = doc.select("p");

        String originalHtml = doc.html();
        int originalSize = pElements.size();

        // Create an element that is not attached to the document, so it is not a member of pElements.
        Element nonMemberElement = doc.createElement("p").text("New");
        
        // Create another object of a completely different type to test the remove(Object) method.
        List<Node> listOfNodes = nonMemberElement.childNodes();

        // Act & Assert
        // 1. Attempt to remove an element that is not in the list.
        // This should return false and not modify the list.
        boolean wasElementRemoved = pElements.remove(nonMemberElement);
        assertFalse(wasElementRemoved, "remove(Object) should return false for a non-member element.");

        // 2. Attempt to remove an object of an incompatible type.
        // This should also return false and not modify the list.
        boolean wasListRemoved = pElements.remove(listOfNodes);
        assertFalse(wasListRemoved, "remove(Object) should return false for an object of a different type.");

        // Verify that the original Elements list and the document are unchanged.
        assertEquals(originalSize, pElements.size(), "The size of the Elements list should not change.");
        assertEquals(originalHtml, doc.html(), "The document's HTML should not change.");
    }
}