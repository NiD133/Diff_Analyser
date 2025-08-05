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
 * Unit and functional tests for {@link JavaVersion}
 *
 * @author Inderjeet Singh
 */
public class JavaVersionTest {
  // Borrowed some of test strings from
  // https://github.com/prestodb/presto/blob/master/presto-main/src/test/java/com/facebook/presto/server/TestJavaVersion.java

  @Test
  public void getMajorJavaVersion_currentVersionIsAtLeast8() {
    // Gson requires at least Java 8
    assertThat(JavaVersion.getMajorJavaVersion()).isAtLeast(8);
  }

  @Test
  public void parseMajorJavaVersion_java6() {
    // Java 6 version format: http://www.oracle.com/technetwork/java/javase/version-6-141920.html
    assertThat(JavaVersion.parseMajorJavaVersion("1.6.0")).isEqualTo(6);
  }

  @Test
  public void parseMajorJavaVersion_java7() {
    // Java 7 version format: http://www.oracle.com/technetwork/java/javase/jdk7-naming-418744.html
    assertThat(JavaVersion.parseMajorJavaVersion("1.7.0")).isEqualTo(7);
  }

  @Test
  public void parseMajorJavaVersion_java8() {
    String[] versions = {
        "1.8",           // Basic version
        "1.8.0",         // With minor version
        "1.8.0_131",     // With update number
        "1.8.0_60-ea",   // Early access release
        "1.8.0_111-internal", // Internal build
        "1.8.0-internal",      // OpenJDK format (https://github.com/AdoptOpenJDK/openjdk-build/issues/93)
        "1.8.0_131-adoptopenjdk" // AdoptOpenJDK build
    };

    for (String version : versions) {
      assertThat(JavaVersion.parseMajorJavaVersion(version)).isEqualTo(8);
    }
  }

  @Test
  public void parseMajorJavaVersion_java9() {
    String[] versions = {
        // Oracle JDK 9 legacy format
        "9.0.4",
        // Debian-specific format (https://github.com/google/gson/issues/1310)
        "9-Debian",
        // New versioning formats
        "9-ea+19",      // Early access
        "9+100",         // Build number
        "9.0.1+20",      // Point release with build
        "9.1.1+20"       // Minor release with build
    };

    for (String version : versions) {
      assertThat(JavaVersion.parseMajorJavaVersion(version)).isEqualTo(9);
    }
  }

  @Test
  public void parseMajorJavaVersion_java10() {
    // Oracle JDK 10 format
    assertThat(JavaVersion.parseMajorJavaVersion("10.0.1")).isEqualTo(10);
  }

  @Test
  public void parseMajorJavaVersion_unknownFormatDefaultsTo6() {
    // Unrecognized format should default to Java 6
    assertThat(JavaVersion.parseMajorJavaVersion("Java9")).isEqualTo(6);
  }
}