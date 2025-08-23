package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    @Test
    public void startsWithNewlineShouldReturnFalseForNullInput() {
        // The StringUtil.startsWithNewline method is expected to handle null input
        // gracefully by returning false, rather than throwing a NullPointerException.

        // When: The startsWithNewline method is called with a null string.
        boolean result = StringUtil.startsWithNewline(null);

        // Then: The method should return false.
        assertFalse("A null string should not be considered as starting with a newline.", result);
    }
}