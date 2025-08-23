package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the matches() method returns true when the provided byte array
     * is identical to the Byte Order Mark's internal bytes.
     */
    @Test
    public void testMatchesReturnsTrueForIdenticalByteArray() {
        // Arrange
        // Use a non-trivial byte sequence to define the BOM.
        final int[] bomBytes = {0x12, 0x34, 0x56};
        final ByteOrderMark bom = new ByteOrderMark("CUSTOM-BOM", bomBytes);

        // Act
        // Check if the BOM matches the exact same byte sequence it was created with.
        final boolean isMatch = bom.matches(bomBytes);

        // Assert
        assertTrue("matches() should return true for an identical byte array.", isMatch);
    }
}