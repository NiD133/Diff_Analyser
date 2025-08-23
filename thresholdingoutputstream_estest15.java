package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Tests for {@link ThresholdingOutputStream}.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that the internal byte count is updated correctly even when the
     * write(byte[], int, int) method is called with parameters that would
     * normally cause an IndexOutOfBoundsException.
     * <p>
     * This behavior is due to the default underlying stream being a
     * {@link NullOutputStream}, which ignores invalid parameters and does not
     * throw an exception.
     * </p>
     */
    @Test
    public void testByteCountIsUpdatedEvenWithInvalidWriteParameters() throws IOException {
        // Arrange
        // A negative threshold is treated as 0 by the constructor.
        final int negativeThreshold = -2596;
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(negativeThreshold);

        final byte[] data = new byte[1];
        final int firstWriteLength = data.length;
        final int secondWriteLength = 1950;
        final int invalidOffset = -1473;

        // Act
        // Perform a first write to initialize the byte count.
        stream.write(data);

        // Perform a second write with an invalid offset. This does not throw an
        // exception because the default underlying stream is NullOutputStream,
        // which simply discards the output. The internal byte count, however,
        // is still incremented.
        stream.write(data, invalidOffset, secondWriteLength);

        // Assert
        final long expectedByteCount = (long) firstWriteLength + secondWriteLength;
        assertEquals("The byte count should be the sum of the lengths of all write operations.",
                expectedByteCount, stream.getByteCount());
    }
}