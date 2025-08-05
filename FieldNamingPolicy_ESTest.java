package com.google.gson;

import org.junit.Test;
import java.lang.reflect.Field;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link FieldNamingPolicy} enum, focusing on the correctness
 * of its field name translation strategies.
 */
public class FieldNamingPolicyTest {

    /** A helper class with fields of different styles for testing naming policies. */
    private static class TestClass {
        String someFieldName;
        String _someFieldNameWithLeadingUnderscore;
        String aURL; // An example with an acronym
        String single;
    }

    // --- Tests for translateName() ---

    @Test
    public void translateName_identity_shouldReturnFieldNameUnchanged() throws Exception {
        Field field = TestClass.class.getDeclaredField("someFieldName");
        assertEquals("someFieldName", FieldNamingPolicy.IDENTITY.translateName(field));
    }

    @Test
    public void translateName_upperCamelCase_shouldCapitalizeFirstLetter() throws Exception {
        Field field1 = TestClass.class.getDeclaredField("someFieldName");
        assertEquals("SomeFieldName", FieldNamingPolicy.UPPER_CAMEL_CASE.translateName(field1));

        Field field2 = TestClass.class.getDeclaredField("_someFieldNameWithLeadingUnderscore");
        assertEquals("_SomeFieldNameWithLeadingUnderscore", FieldNamingPolicy.UPPER_CAMEL_CASE.translateName(field2));
    }

    @Test
    public void translateName_upperCamelCaseWithSpaces_shouldSeparateWordsAndCapitalize() throws Exception {
        Field field = TestClass.class.getDeclaredField("someFieldName");
        assertEquals("Some Field Name", FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES.translateName(field));
    }

    @Test
    public void translateName_upperCaseWithUnderscores_shouldUseUnderscoresAndUppercase() throws Exception {
        Field field1 = TestClass.class.getDeclaredField("someFieldName");
        assertEquals("SOME_FIELD_NAME", FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES.translateName(field1));

        Field field2 = TestClass.class.getDeclaredField("aURL");
        assertEquals("A_U_R_L", FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES.translateName(field2));
    }

    @Test
    public void translateName_lowerCaseWithUnderscores_shouldUseUnderscoresAndLowercase() throws Exception {
        Field field1 = TestClass.class.getDeclaredField("someFieldName");
        assertEquals("some_field_name", FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES.translateName(field1));

        Field field2 = TestClass.class.getDeclaredField("aURL");
        assertEquals("a_u_r_l", FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES.translateName(field2));
    }

    @Test
    public void translateName_lowerCaseWithDashes_shouldUseDashesAndLowercase() throws Exception {
        Field field1 = TestClass.class.getDeclaredField("someFieldName");
        assertEquals("some-field-name", FieldNamingPolicy.LOWER_CASE_WITH_DASHES.translateName(field1));

        Field field2 = TestClass.class.getDeclaredField("aURL");
        assertEquals("a-u-r-l", FieldNamingPolicy.LOWER_CASE_WITH_DASHES.translateName(field2));
    }

    @Test
    public void translateName_lowerCaseWithDots_shouldUseDotsAndLowercase() throws Exception {
        Field field1 = TestClass.class.getDeclaredField("someFieldName");
        assertEquals("some.field.name", FieldNamingPolicy.LOWER_CASE_WITH_DOTS.translateName(field1));

        Field field2 = TestClass.class.getDeclaredField("aURL");
        assertEquals("a.u.r.l", FieldNamingPolicy.LOWER_CASE_WITH_DOTS.translateName(field2));
    }

    @Test
    public void translateName_shouldThrowNullPointerException_whenFieldIsNull() {
        for (FieldNamingPolicy policy : FieldNamingPolicy.values()) {
            try {
                policy.translateName(null);
                fail("Expected NullPointerException for policy: " + policy);
            } catch (NullPointerException expected) {
                // This is the expected behavior.
            }
        }
    }

    // --- Tests for static helper methods ---

    @Test
    public void upperCaseFirstLetter_shouldCapitalizeFirstLetterOfWord() {
        assertEquals("Test", FieldNamingPolicy.upperCaseFirstLetter("test"));
        assertEquals(" Test", FieldNamingPolicy.upperCaseFirstLetter(" test"));
    }

    @Test
    public void upperCaseFirstLetter_shouldNotChangeString_whenFirstLetterIsAlreadyCapitalized() {
        assertEquals("Test", FieldNamingPolicy.upperCaseFirstLetter("Test"));
    }

    @Test
    public void upperCaseFirstLetter_shouldHandleStringsStartingWithNonLetters() {
        assertEquals("_Test", FieldNamingPolicy.upperCaseFirstLetter("_test"));
        assertEquals("1Test", FieldNamingPolicy.upperCaseFirstLetter("1test"));
    }

    @Test
    public void upperCaseFirstLetter_shouldReturnEmptyString_whenInputIsEmpty() {
        assertEquals("", FieldNamingPolicy.upperCaseFirstLetter(""));
    }

    @Test(expected = NullPointerException.class)
    public void upperCaseFirstLetter_shouldThrowNullPointerException_whenInputIsNull() {
        FieldNamingPolicy.upperCaseFirstLetter(null);
    }

    @Test
    public void separateCamelCase_shouldSeparateWordsWithGivenCharacter() {
        assertEquals("some_Field_Name", FieldNamingPolicy.separateCamelCase("someFieldName", '_'));
        assertEquals("a_U_R_L", FieldNamingPolicy.separateCamelCase("aURL", '_'));
    }

    @Test
    public void separateCamelCase_shouldReturnEmptyString_whenInputIsEmpty() {
        assertEquals("", FieldNamingPolicy.separateCamelCase("", '_'));
    }

    @Test(expected = NullPointerException.class)
    public void separateCamelCase_shouldThrowNullPointerException_whenInputIsNull() {
        FieldNamingPolicy.separateCamelCase(null, '_');
    }

    // --- General Enum tests ---

    @Test
    public void values_shouldReturnAllExpectedPolicies() {
        // This test confirms the expected number of policies.
        // A change in the number of policies may require an update to tests.
        assertEquals(7, FieldNamingPolicy.values().length);
    }
}