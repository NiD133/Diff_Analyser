package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

/**
 * Test case for the {@link ByteOrderMark} class, focusing on the {@link ByteOrderMark#getBytes()} method.
 * This test verifies that the correct byte arrays are returned for different Byte Order Marks and that modifications
 * to the returned array don't affect the original ByteOrderMark's data.
 */
public class ByteOrderMarkBytesTest {

    // Predefined Byte Order Mark instances for testing.  It's assumed these are defined elsewhere
    // (e.g., as static final fields in the class being tested or in a common test utility class).
    // Replace these placeholders with actual ByteOrderMark instances.
    private static final ByteOrderMark BOM_ONE_BYTE   = new ByteOrderMark("ONE", 0x01);
    private static final ByteOrderMark BOM_TWO_BYTE   = new ByteOrderMark("TWO", 0x01, 0x02);
    private static final ByteOrderMark BOM_THREE_BYTE = new ByteOrderMark("THREE", 0x01, 0x02, 0x03);


    /**
     * Tests that {@link ByteOrderMark#getBytes()} returns the correct byte array for different ByteOrderMark instances.
     * Also verifies that modifications to the returned byte array do not affect the internal state of the ByteOrderMark.
     */
    @Test
    public void testGetBytesReturnsCorrectBytes() {
        // Verify the byte array returned for a single-byte ByteOrderMark
        byte[] expectedOneByte = new byte[] { (byte) 0x01 };
        byte[] actualOneByte = BOM_ONE_BYTE.getBytes();
        assertArrayEquals(expectedOneByte, actualOneByte, "getBytes() for single-byte BOM should return the correct byte array.");

        // Verify that modifying the returned array does not change the ByteOrderMark's original data
        actualOneByte[0] = 0x02; // Modify the returned array
        byte[] actualOneByteAfterModification = BOM_ONE_BYTE.getBytes();  // Get a fresh byte array
        assertArrayEquals(expectedOneByte, actualOneByteAfterModification, "Modifying the returned array should not affect the ByteOrderMark's original data.");


        // Verify the byte array returned for a two-byte ByteOrderMark
        byte[] expectedTwoByte = new byte[] { (byte) 0x01, (byte) 0x02 };
        byte[] actualTwoByte = BOM_TWO_BYTE.getBytes();
        assertArrayEquals(expectedTwoByte, actualTwoByte, "getBytes() for two-byte BOM should return the correct byte array.");

        // Verify the byte array returned for a three-byte ByteOrderMark
        byte[] expectedThreeByte = new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03 };
        byte[] actualThreeByte = BOM_THREE_BYTE.getBytes();
        assertArrayEquals(expectedThreeByte, actualThreeByte, "getBytes() for three-byte BOM should return the correct byte array.");
    }
}