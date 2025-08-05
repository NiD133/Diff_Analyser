package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.lang.reflect.Field;
import java.util.Locale;
import org.junit.Test;

/**
 * Unit tests for {@link FieldNamingPolicy}. These tests verify the behavior of field naming policies
 * and ensure they work correctly under different conditions.
 */
public class FieldNamingPolicyTest {

  /**
   * Tests the {@link FieldNamingPolicy#separateCamelCase} method to ensure it correctly separates
   * camel case words with underscores.
   */
  @Test
  public void testSeparateCamelCase() {
    // Map of input strings to expected output strings
    String[][] testCases = {
      {"a", "a"},
      {"ab", "ab"},
      {"Ab", "Ab"},
      {"aB", "a_B"},
      {"AB", "A_B"},
      {"A_B", "A__B"},
      {"firstSecondThird", "first_Second_Third"},
      {"__", "__"},
      {"_123", "_123"}
    };

    for (String[] testCase : testCases) {
      String input = testCase[0];
      String expectedOutput = testCase[1];
      assertThat(FieldNamingPolicy.separateCamelCase(input, '_')).isEqualTo(expectedOutput);
    }
  }

  /**
   * Tests the {@link FieldNamingPolicy#upperCaseFirstLetter} method to ensure it correctly
   * capitalizes the first letter of the input string.
   */
  @Test
  public void testUpperCaseFirstLetter() {
    // Map of input strings to expected output strings
    String[][] testCases = {
      {"a", "A"},
      {"ab", "Ab"},
      {"AB", "AB"},
      {"_a", "_A"},
      {"_ab", "_Ab"},
      {"__", "__"},
      {"_1", "_1"},
      // Special case: Unicode character with an uppercase variant
      {"\u2170", "\u2170"},
      {"_\u2170", "_\u2170"},
      {"\u2170a", "\u2170A"},
    };

    for (String[] testCase : testCases) {
      String input = testCase[0];
      String expectedOutput = testCase[1];
      assertThat(FieldNamingPolicy.upperCaseFirstLetter(input)).isEqualTo(expectedOutput);
    }
  }

  /**
   * Verifies that upper-casing policies are not affected by the default Locale.
   */
  @Test
  public void testUpperCasingLocaleIndependent() throws Exception {
    class Dummy {
      @SuppressWarnings("unused")
      int i;
    }

    FieldNamingPolicy[] upperCasingPolicies = {
      FieldNamingPolicy.UPPER_CAMEL_CASE,
      FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES,
      FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES,
    };

    Field field = Dummy.class.getDeclaredField("i");
    String fieldName = field.getName();
    String expectedUpperCaseName = fieldName.toUpperCase(Locale.ROOT);

    Locale originalLocale = Locale.getDefault();
    Locale turkishLocale = new Locale("tr");

    // Set Turkish locale to test locale independence
    Locale.setDefault(turkishLocale);

    try {
      // Ensure the test setup is correct by verifying locale-specific behavior
      assertWithMessage("Test setup is broken")
          .that(fieldName.toUpperCase(Locale.getDefault()))
          .doesNotMatch(expectedUpperCaseName);

      for (FieldNamingPolicy policy : upperCasingPolicies) {
        // Verify that the policy ignores the default locale
        assertWithMessage("Unexpected conversion for %s", policy)
            .that(policy.translateName(field))
            .matches(expectedUpperCaseName);
      }
    } finally {
      // Restore the original locale
      Locale.setDefault(originalLocale);
    }
  }

  /**
   * Verifies that lower-casing policies are not affected by the default Locale.
   */
  @Test
  public void testLowerCasingLocaleIndependent() throws Exception {
    class Dummy {
      @SuppressWarnings({"unused", "ConstantField"})
      int I;
    }

    FieldNamingPolicy[] lowerCasingPolicies = {
      FieldNamingPolicy.LOWER_CASE_WITH_DASHES,
      FieldNamingPolicy.LOWER_CASE_WITH_DOTS,
      FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES,
    };

    Field field = Dummy.class.getDeclaredField("I");
    String fieldName = field.getName();
    String expectedLowerCaseName = fieldName.toLowerCase(Locale.ROOT);

    Locale originalLocale = Locale.getDefault();
    Locale turkishLocale = new Locale("tr");

    // Set Turkish locale to test locale independence
    Locale.setDefault(turkishLocale);

    try {
      // Ensure the test setup is correct by verifying locale-specific behavior
      assertWithMessage("Test setup is broken")
          .that(fieldName.toLowerCase(Locale.getDefault()))
          .doesNotMatch(expectedLowerCaseName);

      for (FieldNamingPolicy policy : lowerCasingPolicies) {
        // Verify that the policy ignores the default locale
        assertWithMessage("Unexpected conversion for %s", policy)
            .that(policy.translateName(field))
            .matches(expectedLowerCaseName);
      }
    } finally {
      // Restore the original locale
      Locale.setDefault(originalLocale);
    }
  }
}