package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(ByteUtils.ByteSupplier, int)}
     * correctly converts a single byte with the value 0 into the long value 0L.
     */
    @Test
    public void fromLittleEndianWithSupplierReadsSingleZeroByteAsZero() throws IOException {
        // Arrange: Create a mock ByteSupplier that returns a single byte with value 0.
        // The method under test will read 'length' bytes, which is 1 in this case.
        final ByteUtils.ByteSupplier mockSupplier = mock(ByteUtils.ByteSupplier.class);
        when(mockSupplier.getAsByte()).thenReturn(0);

        // Act: Call the method to convert one byte from the supplier to a long.
        final long result = ByteUtils.fromLittleEndian(mockSupplier, 1);

        // Assert: The resulting long should be 0.
        assertEquals(0L, result);
    }
}