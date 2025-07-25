package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ByteOrderMark} class.
 */
class ByteOrderMarkTest {

    // Test ByteOrderMark instances
    private static final ByteOrderMark TEST_BOM_1 = new ByteOrderMark("test1", 1);
    private static final ByteOrderMark TEST_BOM_2 = new ByteOrderMark("test2", 1, 2);
    private static final ByteOrderMark TEST_BOM_3 = new ByteOrderMark("test3", 1, 2, 3);

    /**
     * Verifies that the charset names of predefined ByteOrderMarks can be loaded as a Charset.
     */
    @Test
    void shouldLoadCharsetNames() {
        assertNotNull(Charset.forName(ByteOrderMark.UTF_8.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_16BE.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_16LE.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_32BE.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_32LE.getCharsetName()));
    }

    /**
     * Tests that the constructor throws exceptions for invalid input.
     */
    @Test
    void shouldThrowExceptionForInvalidConstructorArguments() {
        assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3));
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3));
        assertThrows(NullPointerException.class, () -> new ByteOrderMark("a", (int[]) null));
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("b"));
    }

    /**
     * Tests the equality of ByteOrderMark instances.
     */
    @Test
    void shouldTestEquality() {
        // Test equality of predefined BOMs
        assertEquals(ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16BE);
        assertEquals(ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16LE);
        assertEquals(ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32BE);
        assertEquals(ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32LE);
        assertEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_8);

        // Test inequality between different BOMs
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32BE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32LE);

        // Test equality of test BOMs
        assertEquals(TEST_BOM_1, TEST_BOM_1, "test1 equals");
        assertEquals(TEST_BOM_2, TEST_BOM_2, "test2 equals");
        assertEquals(TEST_BOM_3, TEST_BOM_3, "test3 equals");

        // Test inequality with different objects and BOMs
        assertNotEquals(TEST_BOM_1, new Object(), "Object not equal");
        assertNotEquals(TEST_BOM_1, new ByteOrderMark("1a", 2), "test1-1 not equal");
        assertNotEquals(TEST_BOM_1, new ByteOrderMark("1b", 1, 2), "test1-2 not test2");
        assertNotEquals(TEST_BOM_2, new ByteOrderMark("2", 1, 1), "test2 not equal");
        assertNotEquals(TEST_BOM_3, new ByteOrderMark("3", 1, 2, 4), "test3 not equal");
    }

    /**
     * Tests the byte array representation of ByteOrderMark instances.
     */
    @Test
    void shouldReturnCorrectByteArray() {
        assertArrayEquals(new byte[] { (byte) 1 }, TEST_BOM_1.getBytes(), "test1 bytes");
        assertArrayEquals(new byte[] { (byte) 1, (byte) 2 }, TEST_BOM_2.getBytes(), "test2 bytes");
        assertArrayEquals(new byte[] { (byte) 1, (byte) 2, (byte) 3 }, TEST_BOM_3.getBytes(), "test3 bytes");
    }

    /**
     * Tests the charset name retrieval of ByteOrderMark instances.
     */
    @Test
    void shouldReturnCorrectCharsetName() {
        assertEquals("test1", TEST_BOM_1.getCharsetName(), "test1 name");
        assertEquals("test2", TEST_BOM_2.getCharsetName(), "test2 name");
        assertEquals("test3", TEST_BOM_3.getCharsetName(), "test3 name");
    }

    /**
     * Tests the retrieval of individual bytes from ByteOrderMark instances.
     */
    @Test
    void shouldReturnCorrectByteAtPosition() {
        assertEquals(1, TEST_BOM_1.get(0), "test1 get(0)");
        assertEquals(1, TEST_BOM_2.get(0), "test2 get(0)");
        assertEquals(2, TEST_BOM_2.get(1), "test2 get(1)");
        assertEquals(1, TEST_BOM_3.get(0), "test3 get(0)");
        assertEquals(2, TEST_BOM_3.get(1), "test3 get(1)");
        assertEquals(3, TEST_BOM_3.get(2), "test3 get(2)");
    }

    /**
     * Tests the hash code computation of ByteOrderMark instances.
     */
    @Test
    void shouldComputeCorrectHashCode() {
        final int bomClassHash = ByteOrderMark.class.hashCode();
        assertEquals(bomClassHash + 1, TEST_BOM_1.hashCode(), "hash test1");
        assertEquals(bomClassHash + 3, TEST_BOM_2.hashCode(), "hash test2");
        assertEquals(bomClassHash + 6, TEST_BOM_3.hashCode(), "hash test3");
    }

    /**
     * Tests the length of ByteOrderMark instances.
     */
    @Test
    void shouldReturnCorrectLength() {
        assertEquals(1, TEST_BOM_1.length(), "test1 length");
        assertEquals(2, TEST_BOM_2.length(), "test2 length");
        assertEquals(3, TEST_BOM_3.length(), "test3 length");
    }

    /**
     * Tests the matching functionality of ByteOrderMark instances.
     */
    @Test
    void shouldMatchCorrectly() {
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_16LE.matches(ByteOrderMark.UTF_16LE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_32BE.matches(ByteOrderMark.UTF_32BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_8.matches(ByteOrderMark.UTF_8.getRawBytes()));

        assertTrue(TEST_BOM_1.matches(TEST_BOM_1.getRawBytes()));
        assertTrue(TEST_BOM_2.matches(TEST_BOM_2.getRawBytes()));
        assertTrue(TEST_BOM_3.matches(TEST_BOM_3.getRawBytes()));

        assertFalse(TEST_BOM_1.matches(new ByteOrderMark("1a", 2).getRawBytes()));
        assertTrue(TEST_BOM_1.matches(new ByteOrderMark("1b", 1, 2).getRawBytes()));
        assertFalse(TEST_BOM_2.matches(new ByteOrderMark("2", 1, 1).getRawBytes()));
        assertFalse(TEST_BOM_3.matches(new ByteOrderMark("3", 1, 2, 4).getRawBytes()));
    }

    /**
     * Tests the string representation of ByteOrderMark instances.
     */
    @Test
    void shouldReturnCorrectStringRepresentation() {
        assertEquals("ByteOrderMark[test1: 0x1]", TEST_BOM_1.toString(), "test1");
        assertEquals("ByteOrderMark[test2: 0x1,0x2]", TEST_BOM_2.toString(), "test2");
        assertEquals("ByteOrderMark[test3: 0x1,0x2,0x3]", TEST_BOM_3.toString(), "test3");
    }
}