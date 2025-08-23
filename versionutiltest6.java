package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Version;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link VersionUtil}.
 */
// The class name is simplified to follow standard conventions (SubjectUnderTestTest).
class VersionUtilTest {

    @Test
    // @DisplayName provides a clear, human-readable description of the test's purpose.
    @DisplayName("parseVersion should return an 'unknown' version when the version string is null")
    void parseVersion_withNullVersionString_shouldReturnUnknownVersion() {
        // Arrange: Set up the inputs for the test.
        // The null version string is the specific condition being tested.
        // GroupId and artifactId are provided as realistic, non-null values, as their
        // specific content is not expected to affect this particular outcome.
        String nullVersionString = null;
        String groupId = "com.fasterxml.jackson.core";
        String artifactId = "jackson-core";

        // Act: Call the method under test.
        Version resultVersion = VersionUtil.parseVersion(nullVersionString, groupId, artifactId);

        // Assert: Verify the result.
        // The method is expected to return a special "unknown" version instance
        // when it cannot parse the version string. Asserting equality with
        // Version.unknownVersion() is a more direct and comprehensive check
        // than just verifying a single property (like whether it's a snapshot).
        assertEquals(Version.unknownVersion(), resultVersion);
    }
}