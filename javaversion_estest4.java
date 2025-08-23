package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JavaVersion} utility class.
 */
public class JavaVersionTest {

    /**
     * Tests that parsing an empty version string results in a fallback version.
     * The current implementation defaults to version 6 for any unparseable string,
     * treating it as an old, unsupported Java version.
     */
    @Test
    public void parseMajorJavaVersion_withEmptyString_returnsFallbackVersion() {
        // Arrange: The expected fallback version for unparseable strings is 6.
        int expectedFallbackVersion = 6;
        String emptyVersionString = "";

        // Act: Parse the empty string.
        int actualVersion = JavaVersion.parseMajorJavaVersion(emptyVersionString);

        // Assert: The result should be the fallback version.
        assertEquals(expectedFallbackVersion, actualVersion);
    }
}