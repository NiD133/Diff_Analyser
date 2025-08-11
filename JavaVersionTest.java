package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for JavaVersion parsing and environment detection.
 *
 * Focus:
 * - Parse a variety of real-world java.version strings into a major version number.
 * - Keep tests environment-stable (no assumptions beyond “current JVM is at least Java 8”).
 * - Document why each sample string exists (legacy vs. new format, vendor variations).
 */
public class JavaVersionTest {

  /**
   * Our build/runtime must run on at least Java 8.
   * This assertion intentionally does not pin to an exact version to remain CI-friendly.
   */
  @Test
  public void currentJvmMajorVersion_isAtLeast8() {
    assertThat(JavaVersion.getMajorJavaVersion()).isAtLeast(8);
  }

  /**
   * Sanity check for the convenience method: it must reflect the same logic
   * as getMajorJavaVersion() >= 9 regardless of the JVM the tests run on.
   */
  @Test
  public void isJava9OrLater_agreesWithGetMajorJavaVersion() {
    boolean expected = JavaVersion.getMajorJavaVersion() >= 9;
    assertThat(JavaVersion.isJava9OrLater()).isEqualTo(expected);
  }

  @Test
  public void parses_java6_legacyScheme() {
    // Legacy "1.x" scheme
    assertParsesTo(6, "1.6.0");
  }

  @Test
  public void parses_java7_legacyScheme() {
    // Legacy "1.x" scheme
    assertParsesTo(7, "1.7.0");
  }

  @Test
  public void parses_java8_variations() {
    // Legacy "1.8" scheme with various vendor/build suffixes
    assertParsesTo(
        8,
        "1.8",
        "1.8.0",
        "1.8.0_131",
        "1.8.0_60-ea",
        "1.8.0_111-internal",
        // OpenJDK 8 (AdoptOpenJDK et al.) variations
        "1.8.0-internal",
        "1.8.0_131-adoptopenjdk");
  }

  @Test
  public void parses_java9_variations() {
    // Legacy dotted style
    assertParsesTo(9, "9.0.4");   // Oracle JDK 9
    // Debian uses a hyphenated suffix
    assertParsesTo(9, "9-Debian");

    // New versioning scheme (JEP 223), including early access and build metadata
    assertParsesTo(9, "9-ea+19", "9+100", "9.0.1+20", "9.1.1+20");
  }

  @Test
  public void parses_java10_dottedStyle() {
    assertParsesTo(10, "10.0.1"); // Oracle JDK 10.0.1
  }

  @Test
  public void unknownFormat_defaultsToJava6() {
    // Defensive fallback behavior for unrecognized formats
    assertParsesTo(6, "Java9");
  }

  // Helper to keep assertions concise and failure messages meaningful.
  private static void assertParsesTo(int expectedMajor, String... versionStrings) {
    for (String v : versionStrings) {
      assertThat(JavaVersion.parseMajorJavaVersion(v))
          .named("java.version='" + v + "'")
          .isEqualTo(expectedMajor);
    }
  }
}