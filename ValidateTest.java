package org.jsoup.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("deprecation") // keeps tests for ensureNotNull
public class ValidateTest {

    @Test
    public void notNull_withValidObject_shouldNotThrow() {
        // Given a non-null object
        String validObject = "foo";
        
        // When validating it's not null
        // Then no exception should be thrown
        assertDoesNotThrow(() -> Validate.notNull(validObject));
    }

    @Test
    public void notNull_withNullObject_shouldThrowIllegalArgumentException() {
        // Given a null object
        String nullObject = null;
        
        // When validating it's not null
        // Then IllegalArgumentException should be thrown
        assertThrows(IllegalArgumentException.class, () -> Validate.notNull(nullObject));
    }

    @Test
    void notNull_stacktraceFiltersOutValidateClass() {
        // When validation fails
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.notNull(null));
        
        // Then the exception should have the expected message
        assertEquals("Object must not be null", exception.getMessage());
        
        // And the stack trace should not contain the Validate class
        StackTraceElement[] stackTrace = exception.getStackTrace();
        for (StackTraceElement trace : stackTrace) {
            assertNotEquals(Validate.class.getName(), trace.getClassName());
        }
        assertTrue(stackTrace.length >= 1);
    }

    @Test
    void notNullParam_withNullObject_shouldThrowValidationExceptionWithParameterName() {
        // Given a null object and parameter name
        String parameterName = "foo";
        
        // When validating the parameter is not null
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.notNullParam(null, parameterName));
        
        // Then the exception message should include the parameter name
        assertEquals("The parameter 'foo' must not be null.", exception.getMessage());
    }

    @Test
    public void wtf_shouldThrowIllegalStateExceptionWithMessage() {
        // Given an error message
        String errorMessage = "Unexpected state reached";
        
        // When calling wtf
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> Validate.wtf(errorMessage));
        
        // Then the exception should contain the message
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void ensureNotNull_withValidObject_shouldReturnSameObject() {
        // Given a non-null object
        Object validObject = new Object();
        
        // When ensuring it's not null
        Object result = Validate.ensureNotNull(validObject);
        
        // Then the same object should be returned
        assertSame(validObject, result);
    }

    @Test
    public void ensureNotNull_withNullObject_shouldThrowValidationException() {
        // When ensuring a null object is not null
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.ensureNotNull(null));
        
        // Then the exception should have the default message
        assertEquals("Object must not be null", exception.getMessage());
    }

    @Test
    public void ensureNotNullWithMessage_withValidObject_shouldReturnSameObject() {
        // Given a non-null object and custom message
        Object validObject = new Object();
        String customMessage = "Object must not be null";
        
        // When ensuring it's not null with custom message
        Object result = Validate.ensureNotNull(validObject, customMessage);
        
        // Then the same object should be returned
        assertSame(validObject, result);
    }

    @Test
    public void ensureNotNullWithMessage_withNullObject_shouldThrowValidationExceptionWithCustomMessage() {
        // Given a custom error message
        String customMessage = "Custom error message";
        
        // When ensuring a null object is not null with custom message
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.ensureNotNull(null, customMessage));
        
        // Then the exception should contain the custom message
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    public void ensureNotNullWithFormattedMessage_withValidObject_shouldReturnSameObject() {
        // Given a non-null object and formatted message
        Object validObject = new Object();
        String messageFormat = "Object must not be null: %s";
        String additionalInfo = "additional info";
        
        // When ensuring it's not null with formatted message
        Object result = Validate.ensureNotNull(validObject, messageFormat, additionalInfo);
        
        // Then the same object should be returned
        assertSame(validObject, result);
    }

    @Test
    public void ensureNotNullWithFormattedMessage_withNullObject_shouldThrowValidationExceptionWithFormattedMessage() {
        // Given a message format and arguments
        String messageFormat = "Object must not be null: %s";
        String additionalInfo = "additional info";
        String expectedMessage = "Object must not be null: additional info";
        
        // When ensuring a null object is not null with formatted message
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.ensureNotNull(null, messageFormat, additionalInfo));
        
        // Then the exception should contain the formatted message
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void expectNotNull_withValidObject_shouldReturnSameObject() {
        // Given a non-null string
        String validString = "Foo";
        
        // When expecting it's not null
        String result = Validate.expectNotNull(validString);
        
        // Then the same object should be returned
        assertSame(validString, result);
    }

    @Test
    void expectNotNull_withNullObject_shouldThrowValidationException() {
        // Given a null string
        String nullString = null;
        
        // When expecting it's not null
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.expectNotNull(nullString));
        
        // Then the exception should have the default message
        assertEquals("Object must not be null", exception.getMessage());
    }

    @Test
    public void notNullParam_withValidObject_shouldNotThrow() {
        // Given a non-null object and parameter name
        Object validObject = new Object();
        String parameterName = "param";
        
        // When validating the parameter is not null
        // Then no exception should be thrown
        assertDoesNotThrow(() -> Validate.notNullParam(validObject, parameterName));
    }

    @Test
    public void notNullParam_withNullObject_shouldThrowValidationExceptionWithParameterMessage() {
        // Given a parameter name
        String parameterName = "param";
        
        // When validating a null parameter
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.notNullParam(null, parameterName));
        
        // Then the exception should include the parameter name in the message
        assertEquals("The parameter 'param' must not be null.", exception.getMessage());
    }

    @Test
    public void notEmpty_withNonEmptyString_shouldNotThrow() {
        // Given a non-empty string
        String nonEmptyString = "foo";
        
        // When validating it's not empty
        // Then no exception should be thrown
        assertDoesNotThrow(() -> Validate.notEmpty(nonEmptyString));
    }

    @Test
    public void notEmpty_withEmptyString_shouldThrowValidationException() {
        // Given an empty string
        String emptyString = "";
        
        // When validating it's not empty
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.notEmpty(emptyString));
        
        // Then the exception should have the expected message
        assertEquals("String must not be empty", exception.getMessage());
    }

    @Test
    public void notEmpty_withNullString_shouldThrowValidationException() {
        // Given a null string
        String nullString = null;
        
        // When validating it's not empty
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.notEmpty(nullString));
        
        // Then the exception should have the expected message
        assertEquals("String must not be empty", exception.getMessage());
    }

    @Test
    public void isTrue_withTrueValue_shouldNotThrow() {
        // Given a true value
        boolean trueValue = true;
        
        // When validating it's true
        // Then no exception should be thrown
        assertDoesNotThrow(() -> Validate.isTrue(trueValue));
    }

    @Test
    public void isTrue_withFalseValue_shouldThrowValidationException() {
        // Given a false value
        boolean falseValue = false;
        
        // When validating it's true
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.isTrue(falseValue));
        
        // Then the exception should have the expected message
        assertEquals("Must be true", exception.getMessage());
    }

    @Test
    public void isFalse_withFalseValue_shouldNotThrow() {
        // Given a false value
        boolean falseValue = false;
        
        // When validating it's false
        // Then no exception should be thrown
        assertDoesNotThrow(() -> Validate.isFalse(falseValue));
    }

    @Test
    public void isFalse_withTrueValue_shouldThrowValidationException() {
        // Given a true value
        boolean trueValue = true;
        
        // When validating it's false
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.isFalse(trueValue));
        
        // Then the exception should have the expected message
        assertEquals("Must be false", exception.getMessage());
    }

    @Test
    public void assertFail_shouldAlwaysThrowValidationException() {
        // Given an error message
        String errorMessage = "This should fail";
        
        // When calling assertFail
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.assertFail(errorMessage));
        
        // Then the exception should contain the message
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void notEmptyParam_withNonEmptyString_shouldNotThrow() {
        // Given a non-empty string and parameter name
        String nonEmptyString = "foo";
        String parameterName = "param";
        
        // When validating the parameter is not empty
        // Then no exception should be thrown
        assertDoesNotThrow(() -> Validate.notEmptyParam(nonEmptyString, parameterName));
    }

    @Test
    public void notEmptyParam_withEmptyString_shouldThrowValidationExceptionWithParameterMessage() {
        // Given an empty string and parameter name
        String emptyString = "";
        String parameterName = "param";
        
        // When validating the parameter is not empty
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.notEmptyParam(emptyString, parameterName));
        
        // Then the exception should include the parameter name in the message
        assertEquals("The 'param' parameter must not be empty.", exception.getMessage());
    }

    @Test
    public void notEmptyParam_withNullString_shouldThrowValidationExceptionWithParameterMessage() {
        // Given a null string and parameter name
        String nullString = null;
        String parameterName = "param";
        
        // When validating the parameter is not empty
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.notEmptyParam(nullString, parameterName));
        
        // Then the exception should include the parameter name in the message
        assertEquals("The 'param' parameter must not be empty.", exception.getMessage());
    }

    @Test
    public void noNullElementsWithMessage_withValidArray_shouldNotThrow() {
        // Given an array with no null elements
        Object[] validArray = {new Object(), new Object()};
        String customMessage = "Custom error message";
        
        // When validating no null elements with custom message
        // Then no exception should be thrown
        assertDoesNotThrow(() -> Validate.noNullElements(validArray, customMessage));
    }

    @Test
    public void noNullElementsWithMessage_withNullElement_shouldThrowValidationExceptionWithCustomMessage() {
        // Given an array containing a null element and custom message
        Object[] arrayWithNull = {new Object(), null};
        String customMessage = "Custom error message";
        
        // When validating no null elements with custom message
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.noNullElements(arrayWithNull, customMessage));
        
        // Then the exception should contain the custom message
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    public void notEmptyWithMessage_withNonEmptyString_shouldNotThrow() {
        // Given a non-empty string and custom message
        String nonEmptyString = "foo";
        String customMessage = "Custom error message";
        
        // When validating it's not empty with custom message
        // Then no exception should be thrown
        assertDoesNotThrow(() -> Validate.notEmpty(nonEmptyString, customMessage));
    }

    @Test
    public void notEmptyWithMessage_withEmptyString_shouldThrowValidationExceptionWithCustomMessage() {
        // Given an empty string and custom message
        String emptyString = "";
        String customMessage = "Custom error message";
        
        // When validating it's not empty with custom message
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.notEmpty(emptyString, customMessage));
        
        // Then the exception should contain the custom message
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    public void notEmptyWithMessage_withNullString_shouldThrowValidationExceptionWithCustomMessage() {
        // Given a null string and custom message
        String nullString = null;
        String customMessage = "Custom error message";
        
        // When validating it's not empty with custom message
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> Validate.notEmpty(nullString, customMessage));
        
        // Then the exception should contain the custom message
        assertEquals(customMessage, exception.getMessage());
    }
}