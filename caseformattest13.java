package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for converting strings from {@link CaseFormat#LOWER_CAMEL} to {@link
 * CaseFormat#LOWER_HYPHEN}.
 */
public class CaseFormatTest extends TestCase {

  public void testLowerCamelToLowerHyphen_singleWord_isUnchanged() {
    assertThat(LOWER_CAMEL.to(LOWER_HYPHEN, "foo")).isEqualTo("foo");
  }

  public void testLowerCamelToLowerHyphen_standardConversion() {
    assertThat(LOWER_CAMEL.to(LOWER_HYPHEN, "fooBar")).isEqualTo("foo-bar");
  }

  /**
   * This test verifies a specific behavior: when an all-caps word like an acronym is encountered,
   * it's treated as a series of single-letter words.
   */
  public void testLowerCamelToLowerHyphen_acronymIsSplit() {
    assertThat(LOWER_CAMEL.to(LOWER_HYPHEN, "HTTP")).isEqualTo("h-t-t-p");
  }
}