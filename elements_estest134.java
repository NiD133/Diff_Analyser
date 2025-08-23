package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    @Test
    public void valShouldReturnEmptyStringWhenFirstElementIsNotAFormElement() {
        // Arrange
        // Parsing simple text creates a document with <html>, <head>, and <body> tags.
        // The val() method on an Elements collection operates on the first element.
        Document doc = Parser.parse("A simple text document", "");
        Elements elements = doc.getAllElements(); // First element will be <html>

        // Act
        // Since <html> is not a form element, calling val() should return an empty string.
        String value = elements.val();

        // Assert
        assertEquals("val() should be empty for non-form elements", "", value);
    }
}