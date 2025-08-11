package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ByteOrderMark} class.
 */
class ByteOrderMarkTest {

    // Test ByteOrderMark instances with different byte sequences
    private static final ByteOrderMark BOM_TEST_1 = new ByteOrderMark("test1", 1);
    private static final ByteOrderMark BOM_TEST_2 = new ByteOrderMark("test2", 1, 2);
    private static final ByteOrderMark BOM_TEST_3 = new ByteOrderMark("test3", 1, 2, 3);

    /**
     * Verifies that the charset names of predefined BOMs can be loaded as a Charset.
     */
    @Test
    void testCharsetNameLoading() {
        assertNotNull(Charset.forName(ByteOrderMark.UTF_8.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_16BE.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_16LE.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_32BE.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_32LE.getCharsetName()));
    }

    /**
     * Tests that exceptions are thrown for invalid ByteOrderMark constructor arguments.
     */
    @Test
    void testConstructorArgumentValidation() {
        assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3));
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3));
        assertThrows(NullPointerException.class, () -> new ByteOrderMark("a", (int[]) null));
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("b"));
    }

    /**
     * Tests the equality logic of ByteOrderMark instances.
     */
    @Test
    void testEquality() {
        // Test equality of predefined BOMs
        assertEquals(ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16BE);
        assertEquals(ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16LE);
        assertEquals(ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32BE);
        assertEquals(ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32LE);
        assertEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_8);

        // Test inequality between different predefined BOMs
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32BE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32LE);

        // Test equality and inequality of test BOMs
        assertEquals(BOM_TEST_1, BOM_TEST_1, "BOM_TEST_1 should be equal to itself");
        assertEquals(BOM_TEST_2, BOM_TEST_2, "BOM_TEST_2 should be equal to itself");
        assertEquals(BOM_TEST_3, BOM_TEST_3, "BOM_TEST_3 should be equal to itself");

        assertNotEquals(BOM_TEST_1, new Object(), "BOM_TEST_1 should not equal a different object type");
        assertNotEquals(BOM_TEST_1, new ByteOrderMark("1a", 2), "BOM_TEST_1 should not equal a different BOM");
        assertNotEquals(BOM_TEST_1, new ByteOrderMark("1b", 1, 2), "BOM_TEST_1 should not equal a BOM with different bytes");
        assertNotEquals(BOM_TEST_2, new ByteOrderMark("2", 1, 1), "BOM_TEST_2 should not equal a BOM with different bytes");
        assertNotEquals(BOM_TEST_3, new ByteOrderMark("3", 1, 2, 4), "BOM_TEST_3 should not equal a BOM with different bytes");
    }

    /**
     * Tests the byte array retrieval from ByteOrderMark instances.
     */
    @Test
    void testByteArrayRetrieval() {
        assertArrayEquals(BOM_TEST_1.getBytes(), new byte[] { (byte) 1 }, "BOM_TEST_1 byte array mismatch");
        BOM_TEST_1.getBytes()[0] = 2; // Attempt to modify the byte array
        assertArrayEquals(BOM_TEST_1.getBytes(), new byte[] { (byte) 1 }, "BOM_TEST_1 byte array should be immutable");

        assertArrayEquals(BOM_TEST_2.getBytes(), new byte[] { (byte) 1, (byte) 2 }, "BOM_TEST_2 byte array mismatch");
        assertArrayEquals(BOM_TEST_3.getBytes(), new byte[] { (byte) 1, (byte) 2, (byte) 3 }, "BOM_TEST_3 byte array mismatch");
    }

    /**
     * Tests the retrieval of charset names from ByteOrderMark instances.
     */
    @Test
    void testCharsetNameRetrieval() {
        assertEquals("test1", BOM_TEST_1.getCharsetName(), "BOM_TEST_1 charset name mismatch");
        assertEquals("test2", BOM_TEST_2.getCharsetName(), "BOM_TEST_2 charset name mismatch");
        assertEquals("test3", BOM_TEST_3.getCharsetName(), "BOM_TEST_3 charset name mismatch");
    }

    /**
     * Tests the retrieval of individual bytes from ByteOrderMark instances.
     */
    @Test
    void testByteRetrievalByIndex() {
        assertEquals(1, BOM_TEST_1.get(0), "BOM_TEST_1 byte retrieval mismatch");
        assertEquals(1, BOM_TEST_2.get(0), "BOM_TEST_2 byte retrieval mismatch at index 0");
        assertEquals(2, BOM_TEST_2.get(1), "BOM_TEST_2 byte retrieval mismatch at index 1");
        assertEquals(1, BOM_TEST_3.get(0), "BOM_TEST_3 byte retrieval mismatch at index 0");
        assertEquals(2, BOM_TEST_3.get(1), "BOM_TEST_3 byte retrieval mismatch at index 1");
        assertEquals(3, BOM_TEST_3.get(2), "BOM_TEST_3 byte retrieval mismatch at index 2");
    }

    /**
     * Tests the hash code generation for ByteOrderMark instances.
     */
    @Test
    void testHashCodeGeneration() {
        final int bomClassHash = ByteOrderMark.class.hashCode();
        assertEquals(bomClassHash + 1, BOM_TEST_1.hashCode(), "BOM_TEST_1 hash code mismatch");
        assertEquals(bomClassHash + 3, BOM_TEST_2.hashCode(), "BOM_TEST_2 hash code mismatch");
        assertEquals(bomClassHash + 6, BOM_TEST_3.hashCode(), "BOM_TEST_3 hash code mismatch");
    }

    /**
     * Tests the length retrieval of ByteOrderMark instances.
     */
    @Test
    void testLengthRetrieval() {
        assertEquals(1, BOM_TEST_1.length(), "BOM_TEST_1 length mismatch");
        assertEquals(2, BOM_TEST_2.length(), "BOM_TEST_2 length mismatch");
        assertEquals(3, BOM_TEST_3.length(), "BOM_TEST_3 length mismatch");
    }

    /**
     * Tests the byte sequence matching functionality of ByteOrderMark instances.
     */
    @Test
    void testByteSequenceMatching() {
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()), "UTF_16BE should match its own bytes");
        assertTrue(ByteOrderMark.UTF_16LE.matches(ByteOrderMark.UTF_16LE.getRawBytes()), "UTF_16LE should match its own bytes");
        assertTrue(ByteOrderMark.UTF_32BE.matches(ByteOrderMark.UTF_32BE.getRawBytes()), "UTF_32BE should match its own bytes");
        assertTrue(ByteOrderMark.UTF_8.matches(ByteOrderMark.UTF_8.getRawBytes()), "UTF_8 should match its own bytes");

        assertTrue(BOM_TEST_1.matches(BOM_TEST_1.getRawBytes()), "BOM_TEST_1 should match its own bytes");
        assertTrue(BOM_TEST_2.matches(BOM_TEST_2.getRawBytes()), "BOM_TEST_2 should match its own bytes");
        assertTrue(BOM_TEST_3.matches(BOM_TEST_3.getRawBytes()), "BOM_TEST_3 should match its own bytes");

        assertFalse(BOM_TEST_1.matches(new ByteOrderMark("1a", 2).getRawBytes()), "BOM_TEST_1 should not match different bytes");
        assertTrue(BOM_TEST_1.matches(new ByteOrderMark("1b", 1, 2).getRawBytes()), "BOM_TEST_1 should match compatible bytes");
        assertFalse(BOM_TEST_2.matches(new ByteOrderMark("2", 1, 1).getRawBytes()), "BOM_TEST_2 should not match different bytes");
        assertFalse(BOM_TEST_3.matches(new ByteOrderMark("3", 1, 2, 4).getRawBytes()), "BOM_TEST_3 should not match different bytes");
    }

    /**
     * Tests the string representation of ByteOrderMark instances.
     */
    @Test
    void testToStringRepresentation() {
        assertEquals("ByteOrderMark[test1: 0x1]", BOM_TEST_1.toString(), "BOM_TEST_1 string representation mismatch");
        assertEquals("ByteOrderMark[test2: 0x1,0x2]", BOM_TEST_2.toString(), "BOM_TEST_2 string representation mismatch");
        assertEquals("ByteOrderMark[test3: 0x1,0x2,0x3]", BOM_TEST_3.toString(), "BOM_TEST_3 string representation mismatch");
    }
}