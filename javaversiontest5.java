package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

/**
 * Tests for {@link JavaVersion#parseMajorJavaVersion(String)}, focusing on various
 * formats for Java 9 version strings.
 */
public class JavaVersionTest {

    @Test
    public void parseMajorVersion_forDottedOracleJdk9Version_shouldReturn9() {
        // Standard version string format for Oracle JDK 9, e.g., "9.0.4".
        int majorVersion = JavaVersion.parseMajorJavaVersion("9.0.4");
        assertThat(majorVersion).isEqualTo(9);
    }

    @Test
    public void parseMajorVersion_forDebianJava9Version_shouldReturn9() {
        // A non-standard format used by Debian, as reported in
        // https://github.com/google/gson/issues/1310.
        int majorVersion = JavaVersion.parseMajorJavaVersion("9-Debian");
        assertThat(majorVersion).isEqualTo(9);
    }

    @Test
    public void parseMajorVersion_forEarlyAccessJava9Version_shouldReturn9() {
        // Per JEP 223, the new version-string scheme includes early-access markers.
        // Example: "9-ea+19".
        int majorVersion = JavaVersion.parseMajorJavaVersion("9-ea+19");
        assertThat(majorVersion).isEqualTo(9);
    }

    @Test
    public void parseMajorVersion_forJava9VersionsWithBuildInfo_shouldReturn9() {
        // Per JEP 223, the version string can also contain build information,
        // which should be ignored by the parser.
        assertThat(JavaVersion.parseMajorJavaVersion("9+100")).isEqualTo(9);
        assertThat(JavaVersion.parseMajorJavaVersion("9.0.1+20")).isEqualTo(9);
        assertThat(JavaVersion.parseMajorJavaVersion("9.1.1+20")).isEqualTo(9);
    }
}