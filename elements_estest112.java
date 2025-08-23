package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * This is an improved version of a test case for the {@link Elements#is(String)} method.
 * The original test, likely auto-generated, was functionally correct but used unclear
 * names and a non-obvious setup. This version focuses on understandability and maintainability.
 */
public class ElementsIsTest {

    /**
     * Verifies that `is("*")` returns true for a non-empty Elements collection.
     * The universal selector "*" should match any element present in the collection.
     */
    @Test
    public void isWithUniversalSelectorReturnsTrueForNonEmptyCollection() {
        // Arrange: Create a document containing several elements.
        // A standard HTML snippet is more readable than the original `Parser.parseBodyFragment("*", "*")`.
        Document doc = Parser.parseBodyFragment("<div><p>Test</p></div>", "");
        Elements elements = doc.getAllElements(); // Contains <html>, <head>, <body>, <div>, <p>

        // Act: Check if any element in the collection matches the universal selector.
        boolean anyElementMatches = elements.is("*");

        // Assert: The result must be true because the collection is not empty,
        // and the universal selector matches every element.
        assertTrue("is('*') should return true when the Elements collection is not empty.", anyElementMatches);
    }
}