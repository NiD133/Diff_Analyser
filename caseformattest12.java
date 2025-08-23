package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for conversions from {@link CaseFormat#LOWER_UNDERSCORE} to {@link
 * CaseFormat#UPPER_UNDERSCORE}.
 */
public class CaseFormatTest extends TestCase {

  public void testLowerUnderscoreToUpperUnderscore_forSingleWord() {
    // Arrange: Define the input and the expected outcome.
    String input = "foo";
    String expectedOutput = "FOO";

    // Act: Perform the conversion.
    String actualOutput = LOWER_UNDERSCORE.to(UPPER_UNDERSCORE, input);

    // Assert: Verify the result is as expected.
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  public void testLowerUnderscoreToUpperUnderscore_forMultipleWords() {
    // Arrange
    String input = "foo_bar";
    String expectedOutput = "FOO_BAR";

    // Act
    String actualOutput = LOWER_UNDERSCORE.to(UPPER_UNDERSCORE, input);

    // Assert
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }
}