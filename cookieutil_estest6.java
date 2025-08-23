package org.jsoup.helper;

import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link CookieUtil} helper class.
 */
public class CookieUtilTest {

    /**
     * Verifies that {@link CookieUtil#asUri(URL)} throws a MalformedURLException
     * when given a URL containing characters that are illegal in a URI, such as a space.
     * <p>
     * The underlying {@link URL#toURI()} method fails for such URLs, and CookieUtil is
     * expected to wrap this failure in a MalformedURLException.
     */
    @Test
    public void asUriThrowsExceptionForUrlWithIllegalCharacters() throws MalformedURLException {
        // Arrange: Create a URL with a space in its path. While the URL object can be
        // created, it cannot be converted to a valid URI without encoding.
        URL urlWithInvalidUriChar = new URL("http://example.com/path with space");

        // Act & Assert
        try {
            CookieUtil.asUri(urlWithInvalidUriChar);
            fail("Expected an exception to be thrown for a URL with illegal characters.");
        } catch (IOException e) {
            // The method signature declares IOException, but we expect a MalformedURLException specifically.
            assertTrue("Exception should be an instance of MalformedURLException.", e instanceof MalformedURLException);

            // Verify that the exception message clearly indicates the root cause.
            String message = e.getMessage();
            assertTrue("Exception message should indicate an illegal character problem.",
                message != null && message.contains("Illegal character in path"));
        }
    }
}