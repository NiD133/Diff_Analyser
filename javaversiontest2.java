package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

/**
 * Tests for {@link JavaVersion}.
 */
public final class JavaVersionTest {

    @Test
    public void parseMajorJavaVersion_legacyFormat_returnsCorrectVersion() {
        // For Java versions before 9, the version string followed the format "1.major.minor...".
        // This test verifies that a version string like "1.6.0" is correctly parsed as major version 6.
        assertThat(JavaVersion.parseMajorJavaVersion("1.6.0")).isEqualTo(6);
    }
}