package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.lang.reflect.Field;
import java.util.Locale;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("FieldNamingPolicy locale sensitivity")
class FieldNamingPolicyLocaleTest {

  /**
   * The Turkish locale is used because it has special casing rules for the letter 'i'.
   * Specifically, 'I' lower-cased is 'ı' (dotless i), which differs from the
   * locale-independent `Locale.ROOT` where 'I' becomes 'i'.
   */
  private static final Locale TURKISH_LOCALE = new Locale("tr");
  private static Locale originalDefaultLocale;

  @BeforeAll
  static void setLocaleToTurkish() {
    originalDefaultLocale = Locale.getDefault();
    Locale.setDefault(TURKISH_LOCALE);
  }

  @AfterAll
  static void restoreDefaultLocale() {
    Locale.setDefault(originalDefaultLocale);
  }

  /**
   * A dummy class with a field name chosen to expose locale-sensitive casing issues.
   */
  private static class ClassWithCapitalIField {
    @SuppressWarnings({"unused", "ConstantField"})
    int I;
  }

  @Test
  @DisplayName("Sanity check: Turkish locale should have distinct lower-casing rules")
  void turkishLocaleHasSpecialCasing() {
    // This test verifies that our test environment is correctly configured.
    // It confirms that the default (Turkish) locale's lower-casing of "I"
    // is different from the standard, locale-independent behavior.
    String original = "I";
    String expectedLocaleIndependent = "i"; // "I".toLowerCase(Locale.ROOT)

    assertThat(original.toLowerCase(Locale.getDefault()))
        .isNotEqualTo(expectedLocaleIndependent);
  }

  @ParameterizedTest
  @EnumSource(
      value = FieldNamingPolicy.class,
      names = {
        "LOWER_CASE_WITH_DASHES",
        "LOWER_CASE_WITH_DOTS",
        "LOWER_CASE_WITH_UNDERSCORES"
      })
  @DisplayName("Lower-casing policies should be locale-independent")
  void lowerCasePoliciesShouldBeLocaleIndependent(FieldNamingPolicy policy) throws Exception {
    // Arrange
    Field field = ClassWithCapitalIField.class.getDeclaredField("I");
    // The expected transformation should always use locale-independent rules,
    // resulting in "i", not the Turkish "ı".
    String expected = "i";

    // Act
    String actual = policy.translateName(field);

    // Assert
    assertWithMessage("Policy '%s' should ignore the default locale", policy)
        .that(actual)
        .isEqualTo(expected);
  }
}