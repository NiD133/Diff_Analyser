package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test cases for the {@link ByteOrderMark} class, specifically testing the {@link ByteOrderMark#length()} method.
 */
public class ByteOrderMarkLengthTest { // Renamed class for clarity

    //  Assuming TEST_BOM_1, TEST_BOM_2, and TEST_BOM_3 are defined as ByteOrderMark constants
    //  else you need to define them in this class or import them.  For example:
    private static final ByteOrderMark TEST_BOM_1 = ByteOrderMark.UTF_8; // Example: Assuming it's UTF-8
    private static final ByteOrderMark TEST_BOM_2 = ByteOrderMark.UTF_16BE; // Example: Assuming it's UTF-16BE
    private static final ByteOrderMark TEST_BOM_3 = ByteOrderMark.UTF_8; // Example: Assuming it's UTF-8

    /**
     * Tests the {@link ByteOrderMark#length()} method to ensure it returns the correct length (number of bytes)
     * for different Byte Order Mark instances.
     */
    @Test
    public void testByteOrderMarkLength() { // Renamed method for clarity

        // Test case 1: ByteOrderMark with length 1
        assertEquals(1, TEST_BOM_1.length(), "The length of TEST_BOM_1 should be 1.");

        // Test case 2: ByteOrderMark with length 2
        assertEquals(2, TEST_BOM_2.length(), "The length of TEST_BOM_2 should be 2.");

        // Test case 3: ByteOrderMark with length 3
        assertEquals(3, TEST_BOM_3.length(), "The length of TEST_BOM_3 should be 3.");
    }
}