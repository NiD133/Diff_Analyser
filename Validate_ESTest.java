package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the Validate utility class.
 * Tests validation methods for null checks, boolean assertions, array validation, 
 * string validation, and failure scenarios.
 */
public class ValidateTest {

    // ===== NULL VALIDATION TESTS =====
    
    @Test(expected = IllegalArgumentException.class)
    public void notNull_withNullObject_throwsException() {
        Validate.notNull(null);
    }
    
    @Test
    public void notNull_withValidObject_passes() {
        String validString = "test";
        Validate.notNull(validString);
        // Test passes if no exception is thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void notNull_withNullObjectAndCustomMessage_throwsExceptionWithMessage() {
        Validate.notNull(null, "Custom error message");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void notNullParam_withNullObject_throwsExceptionWithParameterName() {
        Validate.notNullParam(null, "username");
    }
    
    @Test
    public void notNullParam_withValidObject_passes() {
        Validate.notNullParam("validValue", "paramName");
        // Test passes if no exception is thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void ensureNotNull_withNullObject_throwsException() {
        Validate.ensureNotNull(null);
    }
    
    @Test
    public void ensureNotNull_withValidObject_returnsObject() {
        String input = "test";
        Object result = Validate.ensureNotNull(input);
        assertEquals(input, result);
    }
    
    @Test
    public void ensureNotNull_withValidObjectAndMessage_returnsObject() {
        String input = "test";
        Object result = Validate.ensureNotNull(input, "Error message");
        assertEquals(input, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void ensureNotNull_withNullObjectAndMessage_throwsException() {
        Validate.ensureNotNull(null, "Object cannot be null");
    }

    // ===== BOOLEAN VALIDATION TESTS =====
    
    @Test
    public void isTrue_withTrueValue_passes() {
        Validate.isTrue(true);
        // Test passes if no exception is thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void isTrue_withFalseValue_throwsException() {
        Validate.isTrue(false);
    }
    
    @Test
    public void isTrue_withTrueValueAndMessage_passes() {
        Validate.isTrue(true, "Should be true");
        // Test passes if no exception is thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void isTrue_withFalseValueAndMessage_throwsExceptionWithMessage() {
        Validate.isTrue(false, "Value must be true");
    }
    
    @Test
    public void isFalse_withFalseValue_passes() {
        Validate.isFalse(false);
        // Test passes if no exception is thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void isFalse_withTrueValue_throwsException() {
        Validate.isFalse(true);
    }
    
    @Test
    public void isFalse_withFalseValueAndMessage_passes() {
        Validate.isFalse(false, "Should be false");
        // Test passes if no exception is thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void isFalse_withTrueValueAndMessage_throwsExceptionWithMessage() {
        Validate.isFalse(true, "Value must be false");
    }

    // ===== ARRAY VALIDATION TESTS =====
    
    @Test(expected = NullPointerException.class)
    public void noNullElements_withNullArray_throwsNullPointerException() {
        Validate.noNullElements((Object[]) null);
    }
    
    @Test(expected = NullPointerException.class)
    public void noNullElements_withNullArrayAndMessage_throwsNullPointerException() {
        Validate.noNullElements((Object[]) null, "Array cannot be null");
    }
    
    @Test
    public void noNullElements_withEmptyArray_passes() {
        Object[] emptyArray = new Object[0];
        Validate.noNullElements(emptyArray);
        // Test passes if no exception is thrown
    }
    
    @Test
    public void noNullElements_withValidElements_passes() {
        Object[] validArray = {"element1", "element2", "element3"};
        Validate.noNullElements(validArray);
        // Test passes if no exception is thrown
    }
    
    @Test
    public void noNullElements_withValidElementsAndMessage_passes() {
        Object[] validArray = {"element1"};
        Validate.noNullElements(validArray, "No nulls allowed");
        // Test passes if no exception is thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void noNullElements_withNullElement_throwsException() {
        Object[] arrayWithNull = {null};
        Validate.noNullElements(arrayWithNull);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void noNullElements_withNullElementAndMessage_throwsExceptionWithMessage() {
        Object[] arrayWithNull = {null};
        Validate.noNullElements(arrayWithNull, "Array contains null element");
    }

    // ===== STRING VALIDATION TESTS =====
    
    @Test(expected = IllegalArgumentException.class)
    public void notEmpty_withNullString_throwsException() {
        Validate.notEmpty((String) null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void notEmpty_withEmptyString_throwsException() {
        Validate.notEmpty("");
    }
    
    @Test
    public void notEmpty_withValidString_passes() {
        Validate.notEmpty("validString");
        // Test passes if no exception is thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void notEmpty_withNullStringAndMessage_throwsExceptionWithMessage() {
        Validate.notEmpty(null, "String cannot be null or empty");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void notEmpty_withEmptyStringAndMessage_throwsExceptionWithMessage() {
        Validate.notEmpty("", "String cannot be empty");
    }
    
    @Test
    public void notEmpty_withValidStringAndMessage_passes() {
        Validate.notEmpty("validString", "String must have content");
        // Test passes if no exception is thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void notEmptyParam_withNullString_throwsExceptionWithParameterName() {
        Validate.notEmptyParam(null, "username");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void notEmptyParam_withEmptyString_throwsExceptionWithParameterName() {
        Validate.notEmptyParam("", "password");
    }
    
    @Test
    public void notEmptyParam_withValidString_passes() {
        Validate.notEmptyParam("validValue", "paramName");
        // Test passes if no exception is thrown
    }

    // ===== FAILURE METHODS TESTS =====
    
    @Test(expected = IllegalArgumentException.class)
    public void fail_withMessage_throwsIllegalArgumentException() {
        Validate.fail("Test failure message");
    }
    
    @Test(expected = IllegalStateException.class)
    public void wtf_withMessage_throwsIllegalStateException() {
        Validate.wtf("Unexpected state reached");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void assertFail_withMessage_throwsIllegalArgumentException() {
        Validate.assertFail("Assertion failed");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void fail_withMessageAndArgs_throwsIllegalArgumentException() {
        Validate.fail("Simple message without formatting");
    }

    // ===== EDGE CASES AND ERROR HANDLING =====
    
    @Test(expected = NullPointerException.class)
    public void ensureNotNull_withNullMessage_throwsNullPointerException() {
        Object[] args = new Object[0];
        Validate.ensureNotNull(null, null, args);
    }
    
    @Test(expected = NullPointerException.class)
    public void fail_withNullMessage_throwsNullPointerException() {
        Object[] args = new Object[0];
        Validate.fail(null, args);
    }
}