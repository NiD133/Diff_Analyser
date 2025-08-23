package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertThrows;

import java.util.Date;
import org.junit.Test;

/**
 * Tests for the {@link ISO8601Utils} class.
 * Note: The original class name 'ISO8601Utils_ESTestTest7' was kept for context,
 * but a more conventional name would be 'ISO8601UtilsTest'.
 */
public class ISO8601Utils_ESTestTest7 {

    /**
     * Verifies that calling format() with a null Date throws a NullPointerException.
     * The method is expected to fail fast when given invalid input, and passing
     * null is a fundamental edge case to test.
     */
    @Test
    public void format_withNullDate_shouldThrowNullPointerException() {
        // The explicit cast to (Date) is necessary to resolve ambiguity
        // between the multiple 'format' method overloads.
        assertThrows(NullPointerException.class, () -> ISO8601Utils.format((Date) null));
    }
}