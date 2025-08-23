package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * This test class is a refactored version of an auto-generated test.
 * In a real-world project, this test case would be integrated into a comprehensive
 * test suite for the Elements class, such as `ElementsTest.java`.
 */
public class Elements_ESTestTest113 {

    /**
     * Verifies that the is() method returns false when none of the elements
     * in the collection match the given CSS selector.
     */
    @Test
    public void isShouldReturnFalseWhenNoElementsMatchSelector() {
        // Arrange
        // Parsing this fragment creates a document with <html>, <head>, and <body> elements.
        // The string "Bb,Y6" is treated as a simple text node inside the body.
        Document doc = Parser.parseBodyFragment("Bb,Y6", "");
        Elements elements = doc.getAllElements(); // This collection contains [<html>, <head>, <body>]

        // The CSS selector looks for elements with a tag name of either "Bb" or "Y6".
        String nonMatchingSelector = "Bb,Y6";

        // Act
        // Check if any element in the collection matches the selector.
        boolean matches = elements.is(nonMatchingSelector);

        // Assert
        // Since none of the actual elements (html, head, body) match the selector,
        // the result must be false.
        assertFalse("Expected is() to return false for a non-matching selector.", matches);
    }
}