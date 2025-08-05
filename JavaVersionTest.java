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
 * Unit tests for {@link JavaVersion}, verifying its version string parsing logic.
 *
 * @author Inderjeet Singh
 */
public final class JavaVersionTest {
  // Test strings for various Java versions were inspired by
  // https://github.com/prestodb/presto/blob/master/presto-main/src/test/java/com/facebook/presto/server/TestJavaVersion.java

  /**
   * Sanity check to ensure the current JVM's version is detected as at least 8,
   * which is the minimum requirement for Gson.
   */
  @Test
  public void getMajorJavaVersion_onCurrentJvm_isAtLeast8() {
    assertThat(JavaVersion.getMajorJavaVersion()).isAtLeast(8);
  }

  // The following tests verify the parseMajorJavaVersion(String) method for various version string formats.

  @Test
  public void parseMajorJavaVersion_forJava6_returns6() {
    // Format for Java 6: 1.6.0
    // See https://www.oracle.com/technetwork/java/javase/version-6-141920.html
    assertThat(JavaVersion.parseMajorJavaVersion("1.6.0")).isEqualTo(6);
  }

  @Test
  public void parseMajorJavaVersion_forJava7_returns7() {
    // Format for Java 7: 1.7.0
    // See https://www.oracle.com/technetwork/java/javase/jdk7-naming-418744.html
    assertThat(JavaVersion.parseMajorJavaVersion("1.7.0")).isEqualTo(7);
  }

  @Test
  public void parseMajorJavaVersion_forJava8_returns8() {
    // Pre-JEP 223 format for Java 8: "1.8.0_..."
    assertThat(JavaVersion.parseMajorJavaVersion("1.8")).isEqualTo(8);
    assertThat(JavaVersion.parseMajorJavaVersion("1.8.0")).isEqualTo(8);
    assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_131")).isEqualTo(8);
    assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_60-ea")).isEqualTo(8);
    assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_111-internal")).isEqualTo(8);

    // OpenJDK 8 build formats
    // See https://github.com/AdoptOpenJDK/openjdk-build/issues/93
    assertThat(JavaVersion.parseMajorJavaVersion("1.8.0-internal")).isEqualTo(8);
    assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_131-adoptopenjdk")).isEqualTo(8);
  }

  @Test
  public void parseMajorJavaVersion_forJava9_returns9() {
    // JEP 223 introduced a new versioning scheme: "9.0.4", "9-Debian", etc.

    // Legacy-style format
    assertThat(JavaVersion.parseMajorJavaVersion("9.0.4")).isEqualTo(9); // Oracle JDK 9

    // New versioning style
    assertThat(JavaVersion.parseMajorJavaVersion("9-ea+19")).isEqualTo(9);
    assertThat(JavaVersion.parseMajorJavaVersion("9+100")).isEqualTo(9);
    assertThat(JavaVersion.parseMajorJavaVersion("9.0.1+20")).isEqualTo(9);
    assertThat(JavaVersion.parseMajorJavaVersion("9.1.1+20")).isEqualTo(9);

    // Distro-specific format, e.g., Debian
    // See https://github.com/google/gson/issues/1310
    assertThat(JavaVersion.parseMajorJavaVersion("9-Debian")).isEqualTo(9);
  }

  @Test
  public void parseMajorJavaVersion_forJava10_returns10() {
    // JEP 223 format for Java 10
    assertThat(JavaVersion.parseMajorJavaVersion("10.0.1")).isEqualTo(10); // Oracle JDK 10.0.1
  }

  @Test
  public void parseMajorJavaVersion_forUnsupportedFormat_returnsFallback() {
    // When the version string format is not recognized, the method should return a
    // fallback version. The current implementation defaults to 6, likely because
    // it was a common baseline and versions before it are not a concern.
    assertThat(JavaVersion.parseMajorJavaVersion("Java9")).isEqualTo(6);
  }
}