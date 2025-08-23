package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElementsTestTest46 {

    @Test
    void iteratorRemove_shouldModifyBothCollectionAndDom() {
        // This test verifies a key contract of the Elements collection:
        // modifications made via its iterator (e.g., iterator.remove())
        // are reflected not only in the collection but also in the underlying DOM.

        // Arrange: Create a document with four <p> elements.
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p><p>Four</p>");
        Elements pElements = doc.select("p");
        assertEquals(4, pElements.size(), "Verify initial count of paragraphs");

        // Act: Remove the element containing "Two" using the iterator.
        for (Iterator<Element> it = pElements.iterator(); it.hasNext(); ) {
            Element p = it.next();
            if (p.text().equals("Two")) {
                it.remove(); // This should modify both the `pElements` list and the `doc`.
            }
        }

        // Assert: Check that both the collection and the DOM were updated correctly.
        // 1. The live collection reflects the removal.
        assertEquals(3, pElements.size(), "Collection size should be updated after removal");

        // 2. The DOM is also modified. We verify this by re-selecting from the document.
        Elements remainingElements = doc.select("p");
        assertEquals(3, remainingElements.size(), "DOM should contain fewer elements");
        assertEquals("One Three Four", remainingElements.text(), "Text of remaining elements should be correct");
    }
}