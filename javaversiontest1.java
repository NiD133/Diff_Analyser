package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

/**
 * Tests for {@link JavaVersion}.
 * The tests for {@code parseMajorJavaVersion} are the most important ones,
 * as they test the core logic of this class.
 */
public final class JavaVersionTest {

    @Test
    public void getMajorJavaVersion_onCurrentJvm_isAtLeastSupportedVersion() {
        // This test is a good sanity check for the execution environment.
        // Gson requires Java 8 or later.
        assertThat(JavaVersion.getMajorJavaVersion()).isAtLeast(8);
    }

    @Test
    public void parseMajorJavaVersion_parsesLegacyFormat() {
        // Format: 1.x.y_z (e.g., "1.8.0_131")
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_131")).isEqualTo(8);
        assertThat(JavaVersion.parseMajorJavaVersion("1.7.0")).isEqualTo(7);
        assertThat(JavaVersion.parseMajorJavaVersion("1.6.0")).isEqualTo(6);
    }

    @Test
    public void parseMajorJavaVersion_parsesNewFormat() {
        // Format: x.y.z (e.g., "9.0.1", "11.0.2") or just "x" (e.g., "17")
        assertThat(JavaVersion.parseMajorJavaVersion("9.0.1")).isEqualTo(9);
        assertThat(JavaVersion.parseMajorJavaVersion("11.0.2")).isEqualTo(11);
        assertThat(JavaVersion.parseMajorJavaVersion("17")).isEqualTo(17);
    }

    @Test
    public void parseMajorJavaVersion_parsesNonStandardFormats() {
        // These versions are not parsed by the primary dotted-notation logic
        // and fall back to extracting the initial integer from the string.
        assertThat(JavaVersion.parseMajorJavaVersion("9-debian")).isEqualTo(9);
        assertThat(JavaVersion.parseMajorJavaVersion("11-ea")).isEqualTo(11);
    }

    @Test
    public void parseMajorJavaVersion_unparseableVersion_returnsFallback() {
        // When the version string does not start with a number, the parser
        // should return a fallback version (currently 6).
        assertThat(JavaVersion.parseMajorJavaVersion("non-numeric-version")).isEqualTo(6);
        assertThat(JavaVersion.parseMajorJavaVersion("")).isEqualTo(6);
    }
}