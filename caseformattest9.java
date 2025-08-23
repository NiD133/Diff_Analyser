package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for {@link CaseFormat}, focusing on identity conversions.
 */
public class CaseFormatTestTest9 extends TestCase {

  /**
   * Tests that converting a string from a format to itself is an identity operation,
   * meaning the output string is identical to the input.
   */
  public void testLowerUnderscore_convertToSelf_isIdentity() {
    // A string with a single "word"
    String singleWord = "foo";
    assertThat(LOWER_UNDERSCORE.to(LOWER_UNDERSCORE, singleWord)).isEqualTo(singleWord);

    // A string with multiple "words" separated by the format's delimiter
    String multiWord = "foo_bar";
    assertThat(LOWER_UNDERSCORE.to(LOWER_UNDERSCORE, multiWord)).isEqualTo(multiWord);
  }
}