package org.jsoup.internal;

import org.junit.Test;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that appendNormalisedWhitespace throws a NullPointerException
     * when the provided StringBuilder instance is null, as this is an invalid argument.
     */
    @Test(expected = NullPointerException.class)
    public void appendNormalisedWhitespaceShouldThrowNullPointerExceptionWhenBuilderIsNull() {
        // Call the method with a null StringBuilder to trigger the exception.
        // The other arguments are arbitrary valid values.
        StringUtil.appendNormalisedWhitespace(null, "some text", true);
    }
}