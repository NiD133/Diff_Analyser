package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for the {@link Converter#reverse} functionality of the converter returned by
 * {@link CaseFormat#converterTo(CaseFormat)}.
 */
public class CaseFormatTestTest29 extends TestCase {

  /**
   * Tests that reversing a (UPPER_UNDERSCORE -> UPPER_CAMEL) converter results in a converter that
   * correctly converts from UPPER_CAMEL to UPPER_UNDERSCORE.
   */
  public void testReversedConverter_fromUpperCamel_toUpperUnderscore() {
    // Arrange: Create the reversed converter.
    Converter<String, String> reversedConverter =
        UPPER_UNDERSCORE.converterTo(UPPER_CAMEL).reverse();

    // Act & Assert: Verify the conversion from UpperCamel to UPPER_UNDERSCORE.
    assertThat(reversedConverter.convert("FooBar")).isEqualTo("FOO_BAR");
  }

  /**
   * Tests that reversing a (UPPER_UNDERSCORE -> LOWER_CAMEL) converter results in a converter that
   * correctly converts from LOWER_CAMEL to UPPER_UNDERSCORE.
   */
  public void testReversedConverter_fromLowerCamel_toUpperUnderscore() {
    // Arrange: Create the reversed converter.
    Converter<String, String> reversedConverter =
        UPPER_UNDERSCORE.converterTo(LOWER_CAMEL).reverse();

    // Act & Assert: Verify the conversion from lowerCamel to UPPER_UNDERSCORE.
    assertThat(reversedConverter.convert("fooBar")).isEqualTo("FOO_BAR");
  }

  /**
   * Tests that reversing a (UPPER_CAMEL -> UPPER_UNDERSCORE) converter results in a converter that
   * correctly converts from UPPER_UNDERSCORE to UPPER_CAMEL.
   */
  public void testReversedConverter_fromUpperUnderscore_toUpperCamel() {
    // Arrange: Create the reversed converter.
    Converter<String, String> reversedConverter =
        UPPER_CAMEL.converterTo(UPPER_UNDERSCORE).reverse();

    // Act & Assert: Verify the conversion from UPPER_UNDERSCORE to UpperCamel.
    assertThat(reversedConverter.convert("FOO_BAR")).isEqualTo("FooBar");
  }

  /**
   * Tests that reversing a (LOWER_CAMEL -> UPPER_UNDERSCORE) converter results in a converter that
   * correctly converts from UPPER_UNDERSCORE to LOWER_CAMEL.
   */
  public void testReversedConverter_fromUpperUnderscore_toLowerCamel() {
    // Arrange: Create the reversed converter.
    Converter<String, String> reversedConverter =
        LOWER_CAMEL.converterTo(UPPER_UNDERSCORE).reverse();

    // Act & Assert: Verify the conversion from UPPER_UNDERSCORE to lowerCamel.
    assertThat(reversedConverter.convert("FOO_BAR")).isEqualTo("fooBar");
  }
}