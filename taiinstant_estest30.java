package org.threeten.extra.scale;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link TaiInstant}.
 */
public class TaiInstantTest {

    /**
     * Tests that calling TaiInstant.parse() with a null input throws a NullPointerException.
     * The Javadoc for the method specifies that the input text must not be null.
     */
    @Test
    public void parse_throwsNullPointerException_forNullInput() {
        // Arrange: No setup needed, the input is null.

        // Act & Assert: Verify that a NullPointerException is thrown.
        // Using assertThrows is the standard, modern way to test for exceptions,
        // replacing the older try-catch-fail pattern.
        NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> TaiInstant.parse(null)
        );

        // Further assert on the exception message for a more specific test.
        // The original test's structure suggested the underlying code uses
        // Objects.requireNonNull(text, "text"), which produces this message.
        assertEquals("text", exception.getMessage());
    }
}