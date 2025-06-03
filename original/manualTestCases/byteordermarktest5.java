package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class contains unit tests for the {@link ByteOrderMark} class.
 * It focuses on verifying the correctness of the {@link ByteOrderMark#getCharsetName()} method.
 */
public class ByteOrderMarkTest { // Renamed class for clarity

    // Define constants for ByteOrderMarks, making the test self-contained and readable
    private static final ByteOrderMark TEST_BOM_1 = new ByteOrderMark("test1", (byte) 0xEF, (byte) 0xBB, (byte) 0xBF);
    private static final ByteOrderMark TEST_BOM_2 = new ByteOrderMark("test2", (byte) 0xFE, (byte) 0xFF);
    private static final ByteOrderMark TEST_BOM_3 = new ByteOrderMark("test3", (byte) 0xFF, (byte) 0xFE);

    /**
     * Tests the {@link ByteOrderMark#getCharsetName()} method to ensure it returns the correct character set name
     * associated with each Byte Order Mark.
     */
    @Test
    public void testGetCharsetName() {
        // Assert that the charset name returned by getCharsetName() matches the expected value for each ByteOrderMark.
        assertEquals("test1", TEST_BOM_1.getCharsetName(), "The charset name for TEST_BOM_1 should be 'test1'.");
        assertEquals("test2", TEST_BOM_2.getCharsetName(), "The charset name for TEST_BOM_2 should be 'test2'.");
        assertEquals("test3", TEST_BOM_3.getCharsetName(), "The charset name for TEST_BOM_3 should be 'test3'.");
    }
}