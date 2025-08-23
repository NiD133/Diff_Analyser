package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/**
 * Tests for the helper methods in {@link FieldNamingPolicy}.
 */
public class FieldNamingPolicyTest {

  @Test
  public void testSeparateCamelCase() {
    // Cases where no separation should occur
    assertThat(FieldNamingPolicy.separateCamelCase("a", '_')).isEqualTo("a");
    assertThat(FieldNamingPolicy.separateCamelCase("ab", '_')).isEqualTo("ab");
    // A single capital letter at the start is not considered a new word
    assertThat(FieldNamingPolicy.separateCamelCase("Ab", '_')).isEqualTo("Ab");

    // Standard camel case separation
    assertThat(FieldNamingPolicy.separateCamelCase("aB", '_')).isEqualTo("a_B");
    assertThat(FieldNamingPolicy.separateCamelCase("firstSecondThird", '_')).isEqualTo("first_Second_Third");

    // Handles acronyms by separating each capital letter
    assertThat(FieldNamingPolicy.separateCamelCase("AB", '_')).isEqualTo("A_B");

    // Handles strings that already contain the separator
    assertThat(FieldNamingPolicy.separateCamelCase("A_B", '_')).isEqualTo("A__B");

    // Edge cases with leading separators or numbers
    assertThat(FieldNamingPolicy.separateCamelCase("__", '_')).isEqualTo("__");
    assertThat(FieldNamingPolicy.separateCamelCase("_123", '_')).isEqualTo("_123");
  }
}