package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NumberOutput_ESTestTest74 extends NumberOutput_ESTest_scaffolding {

    /**
     * Verifies that {@code NumberOutput.outputLong()} throws an
     * {@link ArrayIndexOutOfBoundsException} when the provided offset is
     * outside the bounds of the destination buffer.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongShouldThrowExceptionWhenOffsetIsOutOfBounds() {
        // Arrange: A small buffer and an offset that is clearly invalid.
        char[] buffer = new char[4];
        int invalidOffset = 100; // An offset well beyond the buffer's capacity.
        long valueToWrite = 1_000_000_000_000L; // The actual value is irrelevant for this test.

        // Act: Attempt to write the long value at the invalid offset.
        // The @Test(expected=...) annotation asserts that an exception is thrown.
        NumberOutput.outputLong(valueToWrite, buffer, invalidOffset);
    }
}