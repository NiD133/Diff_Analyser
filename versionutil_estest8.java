package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * Unit tests for the {@link VersionUtil} class.
 */
public class VersionUtilTest {

    /**
     * Verifies that {@code mavenVersionFor} throws a NullPointerException when
     * the provided ClassLoader is null. The ClassLoader is a required argument
     * for locating the Maven version properties.
     */
    @Test(expected = NullPointerException.class)
    public void mavenVersionFor_withNullClassLoader_shouldThrowNullPointerException() {
        // Act: Call the method with a null ClassLoader.
        // The other arguments are non-null to isolate the cause of the exception.
        VersionUtil.mavenVersionFor(null, "com.fasterxml.jackson.core", "jackson-core");
    }
}