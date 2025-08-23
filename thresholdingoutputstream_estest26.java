package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Contains tests for the {@link ThresholdingOutputStream} class.
 * This specific test class focuses on an edge case identified from generated tests.
 */
public class ThresholdingOutputStream_ESTestTest26 extends ThresholdingOutputStream_ESTest_scaffolding {

    /**
     * Tests that the internal byte count is updated with the provided length,
     * even when that length is a negative value.
     *
     * <p>This test covers a non-standard edge case. The {@code write} method in a
     * standard {@code OutputStream} would typically throw an
     * {@code IndexOutOfBoundsException} for a negative length. However, the
     * {@code ThresholdingOutputStream} updates its internal counter regardless.
     * With its default {@code NullOutputStream}, no exception is thrown,
     * allowing this behavior to be observed.</p>
     *
     * @throws IOException if an I/O error occurs, though not expected with the default stream.
     */
    @Test
    public void testWriteWithNegativeLengthUpdatesByteCount() throws IOException {
        // Arrange
        final int arbitraryThreshold = 76;
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(arbitraryThreshold);

        final byte[] dummyBuffer = new byte[3]; // Buffer content and size are irrelevant here.
        final int arbitraryOffset = -2302;     // Offset is also irrelevant for this test's logic.
        final int negativeLength = -58;

        // Act
        // Call write() with a negative length. This should update the internal byte count.
        stream.write(dummyBuffer, arbitraryOffset, negativeLength);

        // Assert
        final long actualByteCount = stream.getByteCount();
        assertEquals("The byte count should reflect the negative length passed to write().",
                negativeLength, actualByteCount);
    }
}