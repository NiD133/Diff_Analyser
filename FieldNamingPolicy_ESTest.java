package com.google.gson;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Readable, intention-revealing tests for FieldNamingPolicy.
 *
 * These tests focus on:
 * - Behavior of the helper methods separateCamelCase and upperCaseFirstLetter
 * - End-to-end translation of real Field objects for each naming policy
 * - Clear input/output examples rather than opaque data
 */
public class FieldNamingPolicyReadableTest {

  // Simple fixture with representative field names
  private static class Sample {
    String someFieldName;
    String _someFieldName;
    String aURL;
  }

  // Helper to translate a field by name using a specific policy
  private static String translate(FieldNamingPolicy policy, String fieldName) {
    try {
      Field f = Sample.class.getDeclaredField(fieldName);
      return policy.translateName(f);
    } catch (NoSuchFieldException e) {
      throw new AssertionError("Test setup error: field not found: " + fieldName, e);
    }
  }

  // -------- Helper method: separateCamelCase --------

  @Test
  public void separateCamelCase_insertsSeparatorBetweenWords() {
    assertEquals("some Field Name",
        FieldNamingPolicy.separateCamelCase("someFieldName", ' '));

    assertEquals("a U R L",
        FieldNamingPolicy.separateCamelCase("aURL", ' '));
  }

  @Test
  public void separateCamelCase_emptyString_returnsEmpty() {
    assertEquals("",
        FieldNamingPolicy.separateCamelCase("", ' '));
  }

  @Test
  public void separateCamelCase_null_throwsNpe() {
    assertThrows(NullPointerException.class,
        () -> FieldNamingPolicy.separateCamelCase(null, ' '));
  }

  // -------- Helper method: upperCaseFirstLetter --------

  @Test
  public void upperCaseFirstLetter_capitalizesFirstAlphabeticChar() {
    // Starts with non-letters, first letter is 'e'
    String input = "\"1+ejk5p_l;*";
    String expected = "\"1+Ejk5p_l;*";
    assertEquals(expected, FieldNamingPolicy.upperCaseFirstLetter(input));
  }

  @Test
  public void upperCaseFirstLetter_emptyString_returnsEmpty() {
    assertEquals("", FieldNamingPolicy.upperCaseFirstLetter(""));
  }

  @Test
  public void upperCaseFirstLetter_alreadyUppercase_noChange() {
    assertEquals("AlreadyUpper",
        FieldNamingPolicy.upperCaseFirstLetter("AlreadyUpper"));
  }

  @Test
  public void upperCaseFirstLetter_null_throwsNpe() {
    assertThrows(NullPointerException.class,
        () -> FieldNamingPolicy.upperCaseFirstLetter(null));
  }

  // -------- translateName: defensive checks --------

  @Test
  public void translateName_nullField_throwsNpe_forAllPolicies() {
    for (FieldNamingPolicy policy : FieldNamingPolicy.values()) {
      assertThrows(policy + " should throw NPE on null Field",
          NullPointerException.class,
          () -> policy.translateName(null));
    }
  }

  // -------- translateName: IDENTITY --------

  @Test
  public void identity_leavesNameUnchanged() {
    FieldNamingPolicy p = FieldNamingPolicy.IDENTITY;
    assertEquals("someFieldName", translate(p, "someFieldName"));
    assertEquals("_someFieldName", translate(p, "_someFieldName"));
    assertEquals("aURL", translate(p, "aURL"));
  }

  // -------- translateName: UPPER_CAMEL_CASE --------

  @Test
  public void upperCamelCase_capitalizesFirstLetter_preservesLeadingUnderscore() {
    FieldNamingPolicy p = FieldNamingPolicy.UPPER_CAMEL_CASE;
    assertEquals("SomeFieldName", translate(p, "someFieldName"));
    assertEquals("_SomeFieldName", translate(p, "_someFieldName"));
    assertEquals("AURL", translate(p, "aURL"));
  }

  // -------- translateName: UPPER_CAMEL_CASE_WITH_SPACES --------

  @Test
  public void upperCamelCaseWithSpaces_insertsSpaces_andCapitalizesFirstLetter() {
    FieldNamingPolicy p = FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES;
    assertEquals("Some Field Name", translate(p, "someFieldName"));
    assertEquals("_Some Field Name", translate(p, "_someFieldName"));
    assertEquals("A U R L", translate(p, "aURL"));
  }

  // -------- translateName: UPPER_CASE_WITH_UNDERSCORES --------

  @Test
  public void upperCaseWithUnderscores_convertsToUppercaseAndSeparatesWords() {
    FieldNamingPolicy p = FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES;
    assertEquals("SOME_FIELD_NAME", translate(p, "someFieldName"));
    assertEquals("_SOME_FIELD_NAME", translate(p, "_someFieldName"));
    assertEquals("A_U_R_L", translate(p, "aURL"));
  }

  // -------- translateName: LOWER_CASE_WITH_UNDERSCORES --------

  @Test
  public void lowerCaseWithUnderscores_convertsToLowercaseAndSeparatesWords() {
    FieldNamingPolicy p = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
    assertEquals("some_field_name", translate(p, "someFieldName"));
    assertEquals("_some_field_name", translate(p, "_someFieldName"));
    assertEquals("a_u_r_l", translate(p, "aURL"));
  }

  // -------- translateName: LOWER_CASE_WITH_DASHES --------

  @Test
  public void lowerCaseWithDashes_convertsToLowercaseAndUsesDashes() {
    FieldNamingPolicy p = FieldNamingPolicy.LOWER_CASE_WITH_DASHES;
    assertEquals("some-field-name", translate(p, "someFieldName"));
    assertEquals("_some-field-name", translate(p, "_someFieldName"));
    assertEquals("a-u-r-l", translate(p, "aURL"));
  }

  // -------- translateName: LOWER_CASE_WITH_DOTS --------

  @Test
  public void lowerCaseWithDots_convertsToLowercaseAndUsesDots() {
    FieldNamingPolicy p = FieldNamingPolicy.LOWER_CASE_WITH_DOTS;
    assertEquals("some.field.name", translate(p, "someFieldName"));
    assertEquals("_some.field.name", translate(p, "_someFieldName"));
    assertEquals("a.u.r.l", translate(p, "aURL"));
  }
}