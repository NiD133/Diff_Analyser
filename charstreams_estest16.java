package com.google.common.io;

import org.junit.Test;

/**
 * Tests for {@link CharStreams}, focusing on edge cases and invalid inputs.
 */
public class CharStreamsTest {

    /**
     * Verifies that readLines() throws a NullPointerException when given a null Readable.
     * This is the expected behavior as per Guava's contract for non-nullable parameters.
     */
    @Test(expected = NullPointerException.class)
    public void readLines_withNullReadable_throwsNullPointerException() {
        // Act: Call the method under test with a null argument.
        // The @Test(expected) annotation handles the assertion.
        CharStreams.readLines(null);
    }
}