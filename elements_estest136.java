package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link Elements#hasClass(String)} method.
 */
public class Elements_ESTestTest136 {

    @Test
    public void hasClassShouldReturnFalseWhenNoElementHasTheClass() {
        // Arrange: Create a document and select a collection of elements,
        // none of which have the class we're looking for.
        Document doc = Jsoup.parse("<div></div><p>Some text</p><span></span>");
        Elements elements = doc.getAllElements(); // Contains <html>, <head>, <body>, <div>, <p>, <span>
        String classNameToFind = "non-existent-class";

        // Act: Check if any element in the collection has the specified class.
        boolean result = elements.hasClass(classNameToFind);

        // Assert: The result should be false, as no element has the class.
        assertFalse("Expected hasClass to be false when no element has the class", result);
    }
}