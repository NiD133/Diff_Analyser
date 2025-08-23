package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteOrderMark}.
 */
class ByteOrderMarkTest {

    @Test
    @DisplayName("getBytes() should return the correct byte array for BOMs of various lengths")
    void getBytesShouldReturnCorrectValues() {
        // Arrange
        final ByteOrderMark bomWithOneByte = new ByteOrderMark("BOM_1", 1);
        final ByteOrderMark bomWithTwoBytes = new ByteOrderMark("BOM_2", 1, 2);
        final ByteOrderMark bomWithThreeBytes = new ByteOrderMark("BOM_3", 1, 2, 3);

        // Act & Assert
        assertArrayEquals(new byte[]{(byte) 1}, bomWithOneByte.getBytes());
        assertArrayEquals(new byte[]{(byte) 1, (byte) 2}, bomWithTwoBytes.getBytes());
        assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3}, bomWithThreeBytes.getBytes());
    }

    @Test
    @DisplayName("getBytes() should return a defensive copy to ensure the BOM is immutable")
    void getBytesShouldReturnDefensiveCopy() {
        // Arrange
        final byte[] expectedBytes = {(byte) 1, (byte) 2};
        final ByteOrderMark bom = new ByteOrderMark("test", 1, 2);

        // Act: Retrieve the byte array and attempt to modify it
        final byte[] retrievedBytes = bom.getBytes();
        retrievedBytes[0] = 99; // Modify the returned array

        // Assert: A subsequent call to getBytes() should return the original, unmodified bytes,
        // proving the internal state of the object was not affected.
        assertArrayEquals(expectedBytes, bom.getBytes());
    }
}