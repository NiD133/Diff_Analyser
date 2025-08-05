package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.FieldNamingPolicy;
import java.lang.reflect.Field;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class FieldNamingPolicy_ESTest extends FieldNamingPolicy_ESTest_scaffolding {

    // Tests for Enum values
    @Test(timeout = 4000)
    public void testEnumValues_count()  throws Throwable  {
        FieldNamingPolicy[] policies = FieldNamingPolicy.values();
        assertEquals(7, policies.length);
    }

    // Tests for separateCamelCase helper method
    @Test(timeout = 4000)
    public void testSeparateCamelCase_emptyString()  throws Throwable  {
        String result = FieldNamingPolicy.separateCamelCase("", 'T');
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testSeparateCamelCase_allUppercaseWithUnderscores()  throws Throwable  {
        String result = FieldNamingPolicy.separateCamelCase("LOWER_CASE_WITH_UNDERSCORES", 'D');
        assertEquals("LDODWDEDR_DCDADSDE_DWDIDTDH_DUDNDDDEDRDSDCDODRDEDS", result);
    }

    @Test(timeout = 4000)
    public void testSeparateCamelCase_nullInput_throwsNullPointerException()  throws Throwable  {
        try { 
            FieldNamingPolicy.separateCamelCase(null, 'I');
            fail("Expecting exception: NullPointerException");
        } catch(NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy", e);
        }
    }

    // Tests for upperCaseFirstLetter helper method
    @Test(timeout = 4000)
    public void testUpperCaseFirstLetter_startsWithLowercase()  throws Throwable  {
        String result = FieldNamingPolicy.upperCaseFirstLetter("cA:[IY:hB?-NT@IV/y");
        assertEquals("CA:[IY:hB?-NT@IV/y", result);
    }

    @Test(timeout = 4000)
    public void testUpperCaseFirstLetter_startsWithUppercase()  throws Throwable  {
        String result = FieldNamingPolicy.upperCaseFirstLetter("LOWER_CASE_WITH_UNDERSCORES");
        assertEquals("LOWER_CASE_WITH_UNDERSCORES", result);
    }

    @Test(timeout = 4000)
    public void testUpperCaseFirstLetter_nonAlphabeticFirstCharacter()  throws Throwable  {
        String result = FieldNamingPolicy.upperCaseFirstLetter("\"1+ejk5p_l;*");
        assertEquals("\"1+Ejk5p_l;*", result);
    }

    @Test(timeout = 4000)
    public void testUpperCaseFirstLetter_emptyString()  throws Throwable  {
        String result = FieldNamingPolicy.upperCaseFirstLetter("");
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testUpperCaseFirstLetter_nullInput_throwsNullPointerException()  throws Throwable  {
        try { 
            FieldNamingPolicy.upperCaseFirstLetter(null);
            fail("Expecting exception: NullPointerException");
        } catch(NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy", e);
        }
    }

    // Tests for translateName method null handling
    @Test(timeout = 4000)
    public void testIdentity_translateName_nullField_throwsNullPointerException()  throws Throwable  {
        FieldNamingPolicy policy = FieldNamingPolicy.IDENTITY;
        try { 
            policy.translateName(null);
            fail("Expecting exception: NullPointerException");
        } catch(NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$1", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpperCamelCase_translateName_nullField_throwsNullPointerException()  throws Throwable  {
        FieldNamingPolicy policy = FieldNamingPolicy.UPPER_CAMEL_CASE;
        try { 
            policy.translateName(null);
            fail("Expecting exception: NullPointerException");
        } catch(NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$2", e);
        }
    }

    @Test(timeout = 4000)
    public void testLowerCaseWithDots_translateName_nullField_throwsNullPointerException()  throws Throwable  {
        FieldNamingPolicy policy = FieldNamingPolicy.LOWER_CASE_WITH_DOTS;
        try { 
            policy.translateName(null);
            fail("Expecting exception: NullPointerException");
        } catch(NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$7", e);
        }
    }

    @Test(timeout = 4000)
    public void testLowerCaseWithUnderscores_translateName_nullField_throwsNullPointerException()  throws Throwable  {
        FieldNamingPolicy policy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
        try { 
            policy.translateName(null);
            fail("Expecting exception: NullPointerException");
        } catch(NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$5", e);
        }
    }

    @Test(timeout = 4000)
    public void testLowerCaseWithDashes_translateName_nullField_throwsNullPointerException()  throws Throwable  {
        FieldNamingPolicy policy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES;
        try { 
            policy.translateName(null);
            fail("Expecting exception: NullPointerException");
        } catch(NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$6", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpperCamelCaseWithSpaces_translateName_nullField_throwsNullPointerException()  throws Throwable  {
        FieldNamingPolicy policy = FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES;
        try { 
            policy.translateName(null);
            fail("Expecting exception: NullPointerException");
        } catch(NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$3", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpperCaseWithUnderscores_translateName_nullField_throwsNullPointerException()  throws Throwable  {
        FieldNamingPolicy policy = FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES;
        try { 
            policy.translateName(null);
            fail("Expecting exception: NullPointerException");
        } catch(NullPointerException e) {
            verifyException("com.google.gson.FieldNamingPolicy$4", e);
        }
    }
}