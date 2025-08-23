package org.apache.commons.compress.archivers.ar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for the ArArchiveOutputStream class, focusing on edge cases and error handling.
 */
public class ArArchiveOutputStreamTest {

    /**
     * Verifies that calling the write() method with a negative length throws an
     * IndexOutOfBoundsException, as specified by the java.io.OutputStream contract.
     *
     * <p>The original auto-generated test attempted this call but used a non-standard
     * mock OutputStream that did not throw an exception. This resulted in a confusing
     * test that asserted an illogical internal state. This revised test correctly
     * verifies the documented behavior using a standard ByteArrayOutputStream.</p>
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void writeWithNegativeLengthShouldThrowException() throws IOException {
        // Arrange
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOut = new ArArchiveOutputStream(byteArrayOutputStream);

        // An entry must be open before data can be written to the stream.
        ArArchiveEntry entry = new ArArchiveEntry("test-entry.txt", 100);
        arOut.putArchiveEntry(entry);

        byte[] dataToWrite = new byte[10];
        int offset = 0;
        int negativeLength = -1;

        // Act: Attempt to write data with a negative length.
        // This call is expected to fail and throw an exception.
        arOut.write(dataToWrite, offset, negativeLength);

        // Assert: The @Test(expected=...) annotation handles the exception assertion.
    }
}