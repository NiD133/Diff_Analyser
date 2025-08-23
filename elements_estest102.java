package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for collection-modification methods of the {@link Elements} class.
 */
public class ElementsModificationTest {

    /**
     * Verifies that calling retainAll() with an empty collection correctly removes all
     * elements from the list and returns true to indicate that the list was modified.
     */
    @Test
    public void retainAllWithEmptyCollectionRemovesAllElements() {
        // Arrange: Create a document and select a non-empty list of elements.
        Document doc = Parser.parse("<div><p>One</p><span>Two</span></div>");
        Elements elements = doc.select("*"); // Selects all elements: <html>, <head>, <body>, <div>, <p>, <span>

        // Pre-condition check to ensure the test setup is valid.
        assertFalse("The initial list of elements should not be empty.", elements.isEmpty());

        // Act: Attempt to retain only the elements present in an empty collection.
        boolean wasModified = elements.retainAll(Collections.emptyList());

        // Assert: The list should now be empty, and the method should report a modification.
        assertTrue("The elements list should be empty after the retainAll operation.", elements.isEmpty());
        assertTrue("The retainAll method should return true, as the list was modified.", wasModified);
    }
}