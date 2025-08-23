package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for converting from {@link CaseFormat#LOWER_CAMEL} to {@link CaseFormat#LOWER_UNDERSCORE}.
 */
public class CaseFormatTestTest14 extends TestCase {

  /**
   * Tests that a single, all-lowercase word is not modified by the conversion, as it contains no
   * word boundaries.
   */
  public void testLowerCamelToUnderscore_singleWordIsUnchanged() {
    String input = "foo";
    String result = LOWER_CAMEL.to(LOWER_UNDERSCORE, input);
    assertThat(result).isEqualTo("foo");
  }

  /**
   * Tests the standard conversion of a multi-word lowerCamelCase string to lower_underscore_case.
   */
  public void testLowerCamelToUnderscore_convertsStandardInput() {
    String input = "fooBar";
    String result = LOWER_CAMEL.to(LOWER_UNDERSCORE, input);
    assertThat(result).isEqualTo("foo_bar");
  }

  /**
   * Tests that consecutive capital letters, often found in acronyms, are each treated as a new
   * word, separated by underscores.
   */
  public void testLowerCamelToUnderscore_handlesAcronyms() {
    String input = "hTTP";
    String result = LOWER_CAMEL.to(LOWER_UNDERSCORE, input);
    assertThat(result).isEqualTo("h_t_t_p");
  }
}