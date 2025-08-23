package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link VersionUtil} class, focusing on edge cases for version string parsing.
 */
public class VersionUtilTest {

    /**
     * Verifies that parseVersion() throws an ArrayIndexOutOfBoundsException
     * when the version string consists only of a separator character.
     * <p>
     * The method is expected to split the version string by separators. An input
     * like ";" results in an empty array of version components. Attempting to
     * access the first component (major version) from this empty array causes the
     * exception.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void parseVersion_whenVersionStringIsOnlyASeparator_throwsException() {
        // Act: Call parseVersion with a version string that is only a separator.
        // The groupId and artifactId are arbitrary for this test case.
        VersionUtil.parseVersion(";", "some.group.id", "some-artifact-id");
    }
}