package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Parameterized tests for conversions from {@link CaseFormat#LOWER_HYPHEN} to {@link
 * CaseFormat#UPPER_UNDERSCORE}.
 */
@RunWith(Parameterized.class)
public class LowerHyphenToUpperUnderscoreTest {

  private final String input;
  private final String expectedOutput;

  public LowerHyphenToUpperUnderscoreTest(String input, String expectedOutput) {
    this.input = input;
    this.expectedOutput = expectedOutput;
  }

  @Parameters(name = "\"{0}\" -> \"{1}\")
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          // Test case 1: Single word
          {"foo", "FOO"},
          // Test case 2: Multiple words
          {"foo-bar", "FOO_BAR"},
          // Test case 3: Empty string (edge case)
          {"", ""},
          // Test case 4: String with leading hyphen (edge case)
          {"-foo", "_FOO"},
          // Test case 5: String with trailing hyphen (edge case)
          {"foo-", "FOO_"}
        });
  }

  @Test
  public void to_fromLowerHyphenToUpperUnderscore_convertsCorrectly() {
    // Arrange: The input and expectedOutput are provided by the test parameters.

    // Act: Perform the case conversion.
    String actualOutput = LOWER_HYPHEN.to(UPPER_UNDERSCORE, input);

    // Assert: Verify the result matches the expected output.
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }
}