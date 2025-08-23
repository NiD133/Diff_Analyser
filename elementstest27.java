package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the {@link Elements#not(String)} method.
 */
@DisplayName("Elements.not()")
class ElementsNotTest {

    private Document doc;

    @BeforeEach
    void setUp() {
        String html = "<div id=1><p>One</p></div> <div id=2><p><span>Two</span></p></div>";
        doc = Jsoup.parse(html);
    }

    @Test
    @DisplayName("should remove elements matching a complex pseudo-selector")
    void shouldRemoveElementsByPseudoSelector() {
        // Arrange: Start with all <div> elements.
        Elements allDivs = doc.select("div");
        assertEquals(2, allDivs.size(), "Pre-condition: Should find two divs initially.");

        // Act: Filter out divs that have a <span> as a descendant of a <p>.
        Elements filteredDivs = allDivs.not(":has(p > span)");

        // Assert: Only the first div should remain.
        assertEquals(1, filteredDivs.size(), "Should be one div remaining after filtering.");
        
        Element remainingDiv = filteredDivs.first();
        assertNotNull(remainingDiv);
        assertEquals("1", remainingDiv.id(), "The remaining div should be the one with id '1'.");
    }

    @Test
    @DisplayName("should remove elements matching a simple ID selector")
    void shouldRemoveElementsByIdSelector() {
        // Arrange: Start with all <div> elements.
        Elements allDivs = doc.select("div");
        assertEquals(2, allDivs.size(), "Pre-condition: Should find two divs initially.");

        // Act: Filter out the div with the ID '1'.
        Elements filteredDivs = allDivs.not("#1");

        // Assert: Only the second div should remain.
        assertEquals(1, filteredDivs.size(), "Should be one div remaining after filtering.");

        Element remainingDiv = filteredDivs.first();
        assertNotNull(remainingDiv);
        assertEquals("2", remainingDiv.id(), "The remaining div should be the one with id '2'.");
    }
}