package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.lang.reflect.Field;
import java.util.Locale;
import org.junit.Test;

/**
 * Tests for {@link FieldNamingPolicy} focusing on locale-sensitive behavior.
 */
public class FieldNamingPolicyLocaleTest {

  private static final Locale TURKISH = new Locale("tr");
  private static final Field DUMMY_FIELD_I = getField(Dummy.class, "i");

  private static Field getField(Class<?> clazz, String fieldName) {
    try {
      return clazz.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      // This is a test setup error, so we fail fast.
      throw new AssertionError("Test setup failed: " + e.getMessage(), e);
    }
  }

  private static class Dummy {
    @SuppressWarnings("unused")
    int i;
  }

  /**
   * The Turkish language has special casing rules for the letter 'i'.
   * This test verifies that the upper-casing policies in {@link FieldNamingPolicy}
   * are not affected by the default {@link Locale}, and instead behave as if
   * {@link Locale#ROOT} is used for casing.
   */
  @Test
  public void upperCasePolicies_shouldBeLocaleIndependent() {
    // Arrange
    FieldNamingPolicy[] upperCasePolicies = {
      FieldNamingPolicy.UPPER_CAMEL_CASE,
      FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES,
      FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES
    };

    // For the field "i", the locale-independent upper-case is "I".
    String expectedTranslation = "I";
    String fieldName = DUMMY_FIELD_I.getName();

    Locale originalDefaultLocale = Locale.getDefault();
    Locale.setDefault(TURKISH);

    try {
      // Sanity check: In Turkish, the upper-case of "i" is "İ".
      // This confirms our test environment is set up correctly to detect locale-sensitive issues.
      assertWithMessage("Test setup assumption is incorrect: Turkish locale's toUpperCase behavior for 'i' has changed.")
          .that(fieldName.toUpperCase(Locale.getDefault()))
          .isNotEqualTo(expectedTranslation);

      // Act & Assert
      for (FieldNamingPolicy policy : upperCasePolicies) {
        String actualTranslation = policy.translateName(DUMMY_FIELD_I);

        // Verify that the policy's translation is locale-independent.
        assertWithMessage("Policy '%s' should produce a locale-independent result", policy)
            .that(actualTranslation)
            .isEqualTo(expectedTranslation);
      }
    } finally {
      // Cleanup: Restore the original locale to avoid side effects on other tests.
      Locale.setDefault(originalDefaultLocale);
    }
  }
}