package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JavaVersion}.
 */
public class JavaVersionTest {

    /**
     * Verifies that `parseMajorJavaVersion` correctly handles strings that start with a '0'
     * followed by non-numeric characters. The expected behavior is to parse the initial
     * integer and ignore the rest of the string.
     */
    @Test
    public void parseMajorJavaVersion_whenStringStartsWithZeroAndContainsText_returnsZero() {
        // Arrange: A non-standard version string to test the parsing logic.
        String versionString = "0Q?";

        // Act: Call the method under test.
        int majorVersion = JavaVersion.parseMajorJavaVersion(versionString);

        // Assert: The parsed version should be 0.
        assertEquals(0, majorVersion);
    }
}