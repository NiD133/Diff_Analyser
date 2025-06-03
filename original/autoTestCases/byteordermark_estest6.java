package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify the behavior of the {@link ByteOrderMark} class.
 * This class focuses on testing specific aspects of ByteOrderMark retrieval.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the second byte (index 1) of the UTF-32LE Byte Order Mark is correctly retrieved.
     * The UTF-32LE byte order mark is represented as {0xFF, 0xFE, 0x00, 0x00}.
     * Therefore, accessing the byte at index 1 should return 0xFE (254 in decimal).
     */
    @Test
    public void testGetByteAtIndexOneForUTF32LE() {
        // Arrange: Create a ByteOrderMark instance for UTF-32LE.
        ByteOrderMark byteOrderMark = ByteOrderMark.UTF_32LE;

        // Act: Retrieve the byte at index 1 of the ByteOrderMark.
        int byteAtIndexOne = byteOrderMark.get(1);

        // Assert: Verify that the retrieved byte is equal to 254 (0xFE).
        assertEquals(254, byteAtIndexOne, "The second byte of UTF-32LE should be 254 (0xFE).");
    }
}