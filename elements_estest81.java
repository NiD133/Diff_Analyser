package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test suite for exception handling in the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling the html(String) method with a null argument
     * correctly throws an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void htmlWithNullArgumentThrowsException() {
        // Arrange: Create a collection of elements from simple, valid HTML.
        Document doc = Parser.parse("<div><p>One</p><p>Two</p></div>");
        Elements elements = doc.select("p");

        // Act: Attempt to set the inner HTML of the elements to null.
        // The test expects this call to throw an exception.
        elements.html(null);
    }
}