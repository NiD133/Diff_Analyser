package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

/**
 * Test for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    private static final ByteOrderMark TEST_BOM_1 = new ByteOrderMark("test1", 1);
    private static final ByteOrderMark TEST_BOM_2 = new ByteOrderMark("test2", 1, 2);
    private static final ByteOrderMark TEST_BOM_3 = new ByteOrderMark("test3", 1, 2, 3);

    /**
     * Tests that the charset names of the predefined BOMs can be loaded as a {@link java.nio.charset.Charset}.
     */
    @Test
    public void testPredefinedBomCharsetNames() {
        // Test that the charset names of the predefined BOMs can be loaded as a Charset
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
    public void testConstructorExceptions() {
        // Test that the constructor throws a NullPointerException for a null charset name
        assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3));

        // Test that the constructor throws an IllegalArgumentException for an empty charset name
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3));

        // Test that the constructor throws a NullPointerException for null bytes
        assertThrows(NullPointerException.class, () -> new ByteOrderMark("a", (int[]) null));

        // Test that the constructor throws an IllegalArgumentException for zero-length bytes
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("b"));
    }

    /**
     * Tests the equals method.
     */
    @Test
    public void testEquals() {
        // Test that the predefined BOMs are equal to themselves
        assertEquals(ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16BE);
        assertEquals(ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16LE);
        assertEquals(ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32BE);
        assertEquals(ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32LE);
        assertEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_8);

        // Test that the predefined BOMs are not equal to each other
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32BE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32LE);

        // Test that the test BOMs are equal to themselves
        assertEquals(TEST_BOM_1, TEST_BOM_1);
        assertEquals(TEST_BOM_2, TEST_BOM_2);
        assertEquals(TEST_BOM_3, TEST_BOM_3);

        // Test that the test BOMs are not equal to other objects
        assertNotEquals(TEST_BOM_1, new Object());
        assertNotEquals(TEST_BOM_1, new ByteOrderMark("1a", 2));
        assertNotEquals(TEST_BOM_1, new ByteOrderMark("1b", 1, 2));
        assertNotEquals(TEST_BOM_2, new ByteOrderMark("2", 1, 1));
        assertNotEquals(TEST_BOM_3, new ByteOrderMark("3", 1, 2, 4));
    }

    /**
     * Tests the getBytes method.
     */
    @Test
    public void testGetBytes() {
        // Test that the getBytes method returns a copy of the bytes
        assertArrayEquals(TEST_BOM_1.getBytes(), new byte[] { (byte) 1 });
        TEST_BOM_1.getBytes()[0] = 2;
        assertArrayEquals(TEST_BOM_1.getBytes(), new byte[] { (byte) 1 });

        // Test that the getBytes method returns the correct bytes for the test BOMs
        assertArrayEquals(TEST_BOM_2.getBytes(), new byte[] { (byte) 1, (byte) 2 });
        assertArrayEquals(TEST_BOM_3.getBytes(), new byte[] { (byte) 1, (byte) 2, (byte) 3 });
    }

    /**
     * Tests the getCharsetName method.
     */
    @Test
    public void testGetCharsetName() {
        // Test that the getCharsetName method returns the correct charset name for the test BOMs
        assertEquals("test1", TEST_BOM_1.getCharsetName());
        assertEquals("test2", TEST_BOM_2.getCharsetName());
        assertEquals("test3", TEST_BOM_3.getCharsetName());
    }

    /**
     * Tests the get method.
     */
    @Test
    public void testGet() {
        // Test that the get method returns the correct byte at the specified position
        assertEquals(1, TEST_BOM_1.get(0));
        assertEquals(1, TEST_BOM_2.get(0));
        assertEquals(2, TEST_BOM_2.get(1));
        assertEquals(1, TEST_BOM_3.get(0));
        assertEquals(2, TEST_BOM_3.get(1));
        assertEquals(3, TEST_BOM_3.get(2));
    }

    /**
     * Tests the hashCode method.
     */
    @Test
    public void testHashCode() {
        // Test that the hashCode method returns the correct hash code for the test BOMs
        final int bomClassHash = ByteOrderMark.class.hashCode();
        assertEquals(bomClassHash + 1, TEST_BOM_1.hashCode());
        assertEquals(bomClassHash + 3, TEST_BOM_2.hashCode());
        assertEquals(bomClassHash + 6, TEST_BOM_3.hashCode());
    }

    /**
     * Tests the length method.
     */
    @Test
    public void testLength() {
        // Test that the length method returns the correct length for the test BOMs
        assertEquals(1, TEST_BOM_1.length());
        assertEquals(2, TEST_BOM_2.length());
        assertEquals(3, TEST_BOM_3.length());
    }

    /**
     * Tests the matches method.
     */
    @Test
    public void testMatches() {
        // Test that the matches method returns true for the predefined BOMs
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_16LE.matches(ByteOrderMark.UTF_16LE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_32BE.matches(ByteOrderMark.UTF_32BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_8.matches(ByteOrderMark.UTF_8.getRawBytes()));

        // Test that the matches method returns true for the test BOMs
        assertTrue(TEST_BOM_1.matches(TEST_BOM_1.getRawBytes()));
        assertTrue(TEST_BOM_2.matches(TEST_BOM_2.getRawBytes()));
        assertTrue(TEST_BOM_3.matches(TEST_BOM_3.getRawBytes()));

        // Test that the matches method returns false for different BOMs
        assertFalse(TEST_BOM_1.matches(new ByteOrderMark("1a", 2).getRawBytes()));
        assertTrue(TEST_BOM_1.matches(new ByteOrderMark("1b", 1, 2).getRawBytes()));
        assertFalse(TEST_BOM_2.matches(new ByteOrderMark("2", 1, 1).getRawBytes()));
        assertFalse(TEST_BOM_3.matches(new ByteOrderMark("3", 1, 2, 4).getRawBytes()));
    }

    /**
     * Tests the toString method.
     */
    @Test
    public void testToString() {
        // Test that the toString method returns the correct string representation for the test BOMs
        assertEquals("ByteOrderMark[test1: 0x1]", TEST_BOM_1.toString());
        assertEquals("ByteOrderMark[test2: 0x1,0x2]", TEST_BOM_2.toString());
        assertEquals("ByteOrderMark[test3: 0x1,0x2,0x3]", TEST_BOM_3.toString());
    }
}