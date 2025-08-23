package com.google.common.base;

import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for the conversion from {@link CaseFormat#UPPER_UNDERSCORE} to
 * {@link CaseFormat#UPPER_CAMEL}.
 */
@RunWith(JUnit4.class)
public class CaseFormatTest {

    @Test
    public void upperUnderscore_to_upperCamel_convertsStringsCorrectly() {
        // A single word should be converted to title case.
        assertThat(UPPER_UNDERSCORE.to(UPPER_CAMEL, "FOO")).isEqualTo("Foo");

        // Multiple words should be joined, with each word converted to title case.
        assertThat(UPPER_UNDERSCORE.to(UPPER_CAMEL, "FOO_BAR")).isEqualTo("FooBar");

        // An acronym-like string should be fully capitalized, which tests the
        // special handling of single-letter words.
        assertThat(UPPER_UNDERSCORE.to(UPPER_CAMEL, "H_T_T_P")).isEqualTo("HTTP");
    }
}