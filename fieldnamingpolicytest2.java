package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for the helper method {@link FieldNamingPolicy#upperCaseFirstLetter(String)}.
 */
@RunWith(Parameterized.class)
public class FieldNamingPolicyTest {

  @Parameters(name = "{index}: upperCaseFirstLetter(\"{0}\") => \"{1}\"")
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          // Standard cases
          {"a", "A"},
          {"ab", "Ab"},
          {"AB", "AB"},

          // Cases with leading underscores
          {"_a", "_A"},
          {"_ab", "_Ab"},
          {"__", "__"},

          // Cases with non-letter characters
          {"_1", "_1"}, // Digits should not be uppercased

          // Test cases for characters which have an uppercase variant but are not
          // considered letters by Character.isLetter().
          // See https://github.com/google/gson/issues/1965
          {"\u2170", "\u2170"}, // Small Roman Numeral One
          {"_\u2170", "_\u2170"},
          {"\u2170a", "\u2170A"} // First letter 'a' should be uppercased
        });
  }

  private final String input;
  private final String expectedOutput;

  public FieldNamingPolicyTest(String input, String expectedOutput) {
    this.input = input;
    this.expectedOutput = expectedOutput;
  }

  @Test
  public void testUpperCaseFirstLetter() {
    String actual = FieldNamingPolicy.upperCaseFirstLetter(input);
    assertThat(actual).isEqualTo(expectedOutput);
  }
}