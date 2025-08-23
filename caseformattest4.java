package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Unit tests for {@link CaseFormat}.
 */
public class CaseFormatTest extends TestCase {

  public void testTo_fromLowerHyphenToLowerUnderscore_givenSingleWord_returnsUnchangedString() {
    // Arrange
    String input = "foo";
    String expectedOutput = "foo";

    // Act
    String actualOutput = LOWER_HYPHEN.to(LOWER_UNDERSCORE, input);

    // Assert
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  public void testTo_fromLowerHyphenToLowerUnderscore_givenMultipleWords_convertsHyphenToUnderscore() {
    // Arrange
    String input = "foo-bar";
    String expectedOutput = "foo_bar";

    // Act
    String actualOutput = LOWER_HYPHEN.to(LOWER_UNDERSCORE, input);

    // Assert
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }
}