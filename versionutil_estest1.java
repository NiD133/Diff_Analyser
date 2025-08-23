package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link VersionUtil} class.
 */
public class VersionUtilTest {

    /**
     * Verifies that parseVersionPart() correctly extracts an integer from the
     * beginning of a string and stops parsing at the first non-digit character.
     */
    @Test
    public void parseVersionPartShouldExtractLeadingIntegerAndIgnoreTrailingText() {
        // Arrange: A string that starts with a number followed by various non-numeric characters.
        // The method is expected to parse "0" and ignore the rest.
        String versionString = "0T*A{.*6/lD:1tm E_";
        int expectedNumber = 0;

        // Act: Call the method under test.
        int actualNumber = VersionUtil.parseVersionPart(versionString);

        // Assert: The returned value should be the integer found at the start of the string.
        assertEquals("Should parse the initial '0' and ignore subsequent non-digit characters",
                expectedNumber, actualNumber);
    }
}