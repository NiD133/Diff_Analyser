package org.jsoup.internal;

import org.junit.Test;

/**
 * Test suite for the {@link StringUtil} class, focusing on URL resolution.
 */
public class StringUtilTest {

    /**
     * Verifies that calling resolve with a null base URL throws a NullPointerException.
     * A base URL is required to resolve a relative URL, so a null value is invalid.
     */
    @Test(expected = NullPointerException.class)
    public void resolveShouldThrowNullPointerExceptionWhenBaseUrlIsNull() {
        // The resolve method cannot create an absolute URL without a valid base URL.
        // The original test passed null for both arguments; this behavior is preserved.
        StringUtil.resolve(null, null);
    }
}