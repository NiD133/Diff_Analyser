package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link CaseFormat}.
 */
@RunWith(JUnit4.class)
public class CaseFormatTest {

  /**
   * Verifies that converting a string from a format to the exact same format
   * results in the original, unchanged string.
   */
  @Test
  public void to_identityConversionForLowerCamel_returnsOriginalString() {
    // A string converted to its own format should not change.
    String singleWord = "foo";
    assertThat(LOWER_CAMEL.to(LOWER_CAMEL, singleWord)).isEqualTo(singleWord);

    String multiWord = "fooBar";
    assertThat(LOWER_CAMEL.to(LOWER_CAMEL, multiWord)).isEqualTo(multiWord);
  }
}