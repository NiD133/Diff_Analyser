package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link CaseFormat}, focusing on the conversion from {@code UPPER_CAMEL} to {@code
 * LOWER_UNDERSCORE}.
 */
@RunWith(JUnit4.class)
public class CaseFormatTest {

  @Test
  public void upperCamelToLowerUnderscore_convertsSingleWord() {
    // Arrange
    String input = "Foo";
    String expected = "foo";

    // Act
    String result = UPPER_CAMEL.to(LOWER_UNDERSCORE, input);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void upperCamelToLowerUnderscore_convertsMultipleWords() {
    // Arrange
    String input = "FooBar";
    String expected = "foo_bar";

    // Act
    String result = UPPER_CAMEL.to(LOWER_UNDERSCORE, input);

    // Assert
    assertThat(result).isEqualTo(expected);
  }
}