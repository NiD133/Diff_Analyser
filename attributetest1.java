package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Attribute} class.
 */
// Renamed class from AttributeTestTest1 to follow standard naming conventions.
public class AttributeTest {

    @Test
    // Renamed method to clearly describe the behavior under test:
    // 1. The value is HTML-escaped.
    // 2. The toString() output matches the html() output.
    void htmlShouldBeCorrectlyEscapedAndMatchToString() {
        // Arrange: Create an attribute with a value containing a special character ('&')
        // that requires HTML escaping.
        Attribute attribute = new Attribute("key", "value &");
        String expectedHtml = "key=\"value &amp;\"";

        // Act: Generate the HTML representation of the attribute.
        String actualHtml = attribute.html();
        String actualToString = attribute.toString();

        // Assert:
        // 1. Verify that the special character is correctly escaped.
        assertEquals(expectedHtml, actualHtml, "The html() method should escape special characters.");

        // 2. Verify that the toString() method provides the same output as html(), as per its contract.
        assertEquals(expectedHtml, actualToString, "The toString() method should be an alias for html().");
    }
}