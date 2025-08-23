package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * Tests for the {@link Elements#eachText()} method.
 */
public class ElementsTestTest37 {

    private static final String HTML =
        "<div><p>1<p>2<p>3<p>4<p>5<p>6</div>" +
        "<div><p>7<p>8<p>9<p>10<p>11<p>12<p></p></div>";

    @Test
    public void eachTextReturnsCombinedTextOfEachSelectedElement() {
        // Arrange
        Document doc = Jsoup.parse(HTML);

        // Act: Select the two <div> elements and get the text from each.
        List<String> divTexts = doc.select("div").eachText();

        // Assert: The result should be a list containing the combined text of each div.
        List<String> expectedTexts = List.of(
            "1 2 3 4 5 6",
            "7 8 9 10 11 12"
        );
        assertIterableEquals(expectedTexts, divTexts);
    }

    @Test
    public void eachTextExcludesElementsThatHaveNoText() {
        // Arrange
        Document doc = Jsoup.parse(HTML);
        Elements paragraphs = doc.select("p");

        // Sanity-check that the selector finds all <p> tags, including the empty one.
        assertEquals(13, paragraphs.size());

        // Act: Get the text from each <p> element.
        List<String> pTexts = paragraphs.eachText();

        // Assert: The result should contain text from the 12 non-empty paragraphs,
        // and the final empty <p> tag should be excluded.
        List<String> expectedTexts = List.of(
            "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "10", "11", "12"
        );
        assertIterableEquals(expectedTexts, pTexts);
    }
}