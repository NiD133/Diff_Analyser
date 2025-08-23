package org.jsoup.internal;

import org.junit.Test;

/**
 * Test suite for the {@link StringUtil} class, focusing on edge cases.
 */
public class StringUtilTest {

    /**
     * Verifies that the {@code normaliseWhitespace} method throws a
     * {@code NullPointerException} when given a null input. This confirms
     * the method's contract of not accepting null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void normaliseWhitespaceShouldThrowExceptionForNullInput() {
        StringUtil.normaliseWhitespace(null);
    }
}