package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ByteOrderMark} class.
 *
 * <p>This class verifies the functionality of ByteOrderMark, including:
 *   - Correct charset name retrieval
 *   - Exception handling during construction
 *   - Equality checks
 *   - Byte representation
 *   - Access to individual bytes
 *   - Hash code generation
 *   - Length reporting
 *   - Byte sequence matching
 *   - String representation
 */
public class ByteOrderMarkTest {

    // Define some custom ByteOrderMark instances for testing
    private static final ByteOrderMark TEST_BOM_1 = new ByteOrderMark("test1", 1);  // Charset: "test1", bytes: {1}
    private static final ByteOrderMark TEST_BOM_2 = new ByteOrderMark("test2", 1, 2); // Charset: "test2", bytes: {1, 2}
    private static final ByteOrderMark TEST_BOM_3 = new ByteOrderMark("test3", 1, 2, 3); // Charset: "test3", bytes: {1, 2, 3}

    /**
     * Tests that the character set names associated with the predefined ByteOrderMark constants
     * can be successfully used to obtain a Charset instance. This ensures that the charset names
     * are valid and supported by the Java runtime.
     */
    @Test
    public void testConstantCharsetNames() {
        assertNotNull(Charset.forName(ByteOrderMark.UTF_8.getCharsetName()), "UTF-8 charset should be supported");
        assertNotNull(Charset.forName(ByteOrderMark.UTF_16BE.getCharsetName()), "UTF-16BE charset should be supported");
        assertNotNull(Charset.forName(ByteOrderMark.UTF_16LE.getCharsetName()), "UTF-16LE charset should be supported");
        assertNotNull(Charset.forName(ByteOrderMark.UTF_32BE.getCharsetName()), "UTF-32BE charset should be supported");
        assertNotNull(Charset.forName(ByteOrderMark.UTF_32LE.getCharsetName()), "UTF-32LE charset should be supported");
    }

