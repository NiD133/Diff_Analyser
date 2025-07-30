/*
 * Copyright (C) 2017 The Gson authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Unit and functional tests for {@link JavaVersion}.
 * These tests verify the correct parsing of Java version strings and ensure
 * that the major Java version is correctly identified.
 * 
 * Test cases include checks for various Java version formats and edge cases.
 * 
 * Author: Inderjeet Singh
 */
public class JavaVersionTest {

  /**
   * Verifies that the current Java version is at least Java 8.
   * Gson requires a minimum of Java 8 to function correctly.
   */
  @Test
  public void testGetMajorJavaVersion() {
    assertThat(JavaVersion.getMajorJavaVersion()).isAtLeast(8);
  }

  /**
   * Verifies parsing of Java 6 version string.
   */
  @Test
  public void testJava6() {
    assertThat(JavaVersion.parseMajorJavaVersion("1.6.0")).isEqualTo(6);
  }

  /**
   * Verifies parsing of Java 7 version string.
   */
  @Test
  public void testJava7() {
    assertThat(JavaVersion.parseMajorJavaVersion("1.7.0")).isEqualTo(7);
  }

  /**
   * Verifies parsing of various Java 8 version strings.
   */
  @Test
  public void testJava8() {
    assertThat(JavaVersion.parseMajorJavaVersion("1.8")).isEqualTo(8);
    assertThat(JavaVersion.parseMajorJavaVersion("1.8.0")).isEqualTo(8);
    assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_131")).isEqualTo(8);
    assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_60-ea")).isEqualTo(8);
    assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_111-internal")).isEqualTo(8);
    assertThat(JavaVersion.parseMajorJavaVersion("1.8.0-internal")).isEqualTo(8);
    assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_131-adoptopenjdk")).isEqualTo(8);
  }

  /**
   * Verifies parsing of various Java 9 version strings.
   */
  @Test
  public void testJava9() {
    // Legacy style version string
    assertThat(JavaVersion.parseMajorJavaVersion("9.0.4")).isEqualTo(9);
    // Debian specific version string
    assertThat(JavaVersion.parseMajorJavaVersion("9-Debian")).isEqualTo(9);
    // New style version strings
    assertThat(JavaVersion.parseMajorJavaVersion("9-ea+19")).isEqualTo(9);
    assertThat(JavaVersion.parseMajorJavaVersion("9+100")).isEqualTo(9);
    assertThat(JavaVersion.parseMajorJavaVersion("9.0.1+20")).isEqualTo(9);
    assertThat(JavaVersion.parseMajorJavaVersion("9.1.1+20")).isEqualTo(9);
  }

  /**
   * Verifies parsing of Java 10 version string.
   */
  @Test
  public void testJava10() {
    assertThat(JavaVersion.parseMajorJavaVersion("10.0.1")).isEqualTo(10);
  }

  /**
   * Verifies behavior with an unknown version format.
   * The method should return 6 for unrecognized formats.
   */
  @Test
  public void testUnknownVersionFormat() {
    assertThat(JavaVersion.parseMajorJavaVersion("Java9")).isEqualTo(6);
  }
}