package org.jsoup.helper;

import org.junit.Test;

/**
 * Tests for the {@link CookieUtil} helper class.
 */
public class CookieUtilTest {

    /**
     * Verifies that passing a null URL to asUri throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void asUriWithNullUrlThrowsNullPointerException() {
        // The asUri(URL url) method is expected to throw a NullPointerException
        // when its input is null. This test confirms that behavior.
        CookieUtil.asUri(null);
    }
}