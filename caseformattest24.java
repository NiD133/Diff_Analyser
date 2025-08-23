package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest extends TestCase {

  public void testTo_fromUpperUnderscoreToLowerUnderscore_singleWord() {
    String converted = UPPER_UNDERSCORE.to(LOWER_UNDERSCORE, "FOO");
    assertThat(converted).isEqualTo("foo");
  }

  public void testTo_fromUpperUnderscoreToLowerUnderscore_multipleWords() {
    String converted = UPPER_UNDERSCORE.to(LOWER_UNDERSCORE, "FOO_BAR");
    assertThat(converted).isEqualTo("foo_bar");
  }
}