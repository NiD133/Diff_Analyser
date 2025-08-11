package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.FieldNamingPolicy;
import java.lang.reflect.Field;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the FieldNamingPolicy enum.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class FieldNamingPolicy_ESTest extends FieldNamingPolicy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testFieldNamingPolicyValues() throws Throwable {
        FieldNamingPolicy[] policies = FieldNamingPolicy.values();
        assertEquals("FieldNamingPolicy should have 7 values", 7, policies.length);
    }

    @Test(timeout = 4000)
    public void testSeparateCamelCaseEmptyString() throws Throwable {
        String result = FieldNamingPolicy.separateCamelCase("", 'T');
        assertEquals("Empty string should remain unchanged", "", result);
    }

    @Test(timeout = 4000)
    public void testUpperCaseFirstLetterNullInput() throws Throwable {
        try {
            FieldNamingPolicy.upperCaseFirstLetter(null);
            fail("Expected NullPointerException for null input");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy", e);
        }
    }

    @Test(timeout = 4000)
    public void testSeparateCamelCaseNullInput() throws Throwable {
        try {
            FieldNamingPolicy.separateCamelCase(null, 'I');
            fail("Expected NullPointerException for null input");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpperCaseFirstLetterWithSpecialCharacters() throws Throwable {
        String input = "cA:[IY:hB?-NT@IV/y";
        String result = FieldNamingPolicy.upperCaseFirstLetter(input);
        assertEquals("First letter should be capitalized", "CA:[IY:hB?-NT@IV/y", result);
    }

    @Test(timeout = 4000)
    public void testUpperCaseFirstLetterWithUpperCaseInput() throws Throwable {
        String input = "LOWER_CASE_WITH_UNDERSCORES";
        String result = FieldNamingPolicy.upperCaseFirstLetter(input);
        assertEquals("Input should remain unchanged", "LOWER_CASE_WITH_UNDERSCORES", result);
    }

    @Test(timeout = 4000)
    public void testUpperCaseFirstLetterWithLeadingQuote() throws Throwable {
        String input = "\"1+ejk5p_l;*";
        String result = FieldNamingPolicy.upperCaseFirstLetter(input);
        assertEquals("First letter after quote should be capitalized", "\"1+Ejk5p_l;*", result);
    }

    @Test(timeout = 4000)
    public void testUpperCaseFirstLetterEmptyString() throws Throwable {
        String result = FieldNamingPolicy.upperCaseFirstLetter("");
        assertEquals("Empty string should remain unchanged", "", result);
    }

    @Test(timeout = 4000)
    public void testSeparateCamelCaseWithUnderscores() throws Throwable {
        String input = "LOWER_CASE_WITH_UNDERSCORES";
        String result = FieldNamingPolicy.separateCamelCase(input, 'D');
        assertEquals("Camel case should be separated by 'D'", "LDODWDEDR_DCDADSDE_DWDIDTDH_DUDNDDDEDRDSDCDODRDEDS", result);
    }

    @Test(timeout = 4000)
    public void testTranslateNameWithNullFieldIdentityPolicy() throws Throwable {
        FieldNamingPolicy policy = FieldNamingPolicy.IDENTITY;
        try {
            policy.translateName(null);
            fail("Expected NullPointerException for null field");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$1", e);
        }
    }

    @Test(timeout = 4000)
    public void testTranslateNameWithNullFieldUpperCamelCasePolicy() throws Throwable {
        FieldNamingPolicy policy = FieldNamingPolicy.UPPER_CAMEL_CASE;
        try {
            policy.translateName(null);
            fail("Expected NullPointerException for null field");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$2", e);
        }
    }

    @Test(timeout = 4000)
    public void testTranslateNameWithNullFieldLowerCaseWithDotsPolicy() throws Throwable {
        FieldNamingPolicy policy = FieldNamingPolicy.LOWER_CASE_WITH_DOTS;
        try {
            policy.translateName(null);
            fail("Expected NullPointerException for null field");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$7", e);
        }
    }

    @Test(timeout = 4000)
    public void testTranslateNameWithNullFieldLowerCaseWithUnderscoresPolicy() throws Throwable {
        FieldNamingPolicy policy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
        try {
            policy.translateName(null);
            fail("Expected NullPointerException for null field");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$5", e);
        }
    }

    @Test(timeout = 4000)
    public void testTranslateNameWithNullFieldLowerCaseWithDashesPolicy() throws Throwable {
        FieldNamingPolicy policy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES;
        try {
            policy.translateName(null);
            fail("Expected NullPointerException for null field");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$6", e);
        }
    }

    @Test(timeout = 4000)
    public void testTranslateNameWithNullFieldUpperCamelCaseWithSpacesPolicy() throws Throwable {
        FieldNamingPolicy policy = FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES;
        try {
            policy.translateName(null);
            fail("Expected NullPointerException for null field");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$3", e);
        }
    }

    @Test(timeout = 4000)
    public void testTranslateNameWithNullFieldUpperCaseWithUnderscoresPolicy() throws Throwable {
        FieldNamingPolicy policy = FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES;
        try {
            policy.translateName(null);
            fail("Expected NullPointerException for null field");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$4", e);
        }
    }
}