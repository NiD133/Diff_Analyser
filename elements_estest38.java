package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the behavior of the Elements collection, particularly how it interacts
 * with a changing DOM.
 */
public class ElementsDomInteractionTest {

    /**
     * Verifies that an Element reference obtained from an Elements collection
     * reflects subsequent changes to the DOM's structure. This demonstrates
     * that Elements collections hold live references to the DOM nodes.
     */
    @Test
    public void elementReferenceRemainsLiveAfterDomModification() {
        // Arrange: Create a document, select a group of elements, and then
        // find their preceding siblings.
        String html = "<body>" +
                      "  <p>First Paragraph</p>" +
                      "  <h1>First Header</h1>" +
                      "  <h2>Second Header</h2>" +
                      "</body>";
        Document doc = Jsoup.parse(html);

        Elements headers = doc.select("h1, h2"); // Contains <h1> and <h2>
        Elements prevElements = headers.prev();  // Contains <p> (from h1) and <h1> (from h2)

        // Get a direct reference to the <p> element, which is the first in the 'prevElements' list.
        Element pElement = prevElements.first();
        
        // Sanity-check our initial assumptions before modification.
        assertEquals("p", pElement.tagName());
        assertEquals(2, prevElements.size());
        // The <p> element is the first child of <body>, so its sibling index is 0.
        assertEquals(0, pElement.siblingIndex());

        // Act: Modify the DOM by prepending a new element to the body.
        // This action will shift the position of all existing children.
        doc.body().prepend("<div>A New First Element</div>");

        // Assert: The pElement reference, obtained before the modification,
        // should now reflect the new state of the DOM.
        
        // The collection size should remain unchanged.
        assertEquals(2, prevElements.size());

        // The sibling index of our referenced <p> element should now be 1,
        // as the new <div> has been inserted at index 0.
        assertEquals(1, pElement.siblingIndex());
    }
}