package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Elements#text()} method.
 * This class provides a more understandable, refactored version of an auto-generated test.
 */
public class ElementsTextMethodTest {

    /**
     * Tests that calling .text() on a collection of elements, where each element
     * itself contains no text, results in an empty string.
     *
     * <p><b>Refactoring Rationale:</b></p>
     * <p>The original test was auto-generated and had several issues:</p>
     * <ul>
     *     <li><b>Unclear Name:</b> "test156" was not descriptive.</li>
     *     <li><b>Confusing Setup:</b> It created an empty document and selected elements
     *         containing an empty string, which obscurely selected the document's
     *         structural tags (html, head, body).</li>
     *     <li><b>Incorrect Assertion:</b> It asserted the result was "   " (three spaces),
     *         whereas the correct behavior for empty elements is to produce "".</li>
     * </ul>
     * <p>This revised test clarifies the intent with a descriptive name, uses a clear
     * HTML structure for the setup, and asserts the correct, expected behavior.</p>
     */
    @Test
    public void textOfMultipleEmptyElementsShouldBeAnEmptyString() {
        // Arrange: Create a document with several elements that contain no text.
        Document doc = Jsoup.parse("<div><p></p><span></span><b></b></div>");
        Elements elements = doc.select("p, span, b");

        // Sanity check to ensure our setup selected the correct number of elements.
        assertEquals("Should have selected 3 empty elements", 3, elements.size());

        // Act: Get the combined text of the collection of empty elements.
        String combinedText = elements.text();

        // Assert: The result should be an empty string. The Elements.text() method
        // only adds a space separator if the accumulated text from previous elements
        // is not empty. Since all elements are textless, no text or separators are added.
        assertEquals("Combined text of empty elements should be empty", "", combinedText);
    }
}