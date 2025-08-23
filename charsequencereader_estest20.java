package com.google.common.io;

import org.junit.Test;

/**
 * This test suite contains refactored tests for the {@link CharSequenceReader} class.
 * The original test was auto-generated and has been improved for clarity and maintainability.
 */
public class CharSequenceReader_ESTestTest20 extends CharSequenceReader_ESTest_scaffolding {

    /**
     * Verifies that the CharSequenceReader constructor rejects null input.
     * The constructor is expected to throw a NullPointerException if the provided
     * CharSequence is null, enforcing its non-null contract.
     */
    @Test(expected = NullPointerException.class)
    public void constructor_whenCharSequenceIsNull_throwsNullPointerException() {
        // Act: Attempt to create a reader with a null CharSequence.
        // Assert: A NullPointerException is expected, as specified by the @Test annotation.
        new CharSequenceReader(null);
    }
}