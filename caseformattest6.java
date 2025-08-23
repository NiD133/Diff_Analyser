package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.truth.Truth.assertThat;

import junit.framework.TestCase;

/**
 * Tests for {@link CaseFormat} conversions from {@code LOWER_HYPHEN} to {@code UPPER_CAMEL}.
 */
public class CaseFormatTestTest6 extends TestCase {

  public void testLowerHyphenToUpperCamel_singleWord() {
    String converted = LOWER_HYPHEN.to(UPPER_CAMEL, "foo");
    assertThat(converted).isEqualTo("Foo");
  }

  public void testLowerHyphenToUpperCamel_multipleWords() {
    String converted = LOWER_HYPHEN.to(UPPER_CAMEL, "foo-bar");
    assertThat(converted).isEqualTo("FooBar");
  }
}