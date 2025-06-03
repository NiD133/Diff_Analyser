package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Charsets} class, specifically focusing on UTF-16BE encoding.
 * This class aims to verify that the `Charsets` class provides the same UTF-16BE character set
 * as the standard Java library.
 */
public class CharsetsTest {

    @Test
    @DisplayName("Test that Charsets.UTF_16BE matches StandardCharsets.UTF_16BE")
    public void testUtf16Be() {
        // Arrange:  No explicit arrangement needed as we're comparing constants.

        // Act: Get the name of the UTF-16BE charset from both Charsets and StandardCharsets.
        String expectedCharsetName = StandardCharsets.UTF_16BE.name();
        String actualCharsetName = Charsets.UTF_16BE.name();

        // Assert:  Verify that the names are the same.  This confirms that both represent the same character set.
        assertEquals(expectedCharsetName, actualCharsetName, "Charsets.UTF_16BE should be the same as StandardCharsets.UTF_16BE");
    }
}