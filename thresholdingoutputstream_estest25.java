package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;

/**
 * Unit tests for {@link ThresholdingOutputStream}.
 */
public class ThresholdingOutputStreamTest {

    @Test
    public void writeUpdatesByteCountWhenThresholdIsNegative() throws IOException {
        // Arrange
        // A negative threshold is treated as 0, as per the constructor's contract.
        final int negativeThreshold = -1;
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(negativeThreshold);
        final byte[] dataToWrite = new byte[1];

        // Act
        stream.write(dataToWrite);

        // Assert
        // The byte count should reflect the number of bytes written.
        assertEquals("Byte count should be 1 after writing one byte.", 1L, stream.getByteCount());
    }
}