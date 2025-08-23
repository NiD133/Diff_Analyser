package com.google.common.io;

import org.junit.Test;
import java.io.IOException;

/**
 * Tests for {@link CharSequenceReader}.
 * This class contains a more understandable version of a previously auto-generated test.
 */
public class CharSequenceReaderTest {

    /**
     * Verifies that the read(char[], int, int) method throws an IndexOutOfBoundsException
     * when called with a negative offset and a negative length.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void read_withNegativeOffsetAndLength_throwsIndexOutOfBoundsException() throws IOException {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test data");
        char[] destinationBuffer = new char[10];

        // Act
        // Attempt to read into the buffer with invalid negative arguments.
        // This call is expected to fail with an IndexOutOfBoundsException due to precondition checks.
        reader.read(destinationBuffer, -1, -1);

        // Assert
        // The test passes if the expected IndexOutOfBoundsException is thrown.
        // This is handled by the @Test(expected = ...) annotation.
    }
}