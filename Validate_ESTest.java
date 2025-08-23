package org.jsoup.helper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for Validate.
 *
 * Notes:
 * - These tests focus on Validate’s contract (what it guarantees) rather than oddities of String.format.
 * - Messages asserted here match the current behavior of jsoup’s Validate class.
 * - We avoid testing deliberately malformed format strings; those exceptions belong to java.util.Formatter.
 */
public class ValidateTest {

    // notNull

    @Test
    public void notNull_throws_whenNull_withDefaultMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.notNull(null));
        assertEquals("Object must not be null", ex.getMessage());
    }

    @Test
    public void notNull_doesNothing_whenNonNull() {
        Validate.notNull(new Object());
    }

    @Test
    public void notNull_throws_withCustomMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.notNull(null, "Custom message"));
        assertEquals("Custom message", ex.getMessage());
    }

    // notNullParam

    @Test
    public void notNullParam_throws_andIncludesParamName() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.notNullParam(null, "userId"));
        assertEquals("The parameter 'userId' must not be null.", ex.getMessage());
    }

    @Test
    public void notNullParam_doesNothing_whenNonNull() {
        Validate.notNullParam("value", "userId");
    }

    // expectNotNull

    @Test
    public void expectNotNull_returnsSameReference_whenNonNull() {
        String s = "value";
        String out = Validate.expectNotNull(s);
        assertSame(s, out);
    }

    @Test
    public void expectNotNull_throws_whenNull_withDefaultMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.expectNotNull(null));
        assertEquals("Object must not be null", ex.getMessage());
    }

    @Test
    public void expectNotNull_throws_whenNull_withFormattedMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.expectNotNull(null, "Missing %s", "thing"));
        assertEquals("Missing thing", ex.getMessage());
    }

    // ensureNotNull (deprecated)

    @Test
    public void ensureNotNull_returnsSameReference_whenNonNull() {
        Object in = new Object();
        Object out = Validate.ensureNotNull(in);
        assertSame(in, out);
    }

    @Test
    public void ensureNotNull_throws_whenNull_withDefaultMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.ensureNotNull(null));
        assertEquals("Object must not be null", ex.getMessage());
    }

    @Test
    public void ensureNotNull_throws_whenNull_withFormattedMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.ensureNotNull(null, "Problem: %s", "boom"));
        assertEquals("Problem: boom", ex.getMessage());
    }

    @Test
    public void ensureNotNull_throws_whenNull_withNullVarargs_usesMessageVerbatim() {
        // Some environments pass null for varargs; Validate should use the message as-is (no formatting).
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.ensureNotNull(null, "Just this message", (Object[]) null));
        assertEquals("Just this message", ex.getMessage());
    }

    // isTrue

    @Test
    public void isTrue_doesNothing_whenTrue() {
        Validate.isTrue(true);
    }

    @Test
    public void isTrue_throws_whenFalse_withDefaultMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.isTrue(false));
        assertEquals("Must be true", ex.getMessage());
    }

    @Test
    public void isTrue_throws_whenFalse_withCustomMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.isTrue(false, "Expected condition to be true"));
        assertEquals("Expected condition to be true", ex.getMessage());
    }

    // isFalse

    @Test
    public void isFalse_doesNothing_whenFalse() {
        Validate.isFalse(false);
    }

    @Test
    public void isFalse_throws_whenTrue_withDefaultMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.isFalse(true));
        assertEquals("Must be false", ex.getMessage());
    }

    @Test
    public void isFalse_throws_whenTrue_withCustomMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.isFalse(true, "Expected condition to be false"));
        assertEquals("Expected condition to be false", ex.getMessage());
    }

    // notEmpty

    @Test
    public void notEmpty_throws_whenEmpty_withDefaultMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.notEmpty(""));
        assertEquals("String must not be empty", ex.getMessage());
    }

    @Test
    public void notEmpty_throws_whenNull_withDefaultMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.notEmpty(null));
        assertEquals("String must not be empty", ex.getMessage());
    }

    @Test
    public void notEmpty_doesNothing_whenNonEmpty() {
        Validate.notEmpty("value");
    }

    @Test
    public void notEmpty_throws_withCustomMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.notEmpty("", "Custom empty message"));
        assertEquals("Custom empty message", ex.getMessage());
    }

    // notEmptyParam

    @Test
    public void notEmptyParam_throws_whenNull_includesParamName() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.notEmptyParam(null, "name"));
        assertEquals("The 'name' parameter must not be empty.", ex.getMessage());
    }

    @Test
    public void notEmptyParam_throws_whenEmpty_includesParamName() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.notEmptyParam("", "name"));
        assertEquals("The 'name' parameter must not be empty.", ex.getMessage());
    }

    @Test
    public void notEmptyParam_doesNothing_whenNonEmpty() {
        Validate.notEmptyParam("ok", "name");
    }

    // noNullElements

    @Test
    public void noNullElements_doesNothing_whenArrayEmpty() {
        Validate.noNullElements(new Object[0]);
    }

    @Test
    public void noNullElements_throws_whenContainsNull_withDefaultMessage() {
        Object[] arr = { "a", null, "b" };
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.noNullElements(arr));
        assertEquals("Array must not contain any null objects", ex.getMessage());
    }

    @Test
    public void noNullElements_throws_withCustomMessage() {
        Object[] arr = new Object[1]; // contains a single null
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.noNullElements(arr, "No nulls allowed"));
        assertEquals("No nulls allowed", ex.getMessage());
    }

    @Test
    public void noNullElements_nullArray_throws_NPE() {
        // The API does not guard against a null array; current behavior is a NullPointerException.
        assertThrows(NullPointerException.class, () -> Validate.noNullElements((Object[]) null));
        assertThrows(NullPointerException.class, () -> Validate.noNullElements((Object[]) null, "ignored"));
    }

    // fail / assertFail / wtf

    @Test
    public void fail_throwsIllegalArgumentException_withMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.fail("Boom"));
        assertEquals("Boom", ex.getMessage());
    }

    @Test
    public void fail_throwsIllegalArgumentException_withFormattedMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.fail("Issue: %s", "bad input"));
        assertEquals("Issue: bad input", ex.getMessage());
    }

    @Test
    public void assertFail_alwaysThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> Validate.assertFail("Should not reach here"));
        assertEquals("Should not reach here", ex.getMessage());
    }

    @Test
    public void wtf_alwaysThrowsIllegalStateException() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
            () -> Validate.wtf("Unexpected state"));
        assertEquals("Unexpected state", ex.getMessage());
    }
}