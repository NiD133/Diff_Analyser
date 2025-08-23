package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for {@link CaseFormat}, focusing on identity conversions.
 */
public class CaseFormatTestTest3 extends TestCase {

  /**
   * Verifies that converting a string from a format to itself results in the original, unchanged
   * string. This is known as an identity conversion.
   */
  public void testConvertToSameFormat_lowerHyphen_returnsUnchangedString() {
    // Arrange: Define test cases for single and multiple words.
    String singleWord = "foo";
    String multipleWords = "foo-bar";

    // Act & Assert: The conversion from a format to itself should be a no-op.
    assertThat(LOWER_HYPHEN.to(LOWER_HYPHEN, singleWord)).isEqualTo(singleWord);
    assertThat(LOWER_HYPHEN.to(LOWER_HYPHEN, multipleWords)).isEqualTo(multipleWords);
  }
}