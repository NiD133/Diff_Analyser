package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that the unwrap() method correctly removes an element from the DOM,
     * promotes its children, and leaves the original Elements collection intact.
     */
    @Test
    public void unwrapRemovesElementFromDomButKeepsItInTheCollection() {
        // Arrange: Create a simple DOM structure where a <p> tag can be unwrapped.
        // The structure is <div><p>Hello</p></div>
        Document doc = Jsoup.parse("<div><p>Hello</p></div>");
        Element div = doc.selectFirst("div");
        Elements paragraphs = doc.select("p");

        // Sanity check the initial state.
        assertEquals("Initial state should have one <p> element.", 1, paragraphs.size());
        assertEquals("The <div> should be the parent of the <p>.", div, paragraphs.first().parent());

        // Act: Unwrap the <p> element.
        Elements result = paragraphs.unwrap();

        // Assert:

        // 1. The method returns the same Elements instance for method chaining.
        assertSame("unwrap() should return the same Elements instance.", paragraphs, result);

        // 2. The collection itself is not modified and still contains the element.
        assertEquals("The collection should still contain one element.", 1, result.size());
        Element unwrappedParagraph = result.first();
        assertNotNull(unwrappedParagraph);

        // 3. The element has been removed from the DOM (it has no parent).
        assertNull("The unwrapped element should be removed from its parent.", unwrappedParagraph.parent());

        // 4. The element's children (the text "Hello") have been moved up to its original parent (the <div>).
        assertEquals("The parent <div> should now contain the unwrapped text.", "Hello", div.text());
        assertTrue("The <div>'s first child should now be a TextNode.", div.childNode(0) instanceof TextNode);
        assertEquals("The <div> should no longer have any element children.", 0, div.children().size());
    }
}