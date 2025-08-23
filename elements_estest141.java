package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This test verifies the fluent API of the Elements class.
 */
public class ElementsFluentApiTest {

    /**
     * Verifies that removeAttr() returns the same Elements instance to allow for method chaining.
     */
    @Test
    public void removeAttrShouldReturnSameInstanceForChaining() {
        // Arrange: Create a document and select some elements.
        Document doc = new Document("http://example.com");
        Elements elements = doc.getAllElements(); // Contains <html>, <head>, <body>

        // Act: Call the method under test.
        Elements returnedElements = elements.removeAttr("class");

        // Assert: The returned object should be the exact same instance as the original.
        assertSame(
            "The removeAttr method should return the same Elements instance for a fluent API.",
            elements,
            returnedElements
        );
    }
}