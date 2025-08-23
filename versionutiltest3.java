package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link VersionUtil} class.
 */
@DisplayName("VersionUtil")
class VersionUtilTest {

    @Test
    @DisplayName("parseVersionPart() should parse the leading integer from a string")
    void shouldParseLeadingIntegerFromStringWithSuffix() {
        // Arrange: A version string part with trailing non-digit characters.
        String versionString = "66R";
        int expectedNumber = 66;

        // Act: Parse the string to extract the version number.
        int actualNumber = VersionUtil.parseVersionPart(versionString);

        // Assert: The extracted number should match the expected integer, ignoring the suffix.
        assertEquals(expectedNumber, actualNumber,
            "Should correctly extract the leading numerical part and ignore subsequent non-digit characters.");
    }
}