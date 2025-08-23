package org.apache.commons.io;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link HexDump} class, focusing on invalid argument handling.
 */
public class HexDumpTest {

    /**
     * Verifies that HexDump.dump() throws an ArrayIndexOutOfBoundsException
     * when called with a negative length argument. The method should reject
     * any attempt to read a negative number of bytes from the array.
     */
    @Test
    public void dumpShouldThrowExceptionForNegativeLength() {
        // Arrange: Set up the input data and arguments for the dump method.
        // A small, non-empty byte array is sufficient for this test.
        final byte[] data = new byte[5];
        final StringBuilder output = new StringBuilder();
        final long offset = 0L;
        final int index = 0;
        final int negativeLength = -1; // The invalid argument being tested.

        // Act & Assert: Call the method and verify that the correct exception is thrown.
        try {
            HexDump.dump(data, offset, output, index, negativeLength);
            fail("Expected an ArrayIndexOutOfBoundsException due to negative length, but none was thrown.");
        } catch (final ArrayIndexOutOfBoundsException e) {
            // This is the expected behavior.
            // For a more robust test, we can verify the exception message.
            final String message = e.getMessage();
            assertTrue("Exception message should indicate an out-of-bounds error.",
                    message != null && message.contains("out of bounds for length " + data.length));
        } catch (final IOException e) {
            // The dump method signature includes IOException, but it is not expected here.
            // If it occurs, the test should fail.
            fail("An unexpected IOException was thrown: " + e.getMessage());
        }
    }
}