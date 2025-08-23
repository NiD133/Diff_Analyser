package org.apache.commons.io;

import org.junit.Test;

import java.io.StringWriter;

/**
 * Tests for the {@link HexDump} class.
 */
public class HexDumpTest {

    /**
     * Tests that HexDump.dump() throws a NullPointerException when the input data array is null.
     */
    @Test(expected = NullPointerException.class)
    public void testDumpWithNullDataArrayThrowsNullPointerException() {
        // Arrange
        final byte[] data = null;
        final StringWriter writer = new StringWriter();
        
        // Act: Call the method under test with a null data array.
        // The other parameters are arbitrary as the method should fail fast.
        HexDump.dump(data, 0L, writer, 0, 0);

        // Assert: The test expects a NullPointerException, which is handled by the
        // @Test(expected = ...) annotation.
    }
}