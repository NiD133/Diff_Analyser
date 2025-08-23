package org.jsoup.nodes;

import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Entities} class, focusing on unescaping complex character entities.
 */
public class EntitiesTest {

    /**
     * Verifies that numeric HTML entities representing a UTF-16 surrogate pair are correctly unescaped.
     * This test uses the "hundred points" emoji (💯), which is outside the Basic Multilingual Plane (BMP)
     * and requires a surrogate pair for its representation in a Java String.
     */
    @Test
    void unescapeHandlesSurrogatePairInNumericEntities() {
        // Arrange: The HTML numeric entities for the high and low surrogates of the 💯 emoji.
        String htmlEncodedSurrogatePair = "&#55357;&#56495;";
        String expectedEmoji = "💯";

        // Act: Unescape the HTML entities. The 'strict' flag is false, which is the
        // typical mode for parsing web content.
        String actualEmoji = Parser.unescapeEntities(htmlEncodedSurrogatePair, false);

        // Assert: The unescaped string should match the literal emoji character.
        assertEquals(expectedEmoji, actualEmoji);
    }
}