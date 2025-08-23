package com.google.common.io;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    /**
     * Verifies that toString() throws a NullPointerException when given a null input.
     * This is the expected behavior, as the method's contract requires a non-null argument.
     */
    @Test
    public void toString_whenInputIsNull_throwsNullPointerException() {
        // Act & Assert
        // The method under test should throw a NullPointerException immediately
        // due to Guava's Preconditions.checkNotNull check.
        assertThrows(
            NullPointerException.class,
            () -> CharStreams.toString(null)
        );
    }
}