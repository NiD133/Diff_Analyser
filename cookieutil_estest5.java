package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for the {@link CookieUtil} helper class.
 */
public class CookieUtilTest {

    /**
     * Verifies that calling {@code parseCookie} with a null {@code HttpConnection.Response}
     * results in a {@code NullPointerException}, as the response object is a required
     * parameter for the method to operate on.
     */
    @Test(expected = NullPointerException.class)
    public void parseCookieWithNullResponseThrowsNullPointerException() {
        // The cookie string value is arbitrary as the test focuses on the null response.
        String anyCookieString = "name=value";
        CookieUtil.parseCookie(anyCookieString, (HttpConnection.Response) null);
    }
}