package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import org.junit.Test;

/**
 * Readable, behavior-focused tests for CaseFormat.
 *
 * These tests exercise the public API with clear, representative examples so that
 * future maintainers can quickly understand the intended behavior.
 */
public class CaseFormatTest {

  // Basic conversions between the supported formats

  @Test
  public void lowerUnderscore_toUpperCamel_basic() {
    assertEquals("FooBar", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "foo_bar"));
    assertEquals("MaxValue", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "max_value"));
  }

  @Test
  public void upperUnderscore_toLowerCamel_basic() {
    assertEquals("fooBar", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "FOO_BAR"));
    assertEquals("maxValue", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "MAX_VALUE"));
  }

  @Test
  public void lowerHyphen_toLowerUnderscore_basic() {
    assertEquals("my_variable_name", CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_UNDERSCORE, "my-variable-name"));
    assertEquals("a_b_c", CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_UNDERSCORE, "a-b-c"));
  }

  @Test
  public void lowerCamel_toUpperUnderscore_basic() {
    assertEquals("FOO_BAR_BAZ", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "fooBarBaz"));
    assertEquals("USER_ID", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "userId"));
  }

  @Test
  public void upperCamel_toLowerHyphen_basic() {
    assertEquals("foo-bar-baz", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "FooBarBaz"));
    assertEquals("max-value", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "MaxValue"));
  }

  // Identity and simple edge cases

  @Test
  public void identityConversion_returnsInputUnchanged() {
    assertEquals("alreadyCamel", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, "alreadyCamel"));
    assertEquals("snake_case", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, "snake_case"));
  }

  @Test
  public void emptyString_returnsEmpty() {
    assertEquals("", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, ""));
    assertEquals("", CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, ""));
  }

  @Test
  public void digitsArePreservedDuringConversion() {
    assertEquals("ServiceV2Api", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "service_v2_api"));
    assertEquals("HTTP2_SERVER", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "http2Server"));
  }

  // Converter API

  @Test
  public void converter_convertsForwardAndBackward() {
    Converter<String, String> toUpperCamel = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.UPPER_CAMEL);

    assertNotNull(toUpperCamel);
    assertEquals("FooBar", toUpperCamel.convert("foo_bar"));
    assertEquals("foo_bar", toUpperCamel.reverse().convert("FooBar"));
  }

  @Test
  public void converter_identityConverterReturnsInput() {
    Converter<String, String> identity = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL);

    assertNotNull(identity);
    assertEquals("fooBar", identity.convert("fooBar"));
    assertEquals("fooBar", identity.reverse().convert("fooBar"));
  }

  // Null-safety

  @Test(expected = NullPointerException.class)
  public void to_withNullTargetFormat_throws() {
    CaseFormat.LOWER_CAMEL.to(null, "value");
  }

  @Test(expected = NullPointerException.class)
  public void to_withNullInput_throws() {
    CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, null);
  }

  @Test(expected = NullPointerException.class)
  public void converterTo_withNullTargetFormat_throws() {
    CaseFormat.LOWER_UNDERSCORE.converterTo(null);
  }
}