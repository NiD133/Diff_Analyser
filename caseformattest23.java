package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for {@link CaseFormat}, focusing on conversions.
 */
public class CaseFormatTest extends TestCase {

  public void testTo_fromUpperUnderscoreToLowerHyphen_singleWord() {
    // Arrange
    String input = "FOO";
    String expected = "foo";

    // Act
    String actual = UPPER_UNDERSCORE.to(LOWER_HYPHEN, input);

    // Assert
    assertThat(actual).isEqualTo(expected);
  }

  public void testTo_fromUpperUnderscoreToLowerHyphen_multipleWords() {
    // Arrange
    String input = "FOO_BAR";
    String expected = "foo-bar";

    // Act
    String actual = UPPER_UNDERSCORE.to(LOWER_HYPHEN, input);

    // Assert
    assertThat(actual).isEqualTo(expected);
  }
}