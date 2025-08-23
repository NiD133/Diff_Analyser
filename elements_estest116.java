package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    @Test
    public void removeShouldReturnSameInstanceForChaining() {
        // Arrange: Create a document and select a collection of elements.
        Document doc = Parser.parseBodyFragment("<div><p>One</p><p>Two</p></div>", "");
        Elements elements = doc.select("p");

        // Act: Call the remove() method on the collection.
        Elements returnedElements = elements.remove();

        // Assert: The method should return the same Elements instance to allow for method chaining.
        assertSame("The remove() method is expected to return the same instance.", elements, returnedElements);
    }
}