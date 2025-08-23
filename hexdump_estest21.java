package org.apache.commons.io;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Tests for the {@link HexDump} class, focusing on exceptional cases.
 */
public class HexDumpTest {

    /**
     * Verifies that the dump method throws an ArrayIndexOutOfBoundsException
     * when a negative index is provided.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void dumpWithNegativeIndexShouldThrowArrayIndexOutOfBoundsException() throws IOException {
        // Arrange: Set up the data and a valid output writer.
        byte[] data = new byte[10];
        StringWriter output = new StringWriter();
        long offset = 0L;
        int length = 5;
        int negativeIndex = -1;

        // Act: Call the dump method with a negative index.
        // The @Test(expected=...) annotation handles the assertion.
        HexDump.dump(data, offset, output, negativeIndex, length);
    }
}