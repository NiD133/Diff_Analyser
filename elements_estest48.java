package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements#before(String)} method.
 */
public class Elements_ESTestTest48 {

    /**
     * Verifies that the {@code before(String)} method correctly inserts the specified HTML
     * before a selected element and returns the original {@code Elements} instance for chaining.
     */
    @Test
    public void before_shouldInsertHtmlBeforeElementAndReturnSameInstance() {
        // Arrange: Set up a simple document and select the target element.
        Document doc = Parser.parse("<div><p>Test Paragraph</p></div>", "");
        Elements paragraphToPrecede = doc.select("p");
        String htmlToInsert = "<span>Inserted Span</span>";

        // Pre-condition check: Ensure the initial state is as expected.
        assertEquals("Initially, there should be one element in the collection.", 1, paragraphToPrecede.size());

        // Act: Insert the HTML string before the selected element.
        Elements returnedElements = paragraphToPrecede.before(htmlToInsert);

        // Assert: Verify the state after the operation.

        // 1. The method should return the same Elements instance to allow for method chaining.
        assertSame("The returned object should be the same instance as the original.", paragraphToPrecede, returnedElements);

        // 2. The DOM should be modified correctly.
        Element insertedElement = paragraphToPrecede.first().previousElementSibling();
        assertNotNull("An element should have been inserted before the paragraph.", insertedElement);
        assertEquals("The inserted element should have the correct tag name.", "span", insertedElement.tagName());
        assertEquals("The inserted element should contain the correct text.", "Inserted Span", insertedElement.text());

        // 3. The overall HTML of the parent should reflect the change.
        String expectedHtml = "<span>Inserted Span</span><p>Test Paragraph</p>";
        assertEquals("The parent's HTML should show the new element before the original paragraph.",
                expectedHtml, doc.select("div").html().trim());
    }
}