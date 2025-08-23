package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Tests for the {@link ByteUtils} class, focusing on edge cases and invalid arguments.
 */
public class ByteUtilsTest {

    /**
     * Verifies that toLittleEndian throws a NullPointerException when given a null DataOutput.
     * This ensures the method correctly handles null inputs as per standard API contracts.
     */
    @Test(expected = NullPointerException.class)
    public void toLittleEndianWithNullDataOutputThrowsNullPointerException() throws IOException {
        // Arrange: Define the arguments for the method call. The key is the null DataOutput.
        final DataOutput nullDataOutput = null;
        final long value = 0L;
        // The length is arbitrary, as the NPE should occur before it is used.
        // A realistic value is chosen for clarity.
        final int length = 4;

        // Act & Assert: Call the method with the null argument.
        // The @Test(expected=...) annotation handles the assertion that an NPE is thrown.
        ByteUtils.toLittleEndian(nullDataOutput, value, length);
    }
}