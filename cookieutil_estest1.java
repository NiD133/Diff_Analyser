package org.jsoup.helper;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link CookieUtil} helper class.
 */
public class CookieUtilTest {

    @Test
    public void asUriShouldCorrectlyConvertUrlToUri() throws IOException {
        // Arrange: Create a sample URL to be converted.
        // We use a standard URL constructor to make the test self-contained and clear.
        URL sourceUrl = new URL("ftp://ftp.example.com/resource/file.txt");

        // Act: Call the method under test.
        URI resultUri = CookieUtil.asUri(sourceUrl);

        // Assert: Verify that the resulting URI has the correct components.
        assertNotNull("The converted URI should not be null.", resultUri);
        assertEquals("The scheme should be correctly transferred.", "ftp", resultUri.getScheme());
        assertEquals("The host should be correctly transferred.", "ftp.example.com", resultUri.getHost());
        assertEquals("The path should be correctly transferred.", "/resource/file.txt", resultUri.getPath());
    }
}