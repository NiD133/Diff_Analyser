package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import org.junit.Test;

/**
 * Contains tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Verifies that fromLittleEndian() returns 0 when provided with a negative length,
     * without attempting to read from the input stream.
     */
    @Test
    public void fromLittleEndianWithNegativeLengthShouldReturnZero() throws IOException {
        // Arrange: Create an empty input stream. The method should not read from it
        // when the length is negative, so an empty stream is sufficient.
        DataInput emptyInput = new DataInputStream(new ByteArrayInputStream(new byte[0]));
        final int negativeLength = -1;

        // Act: Call the method with a negative length.
        long result = ByteUtils.fromLittleEndian(emptyInput, negativeLength);

        // Assert: The result should be 0.
        assertEquals("Should return 0 for a negative length", 0L, result);
    }
}