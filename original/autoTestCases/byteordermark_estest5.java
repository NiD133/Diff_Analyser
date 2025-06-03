package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test case for the {@link ByteOrderMark} class.
 * This specific test focuses on retrieving a specific byte from the UTF-32LE Byte Order Mark.
 */
public class ByteOrderMark_UTF32LE_Test {

    /**
     * Tests that accessing the byte at index 2 of the UTF-32LE Byte Order Mark returns 0.
     * This validates that the third byte of the UTF-32LE BOM is indeed zero.
     */
    @Test
    public void testByteAtIndex2_UTF32LE_ReturnsZero() {
        // Arrange: Obtain the UTF-32LE Byte Order Mark.
        ByteOrderMark byteOrderMark = ByteOrderMark.UTF_32LE;

        // Act: Retrieve the byte at index 2 (the third byte) of the Byte Order Mark.
        int byteAtIndex2 = byteOrderMark.get(2);

        // Assert: Verify that the byte at index 2 is equal to 0.
        assertEquals(0, byteAtIndex2, "The byte at index 2 of UTF-32LE Byte Order Mark should be 0.");
    }
}