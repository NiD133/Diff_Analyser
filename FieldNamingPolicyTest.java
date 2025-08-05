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
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.Test;

/**
 * Performs tests directly against {@link FieldNamingPolicy}; for integration tests see {@code
 * FieldNamingTest}.
 */
public class FieldNamingPolicyTest {

  /** Dummy class for locale-dependent tests. */
  private static class Dummy {
    @SuppressWarnings({"unused", "ConstantField"})
    int i; // for upper-casing tests

    @SuppressWarnings({"unused", "ConstantField"})
    int I; // for lower-casing tests
  }

  @Test
  public void testSeparateCamelCase() {
    // Using a Map makes the input -> expected relationship explicit.
    Map<String, String> testCases = new LinkedHashMap<>();
    testCases.put("a", "a");
    testCases.put("ab", "ab");
    testCases.put("Ab", "Ab");
    testCases.put("aB", "a_B");
    testCases.put("AB", "A_B");
    testCases.put("A_B", "A__B");
    testCases.put("firstSecondThird", "first_Second_Third");
    testCases.put("__", "__");
    testCases.put("_123", "_123");

    testCases.forEach(
        (input, expected) -> {
          String actual = FieldNamingPolicy.separateCamelCase(input, '_');
          assertWithMessage("Input: '%s'", input).that(actual).isEqualTo(expected);
        });
  }

  @Test
  public void testUpperCaseFirstLetter() {
    Map<String, String> testCases = new LinkedHashMap<>();
    testCases.put("a", "A");
    testCases.put("ab", "Ab");
    testCases.put("AB", "AB");
    testCases.put("_a", "_A");
    testCases.put("_ab", "_Ab");
    testCases.put("__", "__");
    testCases.put("_1", "_1");
    // Not a letter, but has uppercase variant (should not be uppercased)
    // See https://github.com/google/gson/issues/1965
    testCases.put("\u2170", "\u2170");
    testCases.put("_\u2170", "_\u2170");
    testCases.put("\u2170a", "\u2170A");

    testCases.forEach(
        (input, expected) -> {
          String actual = FieldNamingPolicy.upperCaseFirstLetter(input);
          assertWithMessage("Input: '%s'", input).that(actual).isEqualTo(expected);
        });
  }

  /** Upper-casing policies should be unaffected by default Locale. */
  @Test
  public void testUpperCasingLocaleIndependent() throws Exception {
    FieldNamingPolicy[] upperCasePolicies = {
      FieldNamingPolicy.UPPER_CAMEL_CASE,
      FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES,
      FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES,
    };
    // The field name 'i' has a special upper-casing rule in the Turkish locale
    assertCasingIsLocaleIndependent("i", upperCasePolicies, true);
  }

  /** Lower-casing policies should be unaffected by default Locale. */
  @Test
  public void testLowerCasingLocaleIndependent() throws Exception {
    FieldNamingPolicy[] lowerCasePolicies = {
      FieldNamingPolicy.LOWER_CASE_WITH_DASHES,
      FieldNamingPolicy.LOWER_CASE_WITH_DOTS,
      FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES,
    };
    // The field name 'I' has a special lower-casing rule in the Turkish locale
    assertCasingIsLocaleIndependent("I", lowerCasePolicies, false);
  }

  /**
   * Helper method to verify that field naming policies perform case conversions using {@link
   * Locale#ROOT}, ignoring the system's default locale.
   *
   * @param fieldName the name of the field in the {@link Dummy} class to test
   * @param policies the naming policies to verify
   * @param testUpperCase if true, tests upper-casing; otherwise, tests lower-casing
   */
  private void assertCasingIsLocaleIndependent(
      String fieldName, FieldNamingPolicy[] policies, boolean testUpperCase) throws Exception {
    Field field = Dummy.class.getDeclaredField(fieldName);
    String name = field.getName();

    String expectedResult =
        testUpperCase ? name.toUpperCase(Locale.ROOT) : name.toLowerCase(Locale.ROOT);

    Locale originalLocale = Locale.getDefault();
    // Set Turkish as the default Locale, which has special case conversion rules for 'i' and 'I'.
    Locale.setDefault(new Locale("tr"));
    try {
      String actualResultWithDefaultLocale =
          testUpperCase ? name.toUpperCase(Locale.getDefault()) : name.toLowerCase(Locale.getDefault());

      // Sanity check: Verify that the default locale's conversion is different from ROOT's.
      // This ensures our test environment is set up correctly.
      assertWithMessage(
              "Test setup is broken: Casing for '%s' in default locale '%s' should differ from ROOT",
              name, Locale.getDefault())
          .that(actualResultWithDefaultLocale)
          .isNotEqualTo(expectedResult);

      for (FieldNamingPolicy policy : policies) {
        // Verify that the policy's translation uses ROOT locale behavior, ignoring the default.
        assertWithMessage("Policy '%s' should be locale-independent", policy)
            .that(policy.translateName(field))
            .isEqualTo(expectedResult);
      }
    } finally {
      Locale.setDefault(originalLocale);
    }
  }
}