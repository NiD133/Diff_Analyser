package org.apache.commons.io.output;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link ThresholdingOutputStream}.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that calling write() with a null byte array throws a NullPointerException.
     * This is the expected behavior as per the contract of most I/O operations.
     */
    @Test
    public void write_withNullByteArray_shouldThrowNullPointerException() {
        // Arrange: Create a stream instance. The threshold value is not relevant for this test.
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(100);

        // Act & Assert: Verify that a NullPointerException is thrown when writing a null array.
        assertThrows(NullPointerException.class, () -> stream.write((byte[]) null));
    }
}