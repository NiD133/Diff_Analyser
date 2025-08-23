package org.jsoup.internal;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.internal.StringUtil.normaliseWhitespace;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link StringUtil}.
 */
class StringUtilTest {

    @Test
    @DisplayName("normaliseWhitespace should correctly handle strings with high surrogates")
    void normaliseWhitespaceHandlesHighSurrogates() {
        // Arrange: Create a string containing a surrogate pair (a single Unicode character represented by two Java chars),
        // followed by multiple spaces. This tests that normalization handles multi-char codepoints correctly.
        // The surrogate pair \ud869\udeb2 represents the CJK character U+2A6B2.
        String stringWithSurrogatePair = "\ud869\udeb2\u304b\u309a  1";
        String expectedNormalizedString = "\ud869\udeb2\u304b\u309a 1";

        // Act
        String directlyNormalized = normaliseWhitespace(stringWithSurrogatePair);
        String parsedAndNormalized = Jsoup.parse(stringWithSurrogatePair).text();

        // Assert: Verify that the whitespace is normalized to a single space, both directly
        // and through the full Jsoup.parse->text() lifecycle.
        assertAll("Whitespace normalization with surrogate pairs",
            () -> assertEquals(expectedNormalizedString, directlyNormalized,
                "Direct call to normaliseWhitespace should preserve the surrogate pair."),
            () -> assertEquals(expectedNormalizedString, parsedAndNormalized,
                "Text extraction via Jsoup.parse().text() should also normalize correctly.")
        );
    }
}