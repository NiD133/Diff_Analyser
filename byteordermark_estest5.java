package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ByteOrderMark} class.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the get() method correctly retrieves a byte from the UTF-32BE BOM.
     * <p>
     * The source code defines the UTF-32BE BOM with the byte sequence {0x00, 0x00, 0xFE, 0xFF}.
     * This test verifies that retrieving the byte at index 3 returns the expected value 0xFF.
     * </p>
     */
    @Test
    public void getFromUtf32beReturnsCorrectByte() {
        // Arrange: The UTF-32BE BOM constant is used as the test subject.
        final ByteOrderMark utf32beBom = ByteOrderMark.UTF_32BE;
        final int indexToGet = 3;
        final int expectedByte = 0xFF; // The 4th byte (at index 3) is 0xFF (255).

        // Act: Retrieve the byte at the specified index.
        final int actualByte = utf32beBom.get(indexToGet);

        // Assert: The retrieved byte should match the expected value from the BOM definition.
        assertEquals(expectedByte, actualByte);
    }
}