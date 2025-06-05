/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    // Test ByteOrderMark instances
    private static final ByteOrderMark BOM_TEST_1 = new ByteOrderMark("test1", 1);
    private static final ByteOrderMark BOM_TEST_2 = new ByteOrderMark("test2", 1, 2);
    private static final ByteOrderMark BOM_TEST_3 = new ByteOrderMark("test3", 1, 2, 3);

    /**
     * Tests that the charset names of predefined BOMs can be loaded as a {@link Charset}.
     */
    @Test
    public void testCharsetNameLoading() {
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
    public void testConstructorValidation() {
        assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3));
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3));
        assertThrows(NullPointerException.class, () -> new ByteOrderMark("a", (int[]) null));
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("b"));
    }

    /**
     * Tests the equality of ByteOrderMark instances.
     */
    @Test
    public void testEquality() {
        // Test equality of predefined BOMs
        assertEquals(ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16BE);
        assertEquals(ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16LE);
        assertEquals(ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32BE);
        assertEquals(ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32LE);
        assertEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_8);

        // Test inequality of different BOMs
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32BE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32LE);

        // Test equality of test BOMs
        assertEquals(BOM_TEST_1, BOM_TEST_1, "BOM_TEST_1 should be equal to itself");
        assertEquals(BOM_TEST_2, BOM_TEST_2, "BOM_TEST_2 should be equal to itself");
        assertEquals(BOM_TEST_3, BOM_TEST_3, "BOM_TEST_3 should be equal to itself");

        // Test inequality with different objects
        assertNotEquals(BOM_TEST_1, new Object(), "BOM_TEST_1 should not equal a different object");
        assertNotEquals(BOM_TEST_1, new ByteOrderMark("1a", 2), "BOM_TEST_1 should not equal a different BOM");
        assertNotEquals(BOM_TEST_1, new ByteOrderMark("1b", 1, 2), "BOM_TEST_1 should not equal a different BOM");
        assertNotEquals(BOM_TEST_2, new ByteOrderMark("2", 1, 1), "BOM_TEST_2 should not equal a different BOM");
        assertNotEquals(BOM_TEST_3, new ByteOrderMark("3", 1, 2, 4), "BOM_TEST_3 should not equal a different BOM");
    }

    /**
     * Tests the byte array representation of ByteOrderMark instances.
     */
    @Test
    public void testByteArrayRepresentation() {
        assertArrayEquals(BOM_TEST_1.getBytes(), new byte[] { (byte) 1 }, "BOM_TEST_1 byte array mismatch");
        BOM_TEST_1.getBytes()[0] = 2; // Ensure immutability
        assertArrayEquals(BOM_TEST_1.getBytes(), new byte[] { (byte) 1 }, "BOM_TEST_1 byte array should be immutable");
        assertArrayEquals(BOM_TEST_2.getBytes(), new byte[] { (byte) 1, (byte) 2 }, "BOM_TEST_2 byte array mismatch");
        assertArrayEquals(BOM_TEST_3.getBytes(), new byte[] { (byte) 1, (byte) 2, (byte) 3 }, "BOM_TEST_3 byte array mismatch");
    }

    /**
     * Tests the charset name retrieval of ByteOrderMark instances.
     */
    @Test
    public void testCharsetNameRetrieval() {
        assertEquals("test1", BOM_TEST_1.getCharsetName(), "BOM_TEST_1 charset name mismatch");
        assertEquals("test2", BOM_TEST_2.getCharsetName(), "BOM_TEST_2 charset name mismatch");
        assertEquals("test3", BOM_TEST_3.getCharsetName(), "BOM_TEST_3 charset name mismatch");
    }

    /**
     * Tests the retrieval of individual bytes from ByteOrderMark instances.
     */
    @Test
    public void testByteRetrieval() {
        assertEquals(1, BOM_TEST_1.get(0), "BOM_TEST_1 byte retrieval mismatch");
        assertEquals(1, BOM_TEST_2.get(0), "BOM_TEST_2 byte retrieval mismatch");
        assertEquals(2, BOM_TEST_2.get(1), "BOM_TEST_2 byte retrieval mismatch");
        assertEquals(1, BOM_TEST_3.get(0), "BOM_TEST_3 byte retrieval mismatch");
        assertEquals(2, BOM_TEST_3.get(1), "BOM_TEST_3 byte retrieval mismatch");
        assertEquals(3, BOM_TEST_3.get(2), "BOM_TEST_3 byte retrieval mismatch");
    }

    /**
     * Tests the hash code computation of ByteOrderMark instances.
     */
    @Test
    public void testHashCodeComputation() {
        final int bomClassHash = ByteOrderMark.class.hashCode();
        assertEquals(bomClassHash + 1, BOM_TEST_1.hashCode(), "BOM_TEST_1 hash code mismatch");
        assertEquals(bomClassHash + 3, BOM_TEST_2.hashCode(), "BOM_TEST_2 hash code mismatch");
        assertEquals(bomClassHash + 6, BOM_TEST_3.hashCode(), "BOM_TEST_3 hash code mismatch");
    }

    /**
     * Tests the length of ByteOrderMark instances.
     */
    @Test
    public void testLength() {
        assertEquals(1, BOM_TEST_1.length(), "BOM_TEST_1 length mismatch");
        assertEquals(2, BOM_TEST_2.length(), "BOM_TEST_2 length mismatch");
        assertEquals(3, BOM_TEST_3.length(), "BOM_TEST_3 length mismatch");
    }

    /**
     * Tests the matching functionality of ByteOrderMark instances.
     */
    @Test
    public void testMatching() {
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_16LE.matches(ByteOrderMark.UTF_16LE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_32BE.matches(ByteOrderMark.UTF_32BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_8.matches(ByteOrderMark.UTF_8.getRawBytes()));

        assertTrue(BOM_TEST_1.matches(BOM_TEST_1.getRawBytes()));
        assertTrue(BOM_TEST_2.matches(BOM_TEST_2.getRawBytes()));
        assertTrue(BOM_TEST_3.matches(BOM_TEST_3.getRawBytes()));

        assertFalse(BOM_TEST_1.matches(new ByteOrderMark("1a", 2).getRawBytes()));
        assertTrue(BOM_TEST_1.matches(new ByteOrderMark("1b", 1, 2).getRawBytes()));
        assertFalse(BOM_TEST_2.matches(new ByteOrderMark("2", 1, 1).getRawBytes()));
        assertFalse(BOM_TEST_3.matches(new ByteOrderMark("3", 1, 2, 4).getRawBytes()));
    }

    /**
     * Tests the string representation of ByteOrderMark instances.
     */
    @Test
    public void testToStringRepresentation() {
        assertEquals("ByteOrderMark[test1: 0x1]", BOM_TEST_1.toString(), "BOM_TEST_1 string representation mismatch");
        assertEquals("ByteOrderMark[test2: 0x1,0x2]", BOM_TEST_2.toString(), "BOM_TEST_2 string representation mismatch");
        assertEquals("ByteOrderMark[test3: 0x1,0x2,0x3]", BOM_TEST_3.toString(), "BOM_TEST_3 string representation mismatch");
    }
}