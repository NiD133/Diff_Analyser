package org.jsoup.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateTest {

    @Test
    public void testNotNull() {
        // Test with a non-null object
        Validate.notNull("foo");

        // Test with a null object
        Exception exception = assertThrows(IllegalArgumentException.class, () -> Validate.notNull(null));
        assertEquals("Object must not be null", exception.getMessage());
    }

    @Test
    void testStacktraceFiltersOutValidateClass() {
        Exception exception = assertThrows(ValidationException.class, () -> Validate.notNull(null));
        assertEquals("Object must not be null", exception.getMessage());

        StackTraceElement[] stackTrace = exception.getStackTrace();
        for (StackTraceElement trace : stackTrace) {
            assertNotEquals(Validate.class.getName(), trace.getClassName());
        }
        assertTrue(stackTrace.length >= 1);
    }

    @Test
    void testNonNullParam() {
        Exception exception = assertThrows(ValidationException.class, () -> Validate.notNullParam(null, "foo"));
        assertEquals("The parameter 'foo' must not be null.", exception.getMessage());
    }

    @Test
    public void testWtf() {
        Exception exception = assertThrows(IllegalStateException.class, () -> Validate.wtf("Unexpected state reached"));
        assertEquals("Unexpected state reached", exception.getMessage());
    }

    @Test
    public void testEnsureNotNull() {
        // Test with a non-null object
        Object obj = new Object();
        assertSame(obj, Validate.ensureNotNull(obj));

        // Test with a null object
        Exception exception = assertThrows(ValidationException.class, () -> Validate.ensureNotNull(null));
        assertEquals("Object must not be null", exception.getMessage());
    }

    @Test
    public void testEnsureNotNullWithMessage() {
        // Test with a non-null object
        Object obj = new Object();
        assertSame(obj, Validate.ensureNotNull(obj, "Object must not be null"));

        // Test with a null object
        Exception exception = assertThrows(ValidationException.class, () -> Validate.ensureNotNull(null, "Custom error message"));
        assertEquals("Custom error message", exception.getMessage());
    }

    @Test
    public void testEnsureNotNullWithFormattedMessage() {
        // Test with a non-null object
        Object obj = new Object();
        assertSame(obj, Validate.ensureNotNull(obj, "Object must not be null: %s", "additional info"));

        // Test with a null object
        Exception exception = assertThrows(ValidationException.class, () -> Validate.ensureNotNull(null, "Object must not be null: %s", "additional info"));
        assertEquals("Object must not be null: additional info", exception.getMessage());
    }

    @Test
    void testExpectNotNull() {
        // Test with a non-null object
        String foo = "Foo";
        String foo2 = Validate.expectNotNull(foo);
        assertSame(foo, foo2);

        // Test with a null object
        Exception exception = assertThrows(ValidationException.class, () -> Validate.expectNotNull(null));
        assertEquals("Object must not be null", exception.getMessage());
    }

    @Test
    public void testNotNullParam() {
        // Test with a non-null object
        Object obj = new Object();
        Validate.notNullParam(obj, "param");

        // Test with a null object
        Exception exception = assertThrows(ValidationException.class, () -> Validate.notNullParam(null, "param"));
        assertEquals("The parameter 'param' must not be null.", exception.getMessage());
    }

    @Test
    public void testNotEmpty() {
        // Test with a non-empty string
        Validate.notEmpty("foo");

        // Test with an empty string
        Exception exception = assertThrows(ValidationException.class, () -> Validate.notEmpty(""));
        assertEquals("String must not be empty", exception.getMessage());

        // Test with a null string
        exception = assertThrows(ValidationException.class, () -> Validate.notEmpty(null));
        assertEquals("String must not be empty", exception.getMessage());
    }

    @Test
    public void testIsTrue() {
        // Test with a true value
        Validate.isTrue(true);

        // Test with a false value
        Exception exception = assertThrows(ValidationException.class, () -> Validate.isTrue(false));
        assertEquals("Must be true", exception.getMessage());
    }

    @Test
    public void testIsFalse() {
        // Test with a false value
        Validate.isFalse(false);

        // Test with a true value
        Exception exception = assertThrows(ValidationException.class, () -> Validate.isFalse(true));
        assertEquals("Must be false", exception.getMessage());
    }

    @Test
    public void testAssertFail() {
        Exception exception = assertThrows(ValidationException.class, () -> Validate.assertFail("This should fail"));
        assertEquals("This should fail", exception.getMessage());
    }

    @Test
    public void testNotEmptyParam() {
        // Test with a non-empty string
        Validate.notEmptyParam("foo", "param");

        // Test with an empty string
        Exception exception = assertThrows(ValidationException.class, () -> Validate.notEmptyParam("", "param"));
        assertEquals("The 'param' parameter must not be empty.", exception.getMessage());

        // Test with a null string
        exception = assertThrows(ValidationException.class, () -> Validate.notEmptyParam(null, "param"));
        assertEquals("The 'param' parameter must not be empty.", exception.getMessage());
    }

    @Test
    public void testNoNullElementsWithMessage() {
        // Test with an array with no null elements
        Object[] array = {new Object(), new Object()};
        Validate.noNullElements(array, "Custom error message");

        // Test with an array containing a null element
        Exception exception = assertThrows(ValidationException.class, () -> Validate.noNullElements(new Object[]{new Object(), null}, "Custom error message"));
        assertEquals("Custom error message", exception.getMessage());
    }

    @Test
    public void testNotEmptyWithMessage() {
        // Test with a non-empty string
        Validate.notEmpty("foo", "Custom error message");

        // Test with an empty string
        Exception exception = assertThrows(ValidationException.class, () -> Validate.notEmpty("", "Custom error message"));
        assertEquals("Custom error message", exception.getMessage());

        // Test with a null string
        exception = assertThrows(ValidationException.class, () -> Validate.notEmpty(null, "Custom error message"));
        assertEquals("Custom error message", exception.getMessage());
    }
}