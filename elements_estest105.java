package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * This test class focuses on the behavior of the Elements.forms() method.
 */
public class Elements_ESTestTest105_Refactored { // Renamed for clarity

    /**
     * Verifies that the forms() method returns an empty list when called on a collection
     * of elements that do not contain any <form> tags.
     */
    @Test
    public void formsShouldReturnEmptyListWhenNoFormElementsArePresent() {
        // Arrange: Create a document with a basic structure but no <form> elements.
        // Parsing an empty body fragment results in <html><head></head><body></body></html>.
        Document doc = Parser.parseBodyFragment("", "");

        // Select all elements in the document. None of these are or contain a <form>.
        Elements elements = doc.getAllElements();

        // Act: Attempt to retrieve form elements from the collection.
        List<FormElement> forms = elements.forms();

        // Assert: The resulting list of forms should be empty.
        assertTrue("The forms list should be empty as no <form> elements exist in the source.", forms.isEmpty());
    }
}