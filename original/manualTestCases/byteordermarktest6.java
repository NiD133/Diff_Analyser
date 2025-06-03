package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ByteOrderMarkTest {  // Renamed the class for clarity

    // Assuming these are pre-defined ByteOrderMark instances for testing
    private static final ByteOrderMark BOM_SINGLE_BYTE = new ByteOrderMark(0x01); //Example
    private static final ByteOrderMark BOM_TWO_BYTES   = new ByteOrderMark(0x01, 0x02); //Example
    private static final ByteOrderMark BOM_THREE_BYTES = new ByteOrderMark(0x01, 0x02, 0x03); //Example


    /**
     * Tests the {@link ByteOrderMark#get(int)} method.
     * This method should return the byte value at the specified index within the ByteOrderMark.
     */
    @Test
    public void testGetByteAtIndex() {
        // Test case 1:  Single-byte BOM
        assertEquals(0x01, BOM_SINGLE_BYTE.get(0), "Should return the first (and only) byte of BOM_SINGLE_BYTE");

        // Test case 2: Two-byte BOM
        assertEquals(0x01, BOM_TWO_BYTES.get(0), "Should return the first byte of BOM_TWO_BYTES");
        assertEquals(0x02, BOM_TWO_BYTES.get(1), "Should return the second byte of BOM_TWO_BYTES");

        // Test case 3: Three-byte BOM
        assertEquals(0x01, BOM_THREE_BYTES.get(0), "Should return the first byte of BOM_THREE_BYTES");
        assertEquals(0x02, BOM_THREE_BYTES.get(1), "Should return the second byte of BOM_THREE_BYTES");
        assertEquals(0x03, BOM_THREE_BYTES.get(2), "Should return the third byte of BOM_THREE_BYTES");
    }
}