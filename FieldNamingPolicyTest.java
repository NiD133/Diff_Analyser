/*
 * Copyright (C) 2021 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.lang.reflect.Field;
import java.util.Locale;
import org.junit.Test;

/**
 * Unit tests for FieldNamingPolicy helper methods and the built-in policies.
 * For end-to-end tests with Gson see FieldNamingTest.
 */
public class FieldNamingPolicyTest {

  private static final char UNDERSCORE = '_';
  // U+2170: SMALL ROMAN NUMERAL ONE; has an uppercase variant but is not a letter in Java sense
  private static final String ROMAN_NUMERAL_ONE = "\u2170";

  private static void assertSeparatesCamelCase(String input, String expected) {
    assertThat(FieldNamingPolicy.separateCamelCase(input, UNDERSCORE)).isEqualTo(expected);
  }

  private static void assertUpperCaseFirstLetter(String input, String expected) {
    assertThat(FieldNamingPolicy.upperCaseFirstLetter(input)).isEqualTo(expected);
  }

  @Test
  public void separateCamelCase_leavesNonCamelCaseStringsUnchanged() {
    assertSeparatesCamelCase("a", "a");
    assertSeparatesCamelCase("ab", "ab");
    assertSeparatesCamelCase("Ab", "Ab");   // already starts with upper-case
    assertSeparatesCamelCase("__", "__");   // only separators
    assertSeparatesCamelCase("_123", "_123"); // leading underscore + digits
  }

  @Test
  public void separateCamelCase_insertsSeparatorBetweenLowerToUpperTransition() {
    assertSeparatesCamelCase("aB", "a_B");
  }

  @Test
  public void separateCamelCase_splitsConsecutiveUppercaseLetters() {
    assertSeparatesCamelCase("AB", "A_B");
  }

  @Test
  public void separateCamelCase_preservesExistingSeparators() {
    assertSeparatesCamelCase("A_B", "A__B");
  }

  @Test
  public void separateCamelCase_splitsMultipleWords() {
    assertSeparatesCamelCase("firstSecondThird", "first_Second_Third");
  }

  @Test
  public void upperCaseFirstLetter_basicAsciiCases() {
    assertUpperCaseFirstLetter("a", "A");
    assertUpperCaseFirstLetter("ab", "Ab");
    assertUpperCaseFirstLetter("AB", "AB"); // already capitalized
  }

  @Test
  public void upperCaseFirstLetter_preservesLeadingUnderscoresAndNonLetters() {
    assertUpperCaseFirstLetter("_a", "_A");
    assertUpperCaseFirstLetter("_ab", "_Ab");
    assertUpperCaseFirstLetter("__", "__");
    assertUpperCaseFirstLetter("_1", "_1");
  }

  @Test
  public void upperCaseFirstLetter_nonLetterWithUppercaseVariant_isNotUppercased() {
    // See https://github.com/google/gson/issues/1965
    assertUpperCaseFirstLetter(ROMAN_NUMERAL_ONE, ROMAN_NUMERAL_ONE);
    assertUpperCaseFirstLetter("_" + ROMAN_NUMERAL_ONE, "_" + ROMAN_NUMERAL_ONE);
    assertUpperCaseFirstLetter(ROMAN_NUMERAL_ONE + "a", ROMAN_NUMERAL_ONE + "A");
  }

  /** Upper-casing policies should be unaffected by default Locale. */
  @Test
  public void upperCasingPolicies_ignoreDefaultLocale() throws Exception {
    class Dummy {
      @SuppressWarnings("unused")
      int i;
    }

    Field field = Dummy.class.getDeclaredField("i");
    String name = field.getName();

    FieldNamingPolicy[] policies = {
        FieldNamingPolicy.UPPER_CAMEL_CASE,
        FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES,
        FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES
    };

    // Expected transformation must use a fixed, locale-independent reference
    String expected = name.toUpperCase(Locale.ROOT);

    Locale original = Locale.getDefault();
    // Turkish has special case conversion rules (e.g., dotted/dotless i)
    Locale.setDefault(Locale.forLanguageTag("tr"));

    try {
      // Sanity-check that our test environment differs from ROOT for this name
      assertWithMessage("Test setup is broken")
          .that(name.toUpperCase(Locale.getDefault()))
          .isNotEqualTo(expected);

      for (FieldNamingPolicy policy : policies) {
        assertWithMessage("Unexpected conversion for %s", policy)
            .that(policy.translateName(field))
            .isEqualTo(expected);
      }
    } finally {
      Locale.setDefault(original);
    }
  }

  /** Lower-casing policies should be unaffected by default Locale. */
  @Test
  public void lowerCasingPolicies_ignoreDefaultLocale() throws Exception {
    class Dummy {
      @SuppressWarnings({"unused", "ConstantField"})
      int I;
    }

    Field field = Dummy.class.getDeclaredField("I");
    String name = field.getName();

    FieldNamingPolicy[] policies = {
        FieldNamingPolicy.LOWER_CASE_WITH_DASHES,
        FieldNamingPolicy.LOWER_CASE_WITH_DOTS,
        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
    };

    // Expected transformation must use a fixed, locale-independent reference
    String expected = name.toLowerCase(Locale.ROOT);

    Locale original = Locale.getDefault();
    // Turkish has special case conversion rules (e.g., dotted/dotless i)
    Locale.setDefault(Locale.forLanguageTag("tr"));

    try {
      // Sanity-check that our test environment differs from ROOT for this name
      assertWithMessage("Test setup is broken")
          .that(name.toLowerCase(Locale.getDefault()))
          .isNotEqualTo(expected);

      for (FieldNamingPolicy policy : policies) {
        assertWithMessage("Unexpected conversion for %s", policy)
            .that(policy.translateName(field))
            .isEqualTo(expected);
      }
    } finally {
      Locale.setDefault(original);
    }
  }
}