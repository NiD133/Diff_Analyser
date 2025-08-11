package com.google.gson.internal;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for JavaVersion.
 *
 * Notes:
 * - Tests for parseMajorJavaVersion(String) are deterministic and do not depend on the JVM running the tests.
 * - Tests that touch the actual runtime version assert relationships (consistency) rather than hard-coding a specific version,
 *   so they remain stable across different JDKs.
 */
public class JavaVersionTest {

  // -----------------------
  // parseMajorJavaVersion()
  // -----------------------

  @Test
  public void parsesLegacyOneDotStyleVersion() {
    // e.g., "1.8.0_202" -> 8
    assertEquals(8, JavaVersion.parseMajorJavaVersion("1.8.0_202"));
    // Single-segment legacy "1" should map to 1
    assertEquals(1, JavaVersion.parseMajorJavaVersion("1"));
  }

  @Test
  public void parsesModernVersionFormats() {
    // e.g., "9.0.4" -> 9
    assertEquals(9, JavaVersion.parseMajorJavaVersion("9.0.4"));
    // Single number like "17" -> 17
    assertEquals(17, JavaVersion.parseMajorJavaVersion("17"));
  }

  @Test
  public void extractsLeadingDigitsAndIgnoresTrailingGarbage() {
    // Falls back to extracting leading digits when dotted parsing fails
    assertEquals(1, JavaVersion.parseMajorJavaVersion("1.&N<+EILs/Cn\","));
    assertEquals(11, JavaVersion.parseMajorJavaVersion("11-ea"));
    assertEquals(9, JavaVersion.parseMajorJavaVersion("9-debian"));
    assertEquals(0, JavaVersion.parseMajorJavaVersion("0Q?"));
  }

  @Test
  public void returnsDefaultWhenNoLeadingDigitsPresent() {
    // When nothing useful can be parsed, it should return the minimum supported version: 6
    assertEquals(6, JavaVersion.parseMajorJavaVersion(""));
    assertEquals(6, JavaVersion.parseMajorJavaVersion("not-a-version"));
  }

  @Test
  public void handlesNegativeAndSignedNumbers() {
    assertEquals(-8, JavaVersion.parseMajorJavaVersion("-8"));
  }

  @Test
  public void nullInputThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> JavaVersion.parseMajorJavaVersion(null));
  }

  // -----------------------
  // Runtime-dependent APIs
  // -----------------------

  @Test
  public void isJava9OrLaterIsConsistentWithGetMajorJavaVersion() {
    boolean expected = JavaVersion.getMajorJavaVersion() >= 9;
    assertEquals(expected, JavaVersion.isJava9OrLater());
  }

  @Test
  public void getMajorJavaVersionIsNeverLowerThanMinimumSupported() {
    // Implementation defaults to 6 if parsing fails, so it should never be below 6
    assertTrue(JavaVersion.getMajorJavaVersion() >= 6);
  }
}