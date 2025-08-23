package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link CaseFormat} conversion from {@code LOWER_UNDERSCORE} to {@code UPPER_CAMEL}.
 */
@RunWith(JUnit4.class)
public class CaseFormatTestTest11 {

  @Test
  public void lowerUnderscoreToUpperCamel_convertsSingleWord() {
    assertThat(LOWER_UNDERSCORE.to(UPPER_CAMEL, "foo")).isEqualTo("Foo");
  }

  @Test
  public void lowerUnderscoreToUpperCamel_convertsMultipleWords() {
    assertThat(LOWER_UNDERSCORE.to(UPPER_CAMEL, "foo_bar")).isEqualTo("FooBar");
  }
}