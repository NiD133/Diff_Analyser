package org.apache.commons.io;

import org.junit.jupiter.api.Test;  // Use JUnit 5 for clarity
import static org.junit.jupiter.api.Assertions.*; // Use JUnit 5 assertions

public class ByteOrderMarkTest {  // More descriptive class name

    @Test
    void testUTF8ByteOrderMarkBytes() { // More descriptive method name
        // Arrange:  Define the byte order mark we're testing
        ByteOrderMark utf8Bom = ByteOrderMark.UTF_8;

        // Act: Get the byte representation of the UTF-8 BOM
        byte[] bomBytes = utf8Bom.getBytes();

        // Assert: Verify that the byte array matches the expected UTF-8 BOM byte sequence
        byte[] expectedBytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}; //Use hexadecimal for clarity
        assertArrayEquals(expectedBytes, bomBytes, "The UTF-8 BOM bytes should match the expected sequence.");
    }
}