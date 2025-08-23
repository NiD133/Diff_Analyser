package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link StringUtil} class, focusing on character type validation.
 */
public class StringUtilTest {

    /**
     * Verifies that the isInvisibleChar() method correctly identifies the
     * "zero-width space" character (Unicode 8203) as an invisible character.
     */
    @Test
    public void isInvisibleCharShouldReturnTrueForZeroWidthSpace() {
        // The integer 8203 represents the Unicode code point for a "zero-width space" (U+200B).
        // This character is expected to be treated as invisible by the utility method.
        final int zeroWidthSpaceCodePoint = 8203;

        assertTrue(
            "The zero-width space character should be classified as invisible.",
            StringUtil.isInvisibleChar(zeroWidthSpaceCodePoint)
        );
    }
}