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
 * Unit tests for {@link JavaVersion} utility class.
 * 
 * Tests the parsing of various Java version string formats to extract major version numbers.
 * Test strings borrowed from:
 * https://github.com/prestodb/presto/blob/master/presto-main/src/test/java/com/facebook/presto/server/TestJavaVersion.java
 *
 * @author Inderjeet Singh
 */
public class JavaVersionTest {

  @Test
  public void getMajorJavaVersion_shouldReturnAtLeastJava8() {
    // Gson currently requires at least Java 8
    int actualMajorVersion = JavaVersion.getMajorJavaVersion();
    
    assertThat(actualMajorVersion).isAtLeast(8);
  }

  @Test
  public void parseMajorJavaVersion_java6Format_shouldReturn6() {
    // Java 6 format: http://www.oracle.com/technetwork/java/javase/version-6-141920.html
    String java6VersionString = "1.6.0";
    
    int parsedVersion = JavaVersion.parseMajorJavaVersion(java6VersionString);
    
    assertThat(parsedVersion).isEqualTo(6);
  }

  @Test
  public void parseMajorJavaVersion_java7Format_shouldReturn7() {
    // Java 7 format: http://www.oracle.com/technetwork/java/javase/jdk7-naming-418744.html
    String java7VersionString = "1.7.0";
    
    int parsedVersion = JavaVersion.parseMajorJavaVersion(java7VersionString);
    
    assertThat(parsedVersion).isEqualTo(7);
  }

  @Test
  public void parseMajorJavaVersion_java8Formats_shouldReturn8() {
    // Test various Java 8 version string formats
    assertJavaVersionParsesTo("1.8", 8);
    assertJavaVersionParsesTo("1.8.0", 8);
    assertJavaVersionParsesTo("1.8.0_131", 8);
    assertJavaVersionParsesTo("1.8.0_60-ea", 8);
    assertJavaVersionParsesTo("1.8.0_111-internal", 8);
    
    // OpenJDK 8 formats (see: https://github.com/AdoptOpenJDK/openjdk-build/issues/93)
    assertJavaVersionParsesTo("1.8.0-internal", 8);
    assertJavaVersionParsesTo("1.8.0_131-adoptopenjdk", 8);
  }

  @Test
  public void parseMajorJavaVersion_java9Formats_shouldReturn9() {
    // Legacy Java 9 format (Oracle JDK 9)
    assertJavaVersionParsesTo("9.0.4", 9);
    
    // Debian format (reported in https://github.com/google/gson/issues/1310)
    assertJavaVersionParsesTo("9-Debian", 9);

    // New Java 9+ version formats
    assertJavaVersionParsesTo("9-ea+19", 9);
    assertJavaVersionParsesTo("9+100", 9);
    assertJavaVersionParsesTo("9.0.1+20", 9);
    assertJavaVersionParsesTo("9.1.1+20", 9);
  }

  @Test
  public void parseMajorJavaVersion_java10Format_shouldReturn10() {
    // Oracle JDK 10.0.1 format
    String java10VersionString = "10.0.1";
    
    int parsedVersion = JavaVersion.parseMajorJavaVersion(java10VersionString);
    
    assertThat(parsedVersion).isEqualTo(10);
  }

  @Test
  public void parseMajorJavaVersion_unknownFormat_shouldReturnDefaultValue6() {
    // When version format is unrecognized, should return default value of 6
    String unknownVersionFormat = "Java9";
    
    int parsedVersion = JavaVersion.parseMajorJavaVersion(unknownVersionFormat);
    
    assertThat(parsedVersion).isEqualTo(6);
  }

  /**
   * Helper method to reduce duplication in version parsing assertions.
   * 
   * @param versionString the Java version string to parse
   * @param expectedMajorVersion the expected major version number
   */
  private void assertJavaVersionParsesTo(String versionString, int expectedMajorVersion) {
    int actualParsedVersion = JavaVersion.parseMajorJavaVersion(versionString);
    assertThat(actualParsedVersion)
        .named("Parsing version string: " + versionString)
        .isEqualTo(expectedMajorVersion);
  }
}