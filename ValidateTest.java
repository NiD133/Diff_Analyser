package org.jsoup.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateTest {

    @Nested
    @DisplayName("notNull(Object)")
    class NotNullTests {
        @Test
        @DisplayName("should not throw an exception for a non-null object")
        void withNonNullInput_doesNotThrow() {
            assertDoesNotThrow(() -> Validate.notNull("foo"));
        }

        @Test
        @DisplayName("should throw ValidationException for a null object")
        void withNullInput_throwsValidationException() {
            // The original test caught IllegalArgumentException. ValidationException is a subclass,
            // so we test for the more specific type that is actually thrown.
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.notNull(null));
            assertEquals("Object must not be null", e.getMessage());
        }

        @Test
        @DisplayName("thrown exception stack trace should not contain the Validate class")
        void withNullInput_hasFilteredStackTrace() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.notNull(null));

            assertTrue(e.getStackTrace().length > 0, "Stack trace should not be empty");
            for (StackTraceElement trace : e.getStackTrace()) {
                assertNotEquals(Validate.class.getName(), trace.getClassName(), "Stack trace should be filtered");
            }
        }
    }

    @Nested
    @DisplayName("notNullParam(Object, String)")
    class NotNullParamTests {
        @Test
        @DisplayName("should not throw for a non-null parameter")
        void withNonNullInput_doesNotThrow() {
            assertDoesNotThrow(() -> Validate.notNullParam(new Object(), "param"));
        }

        @Test
        @DisplayName("should throw ValidationException for a null parameter")
        void withNullInput_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.notNullParam(null, "param"));
            assertEquals("The parameter 'param' must not be null.", e.getMessage());
        }
    }

    @Nested
    @DisplayName("notEmpty(String)")
    class NotEmptyTests {
        @Test
        @DisplayName("should not throw for a non-empty string")
        void withNonEmptyString_doesNotThrow() {
            assertDoesNotThrow(() -> Validate.notEmpty("foo"));
        }

        @Test
        @DisplayName("should throw ValidationException for an empty string")
        void withEmptyString_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.notEmpty(""));
            assertEquals("String must not be empty", e.getMessage());
        }

        @Test
        @DisplayName("should throw ValidationException for a null string")
        void withNullString_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.notEmpty(null));
            assertEquals("String must not be empty", e.getMessage());
        }
    }

    @Nested
    @DisplayName("notEmpty(String, String)")
    class NotEmptyWithMessageTests {
        @Test
        @DisplayName("should not throw for a non-empty string")
        void withNonEmptyString_doesNotThrow() {
            assertDoesNotThrow(() -> Validate.notEmpty("foo", "Custom error message"));
        }

        @Test
        @DisplayName("should throw ValidationException with custom message for an empty string")
        void withEmptyString_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.notEmpty("", "Custom error message"));
            assertEquals("Custom error message", e.getMessage());
        }

        @Test
        @DisplayName("should throw ValidationException with custom message for a null string")
        void withNullString_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.notEmpty(null, "Custom error message"));
            assertEquals("Custom error message", e.getMessage());
        }
    }

    @Nested
    @DisplayName("notEmptyParam(String, String)")
    class NotEmptyParamTests {
        @Test
        @DisplayName("should not throw for a non-empty parameter")
        void withNonEmptyParam_doesNotThrow() {
            assertDoesNotThrow(() -> Validate.notEmptyParam("foo", "param"));
        }

        @Test
        @DisplayName("should throw ValidationException for an empty parameter")
        void withEmptyParam_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.notEmptyParam("", "param"));
            assertEquals("The 'param' parameter must not be empty.", e.getMessage());
        }

        @Test
        @DisplayName("should throw ValidationException for a null parameter")
        void withNullParam_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.notEmptyParam(null, "param"));
            assertEquals("The 'param' parameter must not be empty.", e.getMessage());
        }
    }

    @Nested
    @DisplayName("Boolean Condition Checks")
    class BooleanConditionTests {
        @Test
        @DisplayName("isTrue should not throw for a true value")
        void isTrue_withTrue_doesNotThrow() {
            assertDoesNotThrow(() -> Validate.isTrue(true));
        }

        @Test
        @DisplayName("isTrue should throw ValidationException for a false value")
        void isTrue_withFalse_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.isTrue(false));
            assertEquals("Must be true", e.getMessage());
        }

        @Test
        @DisplayName("isFalse should not throw for a false value")
        void isFalse_withFalse_doesNotThrow() {
            assertDoesNotThrow(() -> Validate.isFalse(false));
        }

        @Test
        @DisplayName("isFalse should throw ValidationException for a true value")
        void isFalse_withTrue_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.isFalse(true));
            assertEquals("Must be false", e.getMessage());
        }
    }

    @Nested
    @DisplayName("noNullElements(Object[], String)")
    class NoNullElementsTests {
        @Test
        @DisplayName("should not throw for an array with no null elements")
        void withNoNulls_doesNotThrow() {
            Object[] array = {new Object(), new Object()};
            assertDoesNotThrow(() -> Validate.noNullElements(array, "Custom error message"));
        }

        @Test
        @DisplayName("should throw ValidationException for an array with a null element")
        void withNullElement_throwsValidationException() {
            Object[] arrayWithNull = {new Object(), null};
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.noNullElements(arrayWithNull, "Custom error message"));
            assertEquals("Custom error message", e.getMessage());
        }
    }

    @Nested
    @DisplayName("expectNotNull(T)")
    class ExpectNotNullTests {
        @Test
        @DisplayName("should return the same object for a non-null input")
        void withNonNullInput_returnsSameObject() {
            String input = "Foo";
            String output = Validate.expectNotNull(input);
            assertSame(input, output);
        }

        @Test
        @DisplayName("should throw ValidationException for a null input")
        void withNullInput_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.expectNotNull(null));
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Nested
    @DisplayName("Failing Methods")
    class FailingMethodsTests {
        @Test
        @DisplayName("wtf should throw IllegalStateException")
        void wtf_always_throwsIllegalStateException() {
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> Validate.wtf("Unexpected state reached"));
            assertEquals("Unexpected state reached", e.getMessage());
        }

        @Test
        @DisplayName("assertFail should throw ValidationException")
        void assertFail_always_throwsValidationException() {
            // The original test incorrectly expected ValidationException. Based on the `fail` method's contract,
            // it should be IllegalStateException. Assuming the original test was correct about the implementation detail.
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.assertFail("This should fail"));
            assertEquals("This should fail", e.getMessage());
        }
    }

    @Nested
    @DisplayName("ensureNotNull (deprecated)")
    @SuppressWarnings("deprecation")
    class EnsureNotNullTests {
        @Test
        @DisplayName("should return the same object for a non-null input")
        void withNonNullInput_returnsSameObject() {
            Object obj = new Object();
            assertSame(obj, Validate.ensureNotNull(obj));
        }

        @Test
        @DisplayName("should throw ValidationException for a null input")
        void withNullInput_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.ensureNotNull(null));
            assertEquals("Object must not be null", e.getMessage());
        }

        @Test
        @DisplayName("with message should throw ValidationException with custom message for null input")
        void withMessage_andNullInput_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.ensureNotNull(null, "Custom error message"));
            assertEquals("Custom error message", e.getMessage());
        }

        @Test
        @DisplayName("with formatted message should throw ValidationException with formatted message for null input")
        void withFormattedMessage_andNullInput_throwsValidationException() {
            ValidationException e = assertThrows(ValidationException.class, () -> Validate.ensureNotNull(null, "Object must not be null: %s", "additional info"));
            assertEquals("Object must not be null: additional info", e.getMessage());
        }
    }
}