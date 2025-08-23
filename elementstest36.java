package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for sibling-finding methods in the {@link Elements} class.
 * This suite verifies the behavior of next(), prev(), nextAll(), and prevAll() methods,
 * both with and without filters.
 */
class ElementsSiblingsTest {

    private Elements selectedParagraphs;

    /**
     * The HTML structure for all tests is two divs, each containing six paragraphs.
     * The initial selection for each test targets the 4th paragraph in each div ("p4" and "p10").
     * <pre>
     * {@code
     * <div>
     *   <p>1</p> <p>2</p> <p>3</p>
     *   <p>4</p> <!-- selected -->
     *   <p>5</p> <p>6</p>
     * </div>
     * <div>
     *   <p>7</p> <p>8</p> <p>9</p>
     *   <p>10</p> <!-- selected -->
     *   <p>11</p> <p>12</p>
     * </div>
     * }
     * </pre>
     */
    @BeforeEach
    void setUp() {
        String html = "<div><p>1</p><p>2</p><p>3</p><p>4</p><p>5</p><p>6</p></div>" +
                      "<div><p>7</p><p>8</p><p>9</p><p>10</p><p>11</p><p>12</p></div>";
        Document doc = Jsoup.parse(html);
        // The selector "p:eq(3)" gets the 4th <p> sibling within each parent (div).
        // This selects the paragraphs containing "4" and "10".
        selectedParagraphs = doc.select("p:eq(3)");
        assertEquals(2, selectedParagraphs.size(), "Initial selection should contain two elements.");
    }

    @Nested
    @DisplayName("next() methods")
    class NextTests {
        @Test
        void next_shouldReturnImmediateFollowingSiblings() {
            // Act
            Elements nextSiblings = selectedParagraphs.next();

            // Assert
            assertEquals(2, nextSiblings.size(), "Should find a next sibling for each selected element.");
            assertEquals("5", nextSiblings.first().text(), "Next sibling of '4' should be '5'.");
            assertEquals("11", nextSiblings.last().text(), "Next sibling of '10' should be '11'.");
        }

        @Test
        void nextWithFilter_shouldReturnOnlyMatchingSibling() {
            // Act
            Elements nextFiltered = selectedParagraphs.next("p:contains(5)");

            // Assert
            assertEquals(1, nextFiltered.size(), "Should find only the sibling that matches the filter.");
            assertEquals("5", nextFiltered.first().text(), "The found sibling should be '5'.");
        }

        @Test
        void nextWithFilter_shouldReturnEmptyForNonMatchingSibling() {
            // Act
            Elements nextFiltered = selectedParagraphs.next("p:contains(6)");

            // Assert
            assertTrue(nextFiltered.isEmpty(), "Should be empty if the immediate next sibling doesn't match.");
        }

        @Test
        void nextAll_shouldReturnAllFollowingSiblings() {
            // Act
            Elements allNextSiblings = selectedParagraphs.nextAll();

            // Assert
            List<String> expectedTexts = List.of("5", "6", "11", "12");
            assertEquals(expectedTexts, allNextSiblings.eachText());
        }

        @Test
        void nextAllWithFilter_shouldReturnAllMatchingFollowingSiblings() {
            // Act
            Elements nextAllFiltered = selectedParagraphs.nextAll("p:contains(6)");

            // Assert
            assertEquals(1, nextAllFiltered.size());
            assertEquals("6", nextAllFiltered.first().text());
        }
    }

    @Nested
    @DisplayName("prev() methods")
    class PrevTests {
        @Test
        void prev_shouldReturnImmediatePrecedingSiblings() {
            // Act
            Elements prevSiblings = selectedParagraphs.prev();

            // Assert
            assertEquals(2, prevSiblings.size(), "Should find a previous sibling for each selected element.");
            assertEquals("3", prevSiblings.first().text(), "Previous sibling of '4' should be '3'.");
            assertEquals("9", prevSiblings.last().text(), "Previous sibling of '10' should be '9'.");
        }

        @Test
        void prevWithFilter_shouldReturnOnlyMatchingSibling() {
            // Act
            Elements prevFiltered = selectedParagraphs.prev("p:contains(3)");

            // Assert
            assertEquals(1, prevFiltered.size(), "Should find only the sibling that matches the filter.");
            assertEquals("3", prevFiltered.first().text(), "The found sibling should be '3'.");
        }

        @Test
        void prevWithFilter_shouldReturnEmptyForNonMatchingSibling() {
            // Act
            Elements prevFiltered = selectedParagraphs.prev("p:contains(1)");

            // Assert
            assertTrue(prevFiltered.isEmpty(), "Should be empty if the immediate previous sibling doesn't match.");
        }

        @Test
        void prevAll_shouldReturnAllPrecedingSiblings() {
            // Act
            Elements allPrevSiblings = selectedParagraphs.prevAll();

            // Assert
            // The order is from the element outwards (closest first).
            List<String> expectedTexts = List.of("3", "2", "1", "9", "8", "7");
            assertEquals(expectedTexts, allPrevSiblings.eachText());
        }

        @Test
        void prevAllWithFilter_shouldReturnAllMatchingPrecedingSiblings() {
            // Act
            Elements prevAllFiltered = selectedParagraphs.prevAll("p:contains(1)");

            // Assert
            assertEquals(1, prevAllFiltered.size());
            assertEquals("1", prevAllFiltered.first().text());
        }
    }
}