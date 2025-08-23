package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements} class, focusing on exception-throwing scenarios.
 */
public class ElementsTest {

    /**
     * Verifies that expectFirst() throws an IllegalArgumentException when the CSS query
     * does not match any element in the collection.
     */
    @Test
    public void expectFirstThrowsExceptionWhenNoElementMatches() {
        // Arrange: Create a simple document and select an element.
        Document doc = Jsoup.parse("<div><p>Some text</p></div>");
        Elements elements = doc.select("div"); // The collection contains one <div> element.
        String nonExistentSelector = "span";    // This selector will not match anything within the div.

        // Act & Assert: Attempt to find a non-existent element and verify the exception.
        try {
            elements.expectFirst(nonExistentSelector);
            fail("An IllegalArgumentException should have been thrown, as no element matches the selector.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is helpful and accurate.
            String expectedMessage = "No elements matched the query 'span' in the elements.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}