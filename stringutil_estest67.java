package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link StringUtil} class.
 */
public class StringUtilTest {

    @Test
    public void isInvisibleCharIdentifiesSoftHyphenAsInvisible() {
        // The character code 173 (U+00AD) represents a soft hyphen (&shy;).
        // This character is used to suggest a line-break point and is not typically visible.
        final int softHyphenCodePoint = 173;

        assertTrue(
            "The soft hyphen character should be considered invisible.",
            StringUtil.isInvisibleChar(softHyphenCodePoint)
        );
    }
}