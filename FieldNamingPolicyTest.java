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
 * Unit tests for {@link FieldNamingPolicy} utility methods.
 * For integration tests with actual field naming transformations, see {@code FieldNamingTest}.
 */
public class FieldNamingPolicyTest {

  @Test
  public void separateCamelCase_shouldInsertSeparatorBeforeCapitalLetters() {
    // Test cases: input -> expected output with underscore separator
    TestCase[] testCases = {
        new TestCase("a", "a", "single lowercase letter"),
        new TestCase("ab", "ab", "multiple lowercase letters"),
        new TestCase("Ab", "Ab", "capital first letter"),
        new TestCase("aB", "a_B", "camelCase with one capital"),
        new TestCase("AB", "A_B", "all capitals"),
        new TestCase("A_B", "A__B", "existing underscore should be preserved"),
        new TestCase("firstSecondThird", "first_Second_Third", "multiple camelCase words"),
        new TestCase("__", "__", "only underscores"),
        new TestCase("_123", "_123", "underscore with numbers")
    };

    for (TestCase testCase : testCases) {
      String actualResult = FieldNamingPolicy.separateCamelCase(testCase.input, '_');
      assertWithMessage("Failed for case: %s", testCase.description)
          .that(actualResult)
          .isEqualTo(testCase.expectedOutput);
    }
  }

  @Test
  public void upperCaseFirstLetter_shouldCapitalizeOnlyFirstLetter() {
    TestCase[] testCases = {
        new TestCase("a", "A", "single lowercase letter"),
        new TestCase("ab", "Ab", "multiple lowercase letters"),
        new TestCase("AB", "AB", "already uppercase"),
        new TestCase("_a", "_A", "underscore prefix with lowercase"),
        new TestCase("_ab", "_Ab", "underscore prefix with multiple letters"),
        new TestCase("__", "__", "only underscores"),
        new TestCase("_1", "_1", "underscore with number"),
        // Unicode test case: Roman numeral that has uppercase variant but shouldn't be changed
        // See https://github.com/google/gson/issues/1965
        new TestCase("\u2170", "\u2170", "Unicode character that looks like letter but isn't"),
        new TestCase("_\u2170", "_\u2170", "underscore with Unicode character"),
        new TestCase("\u2170a", "\u2170A", "Unicode character followed by letter")
    };

    for (TestCase testCase : testCases) {
      String actualResult = FieldNamingPolicy.upperCaseFirstLetter(testCase.input);
      assertWithMessage("Failed for case: %s", testCase.description)
          .that(actualResult)
          .isEqualTo(testCase.expectedOutput);
    }
  }

  @Test
  public void upperCasingPolicies_shouldIgnoreDefaultLocale() throws Exception {
    // Arrange: Create test field and policies that perform uppercasing
    Field testField = createTestFieldWithName("i");
    FieldNamingPolicy[] upperCasingPolicies = {
        FieldNamingPolicy.UPPER_CAMEL_CASE,
        FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES,
        FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES,
    };

    String fieldName = testField.getName();
    String expectedUppercaseResult = fieldName.toUpperCase(Locale.ROOT);

    // Act & Assert: Test with Turkish locale (which has special case conversion rules)
    testWithTurkishLocale(() -> {
      verifyLocaleIndependentBehavior(fieldName, expectedUppercaseResult);
      
      for (FieldNamingPolicy policy : upperCasingPolicies) {
        String actualResult = policy.translateName(testField);
        assertWithMessage("Policy %s should ignore default locale for uppercasing", policy)
            .that(actualResult)
            .matches(expectedUppercaseResult);
      }
    });
  }

  @Test
  public void lowerCasingPolicies_shouldIgnoreDefaultLocale() throws Exception {
    // Arrange: Create test field and policies that perform lowercasing
    Field testField = createTestFieldWithName("I");
    FieldNamingPolicy[] lowerCasingPolicies = {
        FieldNamingPolicy.LOWER_CASE_WITH_DASHES,
        FieldNamingPolicy.LOWER_CASE_WITH_DOTS,
        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES,
    };

    String fieldName = testField.getName();
    String expectedLowercaseResult = fieldName.toLowerCase(Locale.ROOT);

    // Act & Assert: Test with Turkish locale (which has special case conversion rules)
    testWithTurkishLocale(() -> {
      verifyLocaleIndependentBehavior(fieldName, expectedLowercaseResult);
      
      for (FieldNamingPolicy policy : lowerCasingPolicies) {
        String actualResult = policy.translateName(testField);
        assertWithMessage("Policy %s should ignore default locale for lowercasing", policy)
            .that(actualResult)
            .matches(expectedLowercaseResult);
      }
    });
  }

  // Helper methods for better readability

  private static class TestCase {
    final String input;
    final String expectedOutput;
    final String description;

    TestCase(String input, String expectedOutput, String description) {
      this.input = input;
      this.expectedOutput = expectedOutput;
      this.description = description;
    }
  }

  private Field createTestFieldWithName(String fieldName) throws NoSuchFieldException {
    // Create anonymous class with the desired field name
    class TestClass {
      @SuppressWarnings({"unused", "ConstantField"})
      int i; // lowercase field name
      
      @SuppressWarnings({"unused", "ConstantField"})
      int I; // uppercase field name
    }
    
    return TestClass.class.getDeclaredField(fieldName);
  }

  private void testWithTurkishLocale(Runnable testCode) {
    Locale originalLocale = Locale.getDefault();
    try {
      // Turkish locale has special case conversion rules (i ↔ İ, I ↔ ı)
      Locale.setDefault(new Locale("tr"));
      testCode.run();
    } finally {
      Locale.setDefault(originalLocale);
    }
  }

  private void verifyLocaleIndependentBehavior(String fieldName, String expectedResult) {
    // Verify that Turkish locale actually produces different results
    // This ensures our test setup is correct
    String localeSpecificResult = isUppercaseTest(expectedResult) 
        ? fieldName.toUpperCase(Locale.getDefault())
        : fieldName.toLowerCase(Locale.getDefault());
        
    assertWithMessage("Test setup error: Turkish locale should produce different case conversion")
        .that(localeSpecificResult)
        .doesNotMatch(expectedResult);
  }

  private boolean isUppercaseTest(String expectedResult) {
    // Simple heuristic: if expected result equals the uppercase version, it's an uppercase test
    return expectedResult.equals(expectedResult.toUpperCase(Locale.ROOT));
  }
}