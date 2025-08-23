package org.apache.commons.compress.archivers.ar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for the write operations of {@link ArArchiveOutputStream}.
 */
// The original test class name "ArArchiveOutputStream_ESTestTest12" was auto-generated.
// A more descriptive name like "ArArchiveOutputStreamWriteTest" would be preferable in a real project.
public class ArArchiveOutputStream_ESTestTest12 {

    /**
     * Verifies that calling the write(byte[], int, int) method with a negative offset
     * correctly throws an ArrayIndexOutOfBoundsException, as expected from the OutputStream contract.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void writeWithNegativeOffsetShouldThrowException() throws IOException {
        // Arrange: Set up the stream and the invalid arguments for the write method.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream);

        byte[] buffer = new byte[10];
        int negativeOffset = -1; // A negative offset is invalid.
        int length = 20;         // The length is also out of bounds, but the offset is checked first.

        // Act: Attempt to write with the invalid negative offset.
        // The @Test(expected=...) annotation will assert that the correct exception is thrown.
        arOut.write(buffer, negativeOffset, length);
    }
}