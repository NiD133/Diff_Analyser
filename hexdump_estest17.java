package org.apache.commons.io;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Contains tests for the {@link HexDump} class, focusing on edge cases and exception handling.
 */
public class HexDumpTest {

    /**
     * Tests that {@link HexDump#dump(byte[], long, Appendable, int, int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when the specified index and length
     * define a range that goes beyond the bounds of the input data array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void dumpShouldThrowIndexOutOfBoundsWhenRangeIsInvalid() throws IOException {
        // Arrange: Create a scenario where the requested dump range is out of bounds.
        final byte[] data = new byte[25];
        final int startIndex = 13;
        final int dumpLength = 13; // The sum of startIndex and dumpLength (26) exceeds the array's length (25).

        // The output destination and offset value are not relevant to this boundary check,
        // but are required by the method signature.
        final StringWriter output = new StringWriter();
        final long offset = 0L;

        // Act: This call is expected to fail with an ArrayIndexOutOfBoundsException.
        HexDump.dump(data, offset, output, startIndex, dumpLength);

        // Assert: The exception is verified by the @Test(expected=...) annotation.
    }
}