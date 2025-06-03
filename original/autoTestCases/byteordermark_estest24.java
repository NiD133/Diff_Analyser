package org.apache.commons.io;

import org.junit.jupiter.api.Test;  // Use JUnit 5 for clarity and conciseness
import static org.junit.jupiter.api.Assertions.assertEquals; // Use JUnit 5 assertions

/**
 * This test case verifies that the {@link ByteOrderMark#getCharsetName()} method
 * of the {@link ByteOrderMark} class returns the correct charset name for a given
 * Byte Order Mark (BOM).
 */
class ByteOrderMarkCharsetNameTest { // More descriptive class name

    @Test
    void testUTF16BECharsetName() { // More descriptive method name
        // Arrange:  Create a ByteOrderMark instance representing UTF-16BE.
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;

        // Act:  Retrieve the charset name associated with the UTF-16BE BOM.
        String charsetName = utf16BE.getCharsetName();

        // Assert: Verify that the retrieved charset name is "UTF-16BE".
        assertEquals("UTF-16BE", charsetName, "The charset name should be UTF-16BE.");
    }
}