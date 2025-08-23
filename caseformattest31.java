package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for the {@link Converter} returned by {@link CaseFormat#converterTo(CaseFormat)}.
 */
public class CaseFormatConverterTest extends TestCase {

  /**
   * The converter's toString() method should return a descriptive string that is useful for
   * debugging, clearly identifying the source and target formats.
   */
  public void testToString_returnsDescriptiveString() {
    // Arrange
    Converter<String, String> caseConverter = LOWER_HYPHEN.converterTo(UPPER_CAMEL);
    String expectedToString = "LOWER_HYPHEN.converterTo(UPPER_CAMEL)";

    // Act
    String actualToString = caseConverter.toString();

    // Assert
    assertThat(actualToString).isEqualTo(expectedToString);
  }
}