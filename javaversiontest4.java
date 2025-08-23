package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

/**
 * Tests for {@link JavaVersion#parseMajorJavaVersion(String)}.
 */
public class JavaVersionTest {

    @Test
    public void parseMajorVersion_forJava8_returns8() {
        // Test standard "1.8" formats
        assertThat(JavaVersion.parseMajorJavaVersion("1.8")).isEqualTo(8);
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0")).isEqualTo(8);

        // Test format with update version
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_131")).isEqualTo(8);

        // Test formats with suffixes, e.g., for early access or internal builds
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_60-ea")).isEqualTo(8);
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_111-internal")).isEqualTo(8);

        // Test specific OpenJDK formats
        // See: https://github.com/AdoptOpenJDK/openjdk-build/issues/93
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0-internal")).isEqualTo(8);
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_131-adoptopenjdk")).isEqualTo(8);
    }
}