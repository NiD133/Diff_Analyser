package org.jsoup.internal;

import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link StringUtil#resolve(URL, String)}.
 */
public class StringUtilResolveTest {

    @Test
    public void resolveShouldHandleUrlPathsWithSpecialCharacters() throws MalformedURLException {
        // Arrange
        // The base URL from the original test's MockURL.getFtpExample()
        URL baseUrl = new URL("ftp://ftp.someFakeButWellFormedURL.org/");
        String relativeUrl = "=:,DeW^";
        String expectedResolvedUrl = "ftp://ftp.someFakeButWellFormedURL.org/=:,DeW^";

        // Act
        URL actualResolvedUrl = StringUtil.resolve(baseUrl, relativeUrl);

        // Assert
        assertEquals(expectedResolvedUrl, actualResolvedUrl.toString());
    }
}