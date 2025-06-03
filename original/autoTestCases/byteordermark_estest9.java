package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test case verifies the length of the UTF-16BE Byte Order Mark (BOM).
 */
public class ByteOrderMarkUTF16BETest {

    @Test
    void testUTF16BELength() {
        // Arrange: Create an instance of the UTF-16BE ByteOrderMark.
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;

        // Act: Get the length of the BOM.
        int length = utf16BE.length();

        // Assert: Verify that the length is 2 bytes.
        assertEquals(2, length, "The length of UTF-16BE BOM should be 2.");
    }
}