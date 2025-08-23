package org.apache.commons.io;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test suite for the {@link HexDump} class, focusing on exception handling.
 */
public class HexDumpTest {

    /**
     * Tests that HexDump.dump() throws an ArrayIndexOutOfBoundsException
     * when the starting index is outside the bounds of the data array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void dumpWithIndexOutOfBoundsShouldThrowException() throws IOException {
        // Arrange: Create a data array and an index that is clearly out of its bounds.
        final byte[] data = new byte[20];
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final long offset = 0L; // The offset value is not relevant for this test.
        final int outOfBoundsIndex = data.length + 1; // Any index >= data.length is invalid.

        // Act: Attempt to dump the array starting from an invalid index.
        // The @Test(expected=...) annotation handles the assertion.
        HexDump.dump(data, offset, outputStream, outOfBoundsIndex);
    }
}