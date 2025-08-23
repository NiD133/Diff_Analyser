package org.apache.commons.io.output;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link XmlStreamWriter} class.
 * This class focuses on improving a specific auto-generated test case.
 */
public class XmlStreamWriter_ESTestTest5 {

    /**
     * Tests that the write(char[], int, int) method throws an IndexOutOfBoundsException
     * when the offset and length arguments are outside the bounds of the source character array.
     */
    @Test
    public void writeWithOutOfBoundsParametersShouldThrowException() throws IOException {
        // Arrange: Create a writer and a buffer with invalid write parameters.
        // Using a real OutputStream implementation like ByteArrayOutputStream is more robust
        // than using null, as it prevents potential NullPointerExceptions if the
        // constructor's behavior changes.
        final OutputStream outputStream = new ByteArrayOutputStream();
        final XmlStreamWriter writer = new XmlStreamWriter(outputStream);

        final char[] emptyBuffer = new char[0];
        final int outOfBoundsOffset = 1; // Any offset > 0 is invalid for an empty buffer
        final int length = 1;

        // Act & Assert: Attempt the write operation and verify that the correct exception is thrown.
        try {
            writer.write(emptyBuffer, outOfBoundsOffset, length);
            fail("Expected an IndexOutOfBoundsException to be thrown due to out-of-bounds parameters.");
        } catch (final IndexOutOfBoundsException e) {
            // Success: The expected exception was caught. The test passes.
        }
    }
}