package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link Elements} class, focusing on sibling traversal methods.
 */
public class ElementsTest {

    /**
     * Verifies that calling {@link Elements#next(String)} with a CSS query
     * returns an empty collection if the immediate next sibling element does not match the query.
     */
    @Test
    public void nextWithQueryShouldReturnEmptyWhenNoSiblingMatches() {
        // Arrange: Create a simple HTML structure with two adjacent sibling elements, <p> and <span>.
        String html = "<div><p>First sibling</p><span>Second sibling</span></div>";
        Document doc = Jsoup.parse(html);

        // Select the <p> element. Its direct next sibling is the <span>.
        Elements pElement = doc.select("p");

        // Act: Attempt to find the next sibling using a query ("b") that does not match the <span> tag.
        Elements result = pElement.next("b");

        // Assert: The method should return an empty collection because no matching sibling was found.
        assertTrue(
            "The resulting collection should be empty as the next sibling does not match the query.",
            result.isEmpty()
        );
    }
}