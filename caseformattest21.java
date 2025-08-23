package com.google.common.base;

import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest extends TestCase {

  /**
   * Tests that converting a string from a format to itself is an identity operation, meaning the
   * original string is returned unchanged.
   */
  public void testTo_upperCamelToItself_returnsUnchangedString() {
    // Test with a single "word"
    String singleWord = "Foo";
    assertThat(UPPER_CAMEL.to(UPPER_CAMEL, singleWord)).isEqualTo(singleWord);

    // Test with multiple "words"
    String multipleWords = "FooBar";
    assertThat(UPPER_CAMEL.to(UPPER_CAMEL, multipleWords)).isEqualTo(multipleWords);
  }
}