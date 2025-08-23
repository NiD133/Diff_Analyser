package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTestTest53 {

    @Test
    void expectFirstShouldThrowExceptionWhenNoElementMatches() {
        // Arrange: Create a document with <p> elements but no <span> elements.
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Elements elements = doc.children(); // Contains the three <p> elements
        String nonMatchingQuery = "span";

        // Act & Assert: Verify that calling expectFirst() with a non-matching query
        // throws an IllegalArgumentException.
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> elements.expectFirst(nonMatchingQuery),
            "Expected expectFirst() to throw an exception for a non-matching query, but it did not."
        );

        // Assert on the exception's message for more specific verification.
        assertEquals("No elements matched the query 'span' in the elements.", thrown.getMessage());
    }
}