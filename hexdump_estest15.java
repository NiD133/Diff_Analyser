package org.apache.commons.io;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Tests for {@link HexDump}.
 */
public class HexDumpTest {

    /**
     * Tests that HexDump.dump() throws an ArrayIndexOutOfBoundsException when the
     * starting index is outside the bounds of the data array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void dumpShouldThrowExceptionForOutOfBoundsIndex() throws IOException {
        // Arrange: Create a small data array and an index that is clearly out of bounds.
        final byte[] data = new byte[10];
        final StringWriter output = new StringWriter();
        final long offset = 0L;
        final int outOfBoundsIndex = 20; // Index is larger than the array length (10).
        final int length = 1;

        // Act: Call the dump method with the out-of-bounds index.
        // This call is expected to throw an ArrayIndexOutOfBoundsException.
        HexDump.dump(data, offset, output, outOfBoundsIndex, length);

        // Assert: The test passes if the expected exception is thrown.
        // This is handled by the @Test(expected=...) annotation.
    }
}