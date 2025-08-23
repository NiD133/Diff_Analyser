package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for conversions from {@link CaseFormat#LOWER_CAMEL} to {@link CaseFormat#UPPER_UNDERSCORE}.
 */
public class CaseFormatTest extends TestCase {

  /**
   * Tests the conversion of a single-word string, which should result in an all-caps version of
   * the same word.
   */
  public void testLowerCamelToUpperUnderscore_convertsSingleWord() {
    String converted = LOWER_CAMEL.to(UPPER_UNDERSCORE, "foo");
    assertThat(converted).isEqualTo("FOO");
  }

  /**
   * Tests the conversion of a multi-word string, which should result in an underscore-separated,
   * all-caps string.
   */
  public void testLowerCamelToUpperUnderscore_convertsMultipleWords() {
    String converted = LOWER_CAMEL.to(UPPER_UNDERSCORE, "fooBar");
    assertThat(converted).isEqualTo("FOO_BAR");
  }
}