    /**
     * Tests that the ByteOrderMark constructor throws the expected exceptions when invalid arguments
     * are provided.  Specifically, it verifies that a NullPointerException is thrown when the charset name
     * is null or when the byte array is null, and that an IllegalArgumentException is thrown when the charset name
     * is empty or when no bytes are provided.
     */
    @Test
    public void testConstructorExceptions() {
        assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3), "Should throw NullPointerException for null charset name");
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3), "Should throw IllegalArgumentException for empty charset name");
        assertThrows(NullPointerException.class, () -> new ByteOrderMark("a", (int[]) null), "Should throw NullPointerException for null byte array");
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("b"), "Should throw IllegalArgumentException for no bytes provided");
    }

    /**
     * Tests the {@link ByteOrderMark#equals(Object)} method to ensure that ByteOrderMark instances
     * are compared correctly. It tests equality with itself, inequality with different instances,
     * and inequality with other object types.
     */
    @SuppressWarnings("EqualsWithItself")
    @Test
    public void testEquals() {
        assertEquals(ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16BE, "UTF_16BE should equal itself");
        assertEquals(ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16LE, "UTF_16LE should equal itself");
        assertEquals(ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32BE, "UTF_32BE should equal itself");
        assertEquals(ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32LE, "UTF_32LE should equal itself");
        assertEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_8, "UTF_8 should equal itself");

        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, "UTF_8 should not equal UTF_16BE");
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE, "UTF_8 should not equal UTF_16LE");
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32BE, "UTF_8 should not equal UTF_32BE");
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32LE, "UTF_8 should not equal UTF_32LE");

        assertEquals(TEST_BOM_1, TEST_BOM_1, "test1 should equal itself");
        assertEquals(TEST_BOM_2, TEST_BOM_2, "test2 should equal itself");
        assertEquals(TEST_BOM_3, TEST_BOM_3, "test3 should equal itself");

        assertNotEquals(TEST_BOM_1, new Object(), "Should not equal a generic object");
        assertNotEquals(TEST_BOM_1, new ByteOrderMark("1a", 2), "Should not equal different BOM with different bytes");
        assertNotEquals(TEST_BOM_1, new ByteOrderMark("1b", 1, 2), "Should not equal different BOM with different charset and bytes");
        assertNotEquals(TEST_BOM_2, new ByteOrderMark("2", 1, 1), "Should not equal different BOM with different bytes");
        assertNotEquals(TEST_BOM_3, new ByteOrderMark("3", 1, 2, 4), "Should not equal different BOM with different bytes");
    }

    /**
     * Tests the {@link ByteOrderMark#getBytes()} method to ensure that it returns the correct byte array
     * representing the ByteOrderMark.  It also verifies that the returned array is a copy and modifications
     * to the array do not affect the ByteOrderMark instance.
     */
    @Test
    public void testGetBytes() {
        assertArrayEquals(TEST_BOM_1.getBytes(), new byte[] { (byte) 1 }, "test1 bytes should be {1}");
        byte[] bom1Bytes = TEST_BOM_1.getBytes();
        bom1Bytes[0] = 2; // Modify the returned array
        assertArrayEquals(TEST_BOM_1.getBytes(), new byte[] { (byte) 1 }, "Modifying returned array should not affect BOM");
        assertArrayEquals(TEST_BOM_2.getBytes(), new byte[] { (byte) 1, (byte) 2 }, "test2 bytes should be {1, 2}");
        assertArrayEquals(TEST_BOM_3.getBytes(), new byte[] { (byte) 1, (byte) 2, (byte) 3 }, "test3 bytes should be {1, 2, 3}");
    }

    /**
     * Tests the {@link ByteOrderMark#getCharsetName()} method to ensure that it returns the correct
     * character set name associated with the ByteOrderMark.
     */
    @Test
    public void testGetCharsetName() {
        assertEquals("test1", TEST_BOM_1.getCharsetName(), "test1 name should be 'test1'");
        assertEquals("test2", TEST_BOM_2.getCharsetName(), "test2 name should be 'test2'");
        assertEquals("test3", TEST_BOM_3.getCharsetName(), "test3 name should be 'test3'");
    }

    /**
     * Tests the {@link ByteOrderMark#get(int)} method to ensure that it returns the correct byte value
     * at the specified index within the ByteOrderMark's byte sequence.
     */
    @Test
    public void testGetInt() {
        assertEquals(1, TEST_BOM_1.get(0), "test1 get(0) should be 1");
        assertEquals(1, TEST_BOM_2.get(0), "test2 get(0) should be 1");
        assertEquals(2, TEST_BOM_2.get(1), "test2 get(1) should be 2");
        assertEquals(1, TEST_BOM_3.get(0), "test3 get(0) should be 1");
        assertEquals(2, TEST_BOM_3.get(1), "test3 get(1) should be 2");
        assertEquals(3, TEST_BOM_3.get(2), "test3 get(2) should be 3");
    }

    /**
     * Tests the {@link ByteOrderMark#hashCode()} method to ensure that it generates a consistent
     * hash code for each ByteOrderMark instance.
     */
    @Test
    public void testHashCode() {
        final int bomClassHash = ByteOrderMark.class.hashCode();
        assertEquals(bomClassHash + 1, TEST_BOM_1.hashCode(), "hash test1 should be bomClassHash + 1");
        assertEquals(bomClassHash + 3, TEST_BOM_2.hashCode(), "hash test2 should be bomClassHash + 3");
        assertEquals(bomClassHash + 6, TEST_BOM_3.hashCode(), "hash test3 should be bomClassHash + 6");
    }

    /**
     * Tests the {@link ByteOrderMark#length()} method to ensure that it returns the correct length
     * (number of bytes) of the ByteOrderMark.
     */
    @Test
    public void testLength() {
        assertEquals(1, TEST_BOM_1.length(), "test1 length should be 1");
        assertEquals(2, TEST_BOM_2.length(), "test2 length should be 2");
        assertEquals(3, TEST_BOM_3.length(), "test3 length should be 3");
    }

    /**
     * Tests the {@link ByteOrderMark#matches(byte[])} method to ensure that it correctly identifies whether
     * a given byte array matches the ByteOrderMark's byte sequence.
     */
    @Test
    public void testMatches() {
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()), "UTF_16BE should match its raw bytes");
        assertTrue(ByteOrderMark.UTF_16LE.matches(ByteOrderMark.UTF_16LE.getRawBytes()), "UTF_16LE should match its raw bytes");
        assertTrue(ByteOrderMark.UTF_32BE.matches(ByteOrderMark.UTF_32BE.getRawBytes()), "UTF_32BE should match its raw bytes");
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()), "UTF_16BE should match its raw bytes");
        assertTrue(ByteOrderMark.UTF_8.matches(ByteOrderMark.UTF_8.getRawBytes()), "UTF_8 should match its raw bytes");

        assertTrue(TEST_BOM_1.matches(TEST_BOM_1.getRawBytes()), "test1 should match its raw bytes");
        assertTrue(TEST_BOM_2.matches(TEST_BOM_2.getRawBytes()), "test2 should match its raw bytes");
        assertTrue(TEST_BOM_3.matches(TEST_BOM_3.getRawBytes()), "test3 should match its raw bytes");

        assertFalse(TEST_BOM_1.matches(new ByteOrderMark("1a", 2).getRawBytes()), "test1 should not match different BOM with different bytes");
        assertTrue(TEST_BOM_1.matches(new ByteOrderMark("1b", 1, 2).getRawBytes()), "test1 should match different BOM with superset bytes");
        assertFalse(TEST_BOM_2.matches(new ByteOrderMark("2", 1, 1).getRawBytes()), "test2 should not match different BOM with different bytes");
        assertFalse(TEST_BOM_3.matches(new ByteOrderMark("3", 1, 2, 4).getRawBytes()), "test3 should not match different BOM with different bytes");
    }

    /**
     * Tests the {@link ByteOrderMark#toString()} method to ensure that it returns a descriptive string
     * representation of the ByteOrderMark.
     */
    @Test
    public void testToString() {
        assertEquals("ByteOrderMark[test1: 0x1]", TEST_BOM_1.toString(), "test1 toString should be 'ByteOrderMark[test1: 0x1]'");
        assertEquals("ByteOrderMark[test2: 0x1,0x2]", TEST_BOM_2.toString(), "test2 toString should be 'ByteOrderMark[test2: 0x1,0x2]'");
        assertEquals("ByteOrderMark[test3: 0x1,0x2,0x3]", TEST_BOM_3.toString(), "test3 toString should be 'ByteOrderMark[test3: 0x1,0x2,0x3]'");
    }
}