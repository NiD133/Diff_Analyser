package org.jsoup.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("deprecation")
class ValidateTest {

    // notNull tests
    @Test void notNull_withNonNullObject_doesNotThrow() {
        Validate.notNull("foo");
    }

    @Test void notNull_withNullObject_throwsValidationException() {
        assertThrows(ValidationException.class, () -> Validate.notNull(null));
    }

    @Test void notNullThrows_containsCleanStackTrace() {
        ValidationException e = assertThrows(ValidationException.class, 
            () -> Validate.notNull(null));
        
        assertEquals("Object must not be null", e.getMessage());
        StackTraceElement[] stackTrace = e.getStackTrace();
        assertTrue(stackTrace.length >= 1);
        for (StackTraceElement trace : stackTrace) {
            assertNotEquals(Validate.class.getName(), trace.getClassName());
        }
    }

    // notNullParam tests
    @Test void notNullParam_withNonNullObject_doesNotThrow() {
        Validate.notNullParam("valid", "param");
    }

    @Test void notNullParam_withNullObject_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.notNullParam(null, "param"));
        assertEquals("The parameter 'param' must not be null.", e.getMessage());
    }

    // wtf tests
    @Test void wtf_throwsIllegalStateException() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> Validate.wtf("Unexpected state reached"));
        assertEquals("Unexpected state reached", e.getMessage());
    }

    // ensureNotNull tests (deprecated)
    @Test void ensureNotNull_withNonNullObject_returnsObject() {
        Object obj = new Object();
        assertSame(obj, Validate.ensureNotNull(obj));
    }

    @Test void ensureNotNull_withNullObject_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.ensureNotNull(null));
        assertEquals("Object must not be null", e.getMessage());
    }

    @Test void ensureNotNullWithMessage_withNonNullObject_returnsObject() {
        Object obj = new Object();
        assertSame(obj, Validate.ensureNotNull(obj, "Message"));
    }

    @Test void ensureNotNullWithMessage_withNullObject_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.ensureNotNull(null, "Custom error message"));
        assertEquals("Custom error message", e.getMessage());
    }

    @Test void ensureNotNullWithFormattedMessage_withNonNullObject_returnsObject() {
        Object obj = new Object();
        assertSame(obj, Validate.ensureNotNull(obj, "Format: %s", "arg"));
    }

    @Test void ensureNotNullWithFormattedMessage_withNullObject_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.ensureNotNull(null, "Format: %s", "arg"));
        assertEquals("Format: arg", e.getMessage());
    }

    // expectNotNull tests
    @Test void expectNotNull_withNonNullObject_returnsObject() {
        String input = "test";
        String result = Validate.expectNotNull(input);
        assertSame(input, result);
    }

    @Test void expectNotNull_withNullObject_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.expectNotNull(null));
        assertEquals("Object must not be null", e.getMessage());
    }

    // notEmpty tests
    @Test void notEmpty_withNonEmptyString_doesNotThrow() {
        Validate.notEmpty("valid");
    }

    @Test void notEmpty_withEmptyString_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.notEmpty(""));
        assertEquals("String must not be empty", e.getMessage());
    }

    @Test void notEmpty_withNullString_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.notEmpty(null));
        assertEquals("String must not be empty", e.getMessage());
    }

    // isTrue tests
    @Test void isTrue_withTrue_doesNotThrow() {
        Validate.isTrue(true);
    }

    @Test void isTrue_withFalse_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.isTrue(false));
        assertEquals("Must be true", e.getMessage());
    }

    // isFalse tests
    @Test void isFalse_withFalse_doesNotThrow() {
        Validate.isFalse(false);
    }

    @Test void isFalse_withTrue_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.isFalse(true));
        assertEquals("Must be false", e.getMessage());
    }

    // assertFail tests
    @Test void assertFail_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.assertFail("Error message"));
        assertEquals("Error message", e.getMessage());
    }

    // notEmptyParam tests
    @Test void notEmptyParam_withNonEmptyString_doesNotThrow() {
        Validate.notEmptyParam("valid", "param");
    }

    @Test void notEmptyParam_withEmptyString_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.notEmptyParam("", "param"));
        assertEquals("The 'param' parameter must not be empty.", e.getMessage());
    }

    @Test void notEmptyParam_withNullString_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.notEmptyParam(null, "param"));
        assertEquals("The 'param' parameter must not be empty.", e.getMessage());
    }

    // noNullElements tests
    @Test void noNullElements_withValidArray_doesNotThrow() {
        Validate.noNullElements(new Object[]{1, "a"}, "Message");
    }

    @Test void noNullElements_withNullElement_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.noNullElements(new Object[]{1, null}, "Custom message"));
        assertEquals("Custom message", e.getMessage());
    }

    // notEmpty (custom message) tests
    @Test void notEmptyWithMessage_withNonEmptyString_doesNotThrow() {
        Validate.notEmpty("valid", "Message");
    }

    @Test void notEmptyWithMessage_withEmptyString_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.notEmpty("", "Custom message"));
        assertEquals("Custom message", e.getMessage());
    }

    @Test void notEmptyWithMessage_withNullString_throwsValidationException() {
        ValidationException e = assertThrows(ValidationException.class,
            () -> Validate.notEmpty(null, "Custom message"));
        assertEquals("Custom message", e.getMessage());
    }
}