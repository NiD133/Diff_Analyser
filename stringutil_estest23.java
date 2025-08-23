package org.jsoup.internal;

import org.junit.jupiter.api.Test;
import java.net.MalformedURLException;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test suite for the URL resolution logic in {@link StringUtil}.
 */
class StringUtilTest {

    /**
     * Verifies that resolving a relative URL string with a null base URL
     * correctly throws a MalformedURLException. A URL cannot be constructed
     * from a relative path without a base to resolve against.
     */
    @Test
    void resolveWithNullBaseUrlAndRelativeUrlThrowsException() {
        // Arrange: Define the inputs for the test case.
        final URL baseUrl = null;
        final String relativeUrl = "B1s2U[+";
        final String expectedErrorMessage = "no protocol: " + relativeUrl;

        // Act & Assert: Execute the method and verify the expected exception is thrown.
        MalformedURLException thrownException = assertThrows(
            MalformedURLException.class,
            () -> StringUtil.resolve(baseUrl, relativeUrl),
            "A MalformedURLException should be thrown when the base URL is null and the relative URL is not absolute."
        );

        // Assert: Verify the exception message is as expected for more precise testing.
        assertEquals(expectedErrorMessage, thrownException.getMessage());
    }
}