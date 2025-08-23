package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

/**
 * Tests for {@link JavaVersion}, focusing on its ability to parse version strings.
 */
public final class JavaVersionTest {

    @Test
    public void parseMajorJavaVersion_forModernVersionString_returnsMajorVersion() {
        // The version string format for Java 9 and later is "MAJOR.MINOR.SECURITY".
        // This test verifies that a version 10 string is parsed correctly.
        String java10VersionString = "10.0.1";

        int majorVersion = JavaVersion.parseMajorJavaVersion(java10VersionString);

        assertThat(majorVersion).isEqualTo(10);
    }
}