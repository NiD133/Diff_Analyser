package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for the conversion from {@link CaseFormat#UPPER_UNDERSCORE} to {@link
 * CaseFormat#LOWER_CAMEL}.
 */
public class CaseFormatTestTest25 extends TestCase {

  public void testUpperUnderscoreToLowerCamel_convertsSingleWord() {
    // Arrange
    String input = "FOO";
    String expectedOutput = "foo";

    // Act
    String actualOutput = UPPER_UNDERSCORE.to(LOWER_CAMEL, input);

    // Assert
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

  public void testUpperUnderscoreToLowerCamel_convertsMultipleWords() {
    // Arrange
    String input = "FOO_BAR";
    String expectedOutput = "fooBar";

    // Act
    String actualOutput = UPPER_UNDERSCORE.to(LOWER_CAMEL, input);

    // Assert
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }
}