package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// The original class name `ElementsTestTest28` was redundant and uninformative.
// Renaming it to `ElementsTest` aligns with standard testing conventions.
public class ElementsTest {

    @Test
    // The original method name `tagNameSet` was ambiguous. This new name,
    // `tagNameShouldChangeTagForAllSelectedElements`, clearly describes the
    // behavior under test, following a "should" convention for test naming.
    public void tagNameShouldChangeTagForAllSelectedElements() {
        // Arrange: Set up the initial state and expected outcome.
        String originalHtml = "<p>Hello <i>there</i> <i>now</i></p>";
        Document doc = Jsoup.parse(originalHtml);
        String expectedHtml = "<p>Hello <em>there</em> <em>now</em></p>";

        // Act: Perform the action being tested.
        // Select all 'i' elements and change their tag name to 'em'.
        doc.select("i").tagName("em");

        // Assert: Verify the action produced the expected outcome.
        String actualHtml = doc.body().html();
        assertEquals(expectedHtml, actualHtml);
    }
}