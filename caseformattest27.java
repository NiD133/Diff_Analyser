package com.google.common.base;

import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest extends TestCase {

  /**
   * Verifies that converting a string from a format to the same format results in the
   * original, unchanged string.
   */
  public void testIdentityConversion_upperUnderscore() {
    String singleWord = "FOO";
    assertThat(UPPER_UNDERSCORE.to(UPPER_UNDERSCORE, singleWord))
        .isEqualTo(singleWord);

    String multipleWords = "FOO_BAR";
    assertThat(UPPER_UNDERSCORE.to(UPPER_UNDERSCORE, multipleWords))
        .isEqualTo(multipleWords);
  }
}