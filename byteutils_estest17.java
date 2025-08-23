package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(ByteUtils.ByteSupplier, int)}
     * correctly converts a single byte from a supplier into a long value.
     */
    @Test
    public void fromLittleEndianWithSupplierReadsSingleByteCorrectly() throws IOException {
        // Arrange: Set up a mock supplier that will return a single byte.
        final int byteValue = 8;
        final int lengthToRead = 1;
        final long expectedLongValue = 8L;

        ByteUtils.ByteSupplier mockSupplier = mock(ByteUtils.ByteSupplier.class);
        when(mockSupplier.getAsByte()).thenReturn(byteValue);

        // Act: Call the method under test.
        long actualLongValue = ByteUtils.fromLittleEndian(mockSupplier, lengthToRead);

        // Assert: Verify that the returned long matches the expected value.
        assertEquals("A single byte should be converted to its long equivalent.",
                expectedLongValue, actualLongValue);
    }
}