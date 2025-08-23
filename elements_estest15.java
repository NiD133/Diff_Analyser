package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class, focusing on the select method.
 */
public class ElementsSelectTest {

    /**
     * Verifies that calling .select() on an Elements object with a query that matches nothing
     * returns an empty Elements collection, rather than null or throwing an error.
     */
    @Test
    public void selectWithNonMatchingQueryReturnsEmptyElements() {
        // Arrange: Create a basic document and get a collection of its elements.
        Document doc = Document.createShell(""); // Creates <html><head></head><body></body></html>
        Elements elements = doc.getAllElements();
        String nonExistentSelector = ".non-existent-class";

        // Act: Perform a select operation with a CSS selector that is guaranteed not to match.
        Elements result = elements.select(nonExistentSelector);

        // Assert: The resulting collection should be empty but not null.
        assertNotNull("The select method should not return null for a non-matching query.", result);
        assertTrue(
            "Expected an empty Elements collection for a non-matching selector, but it was not.",
            result.isEmpty()
        );
    }
}