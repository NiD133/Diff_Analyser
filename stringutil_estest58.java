package org.jsoup.internal;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Test suite for the {@link StringUtil#resolve(URL, String)} method.
 */
public class StringUtilTest {

    /**
     * Verifies that calling resolve with a null base URL throws a NullPointerException,
     * as the underlying URL constructor does not permit a null context.
     */
    @Test(expected = NullPointerException.class)
    public void resolveWithNullBaseUrlThrowsNullPointerException() throws MalformedURLException {
        // The 'base' URL is null, which should cause the method to throw a NullPointerException.
        // The relative URL can be any valid string, as it's the null base that triggers the exception.
        StringUtil.resolve(null, "/some/path");
    }
}