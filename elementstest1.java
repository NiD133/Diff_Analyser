package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Elements} class, focusing on selection and filtering methods.
 */
// Renamed from ElementsTestTest1 for better clarity and convention.
public class ElementsTest {

    /**
     * Verifies that calling {@code select(query)} on an existing {@code Elements} object
     * correctly filters the selection, finding matching descendants only within those elements,
     * not throughout the entire document.
     */
    @Test
    @DisplayName("select() on an Elements object should find descendants only within those elements")
    void selectOnElementsShouldFilterResults() {
        // Arrange: Create an HTML structure with a mix of elements inside and outside the target containers.
        // The goal is to select <p> tags that are specifically inside <div class="headline">.
        String html = """
            <p>This paragraph should be excluded</p>
            <div class="headline">
                <p>Hello</p>
                <p>There</p>
            </div>
            <div class="headline">
                <h1>This is a headline, but contains no paragraphs</h1>
            </div>
            """;
        Document doc = Jsoup.parse(html);

        // Act: First, select the 'headline' divs, then select the 'p' tags within that result set.
        Elements headlineDivs = doc.select(".headline");
        Elements paragraphsInHeadlines = headlineDivs.select("p");

        // Assert: The result should only contain the two <p> tags from the first headline div.
        assertEquals(2, paragraphsInHeadlines.size(), "Should find two paragraphs inside the headline divs.");

        List<String> actualTexts = paragraphsInHeadlines.eachText();
        List<String> expectedTexts = List.of("Hello", "There");
        assertEquals(expectedTexts, actualTexts, "The text of the found paragraphs should match the expected list.");
    }
}