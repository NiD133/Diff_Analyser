package org.jsoup.helper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("deprecation") // keeps tests for ensureNotNull
public class ValidateTest {

    private static <T extends Throwable> T assertThrowsWithMessage(Class<T> type, Executable exec, String expectedMessage) {
        T ex = assertThrows(type, exec);
        assertEquals(expectedMessage, ex.getMessage());
        return ex;
    }

    @Nested
    class NotNull {
        @Test
        void acceptsNonNull() {
            Validate.notNull("foo");
        }

        @Test
        void throwsWithDefaultMessage() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.notNull(null),
                "Object must not be null");
        }

        @Test
        void throwsWithCustomMessage() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.notNull(null, "Custom error"),
                "Custom error");
        }

        @Test
        void paramVariant_includesParamName() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.notNullParam(null, "foo"),
                "The parameter 'foo' must not be null.");
        }

        @Test
        void stacktraceFiltersOutValidateClass() {
            ValidationException ex = assertThrowsWithMessage(ValidationException.class,
                () -> Validate.notNull(null),
                "Object must not be null");

            // Ensure the Validate class is not present in the stack trace
            for (StackTraceElement el : ex.getStackTrace()) {
                assertNotEquals(Validate.class.getName(), el.getClassName());
            }
            assertTrue(ex.getStackTrace().length >= 1);
        }
    }

    @Nested
    class EnsureNotNull_Deprecated {
        @Test
        void returnsSameInstance() {
            Object obj = new Object();
            assertSame(obj, Validate.ensureNotNull(obj));
        }

        @Test
        void throwsWithDefaultMessage() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.ensureNotNull(null),
                "Object must not be null");
        }

        @Test
        void throwsWithProvidedMessage() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.ensureNotNull(null, "Custom error message"),
                "Custom error message");
        }

        @Test
        void throwsWithFormattedMessage() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.ensureNotNull(null, "Object must not be null: %s", "additional info"),
                "Object must not be null: additional info");
        }
    }

    @Nested
    class ExpectNotNull {
        @Test
        void returnsSameTypedInstance() {
            String s = "Foo";
            String result = Validate.expectNotNull(s);
            assertSame(s, result);
        }

        @Test
        void throwsWithDefaultMessage() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.expectNotNull(null),
                "Object must not be null");
        }
    }

    @Nested
    class Strings {
        @Test
        void notEmpty_acceptsNonEmpty() {
            Validate.notEmpty("foo");
        }

        @Test
        void notEmpty_rejectsEmpty() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.notEmpty(""),
                "String must not be empty");
        }

        @Test
        void notEmpty_rejectsNull() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.notEmpty(null),
                "String must not be empty");
        }

        @Test
        void notEmptyParam_rejectsEmpty_includesParamName() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.notEmptyParam("", "param"),
                "The 'param' parameter must not be empty.");
        }

        @Test
        void notEmptyParam_rejectsNull_includesParamName() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.notEmptyParam(null, "param"),
                "The 'param' parameter must not be empty.");
        }

        @Test
        void notEmpty_withCustomMessage_isUsedForEmpty() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.notEmpty("", "Custom error message"),
                "Custom error message");
        }

        @Test
        void notEmpty_withCustomMessage_isUsedForNull() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.notEmpty(null, "Custom error message"),
                "Custom error message");
        }
    }

    @Nested
    class Booleans {
        @Test
        void isTrue_rejectsFalse_withDefaultMessage() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.isTrue(false),
                "Must be true");
        }

        @Test
        void isFalse_rejectsTrue_withDefaultMessage() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.isFalse(true),
                "Must be false");
        }
    }

    @Nested
    class Arrays {
        @Test
        void noNullElements_acceptsArrayWithoutNulls() {
            Validate.noNullElements(new Object[] { new Object(), new Object() });
        }

        @Test
        void noNullElements_rejectsArrayWithNull_withCustomMessage() {
            assertThrowsWithMessage(ValidationException.class,
                () -> Validate.noNullElements(new Object[] { new Object(), null }, "Custom error message"),
                "Custom error message");
        }
    }

    @Nested
    class Failures {
        @Test
        void wtf_throwsIllegalStateWithProvidedMessage() {
            assertThrowsWithMessage(IllegalStateException.class,
                () -> Validate.wtf("Unexpected state reached"),
                "Unexpected state reached");
        }

        @Test
        void assertFail_alwaysThrowsIllegalState_withProvidedMessage() {
            assertThrowsWithMessage(IllegalStateException.class,
                () -> Validate.assertFail("This should fail"),
                "This should fail");
        }
    }
}