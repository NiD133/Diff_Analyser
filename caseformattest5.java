package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for {@link CaseFormat} conversions from {@code LOWER_HYPHEN} to {@code LOWER_CAMEL}.
 */
public class CaseFormatTestTest5 extends TestCase {

  public void testLowerHyphenToLowerCamel_singleWordRemainsUnchanged() {
    String result = LOWER_HYPHEN.to(LOWER_CAMEL, "foo");
    assertThat(result).isEqualTo("foo");
  }

  public void testLowerHyphenToLowerCamel_convertsMultipleWords() {
    String result = LOWER_HYPHEN.to(LOWER_CAMEL, "foo-bar");
    assertThat(result).isEqualTo("fooBar");
  }
}