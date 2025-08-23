package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link Elements#eq(int)} method.
 */
public class ElementsEqTest {

    @Test
    public void eqShouldReturnNewElementsWithElementAtSpecifiedIndex() {
        // Arrange: Create a document with a list of distinct elements.
        Document doc = Jsoup.parse("<div><p>First</p><p>Second</p><p>Third</p></div>");
        Elements paragraphs = doc.select("p"); // Contains three <p> elements

        // Act: Use eq(0) to select only the first element from the list.
        Elements firstParagraphWrapper = paragraphs.eq(0);

        // Assert: Verify the new Elements object contains exactly the first paragraph.
        assertNotNull(firstParagraphWrapper);
        assertEquals("The result should contain exactly one element.", 1, firstParagraphWrapper.size());

        Element firstParagraph = firstParagraphWrapper.first();
        assertNotNull("The contained element should not be null.", firstParagraph);
        assertEquals("The element should be the first paragraph.", "First", firstParagraph.text());
    }

    @Test
    public void eqShouldReturnEmptyElementsWhenIndexIsOutOfBounds() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>First</p><p>Second</p></div>");
        Elements paragraphs = doc.select("p"); // Contains two elements

        // Act: Request an index that is out of bounds.
        Elements result = paragraphs.eq(5);

        // Assert: The method should return an empty Elements object, not throw an exception.
        assertNotNull(result);
        assertEquals("Result should be empty for an out-of-bounds index.", 0, result.size());
    }

    @Test
    public void eqShouldReturnEmptyElementsWhenIndexIsNegative() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>First</p><p>Second</p></div>");
        Elements paragraphs = doc.select("p");

        // Act: Request a negative index.
        Elements result = paragraphs.eq(-1);

        // Assert: The method should return an empty Elements object.
        assertNotNull(result);
        assertEquals("Result should be empty for a negative index.", 0, result.size());
    }
}