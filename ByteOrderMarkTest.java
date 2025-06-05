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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ByteOrderMark} class.
 */
public class ByteOrderMarkTest {

    private ByteOrderMark testBom1;
    private ByteOrderMark testBom2;
    private ByteOrderMark testBom3;

    @BeforeEach
    public void setUp() {
        testBom1 = new ByteOrderMark("test1", 1);
        testBom2 = new ByteOrderMark("test2", 1, 2);
        testBom3 = new ByteOrderMark("test3", 1, 2, 3);
    }

    @Test
    public void testConstantCharsetNames_CanBeLoadedAsCharset() {
        assertNotNull(Charset.forName(ByteOrderMark.UTF_8.getCharsetName()), "UTF_8 charset name should be loadable");
        assertNotNull(Charset.forName(ByteOrderMark.UTF_16BE.getCharsetName()), "UTF_16BE charset name should be loadable");
        assertNotNull(Charset.forName(ByteOrderMark.UTF_16LE.getCharsetName()), "UTF_16LE charset name should be loadable");
        assertNotNull(Charset.forName(ByteOrderMark.UTF_32BE.getCharsetName()), "UTF_32BE charset name should be loadable");
        assertNotNull(Charset.forName(ByteOrderMark.UTF_32LE.getCharsetName()), "UTF_32LE charset name should be loadable");
    }

    @Test
    public void testConstructor_InvalidArguments_ThrowsException() {
        assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3), "Null charset name should throw NPE");
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3), "Empty charset name should throw IAE");
        assertThrows(NullPointerException.class, () -> new ByteOrderMark("a", (int[]) null), "Null byte array should throw NPE");
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("b"), "Empty byte array should throw IAE");
    }

    @Test
    public void testEquals_SameInstance_ReturnsTrue() {
        assertEquals(ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16BE, "UTF_16BE should be equal to itself");
        assertEquals(ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16LE, "UTF_16LE should be equal to itself");
        assertEquals(ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32BE, "UTF_32BE should be equal to itself");
        assertEquals(ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32LE, "UTF_32LE should be equal to itself");
        assertEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_8, "UTF_8 should be equal to itself");

        assertEquals(testBom1, testBom1, "test1 should be equal to itself");
        assertEquals(testBom2, testBom2, "test2 should be equal to itself");
        assertEquals(testBom3, testBom3, "test3 should be equal to itself");
    }

    @Test
    public void testEquals_DifferentInstances_ReturnsFalse() {
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, "UTF_8 should not be equal to UTF_16BE");
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE, "UTF_8 should not be equal to UTF_16LE");
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32BE, "UTF_8 should not be equal to UTF_32BE");
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32LE, "UTF_8 should not be equal to UTF_32LE");

        assertNotEquals(testBom1, new Object(), "Should not be equal to a different object type");
        assertNotEquals(testBom1, new ByteOrderMark("1a", 2), "Should not be equal if bytes are different");
        assertNotEquals(testBom1, new ByteOrderMark("1b", 1, 2), "Should not be equal if byte array length is different");
        assertNotEquals(testBom2, new ByteOrderMark("2", 1, 1), "Should not be equal if bytes are different");
        assertNotEquals(testBom3, new ByteOrderMark("3", 1, 2, 4), "Should not be equal if bytes are different");
    }

    @Test
    public void testGetBytes_ReturnsCopyOfBytes() {
        assertArrayEquals(testBom1.getBytes(), new byte[] { (byte) 1 }, "test1 bytes should match");
        // Verify that modifying the returned byte array does not modify the original BOM
        testBom1.getBytes()[0] = 2;
        assertArrayEquals(testBom1.getBytes(), new byte[] { (byte) 1 }, "test1 bytes should still match");
        assertArrayEquals(testBom2.getBytes(), new byte[] { (byte) 1, (byte) 2 }, "test2 bytes should match");
        assertArrayEquals(testBom3.getBytes(), new byte[] { (byte) 1, (byte) 2, (byte) 3 }, "test3 bytes should match");
    }

    @Test
    public void testGetCharsetName_ReturnsCorrectName() {
        assertEquals("test1", testBom1.getCharsetName(), "test1 name should match");
        assertEquals("test2", testBom2.getCharsetName(), "test2 name should match");
        assertEquals("test3", testBom3.getCharsetName(), "test3 name should match");
    }

    @Test
    public void testGetInt_ReturnsCorrectByteAtPosition() {
        assertEquals(1, testBom1.get(0), "test1 get(0) should match");
        assertEquals(1, testBom2.get(0), "test2 get(0) should match");
        assertEquals(2, testBom2.get(1), "test2 get(1) should match");
        assertEquals(1, testBom3.get(0), "test3 get(0) should match");
        assertEquals(2, testBom3.get(1), "test3 get(1) should match");
        assertEquals(3, testBom3.get(2), "test3 get(2) should match");
    }

    @Test
    public void testHashCode_ConsistentAndCorrect() {
        final int bomClassHash = ByteOrderMark.class.hashCode();
        assertEquals(bomClassHash + 1, testBom1.hashCode(), "hash test1 should match");
        assertEquals(bomClassHash + 3, testBom2.hashCode(), "hash test2 should match");
        assertEquals(bomClassHash + 6, testBom3.hashCode(), "hash test3 should match");
    }

    @Test
    public void testLength_ReturnsCorrectLength() {
        assertEquals(1, testBom1.length(), "test1 length should match");
        assertEquals(2, testBom2.length(), "test2 length should match");
        assertEquals(3, testBom3.length(), "test3 length should match");
    }

    @Test
    public void testMatches_CorrectPrefix_ReturnsTrue() {
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()), "UTF_16BE should match its bytes");
        assertTrue(ByteOrderMark.UTF_16LE.matches(ByteOrderMark.UTF_16LE.getRawBytes()), "UTF_16LE should match its bytes");
        assertTrue(ByteOrderMark.UTF_32BE.matches(ByteOrderMark.UTF_32BE.getRawBytes()), "UTF_32BE should match its bytes");
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()), "UTF_16BE should match its bytes");
        assertTrue(ByteOrderMark.UTF_8.matches(ByteOrderMark.UTF_8.getRawBytes()), "UTF_8 should match its bytes");

        assertTrue(testBom1.matches(testBom1.getRawBytes()), "testBom1 should match its bytes");
        assertTrue(testBom2.matches(testBom2.getRawBytes()), "testBom2 should match its bytes");
        assertTrue(testBom3.matches(testBom3.getRawBytes()), "testBom3 should match its bytes");

        assertTrue(testBom1.matches(new ByteOrderMark("1b", 1, 2).getRawBytes()), "Prefix match should return true");

    }

    @Test
    public void testMatches_IncorrectPrefix_ReturnsFalse() {
        assertFalse(testBom1.matches(new ByteOrderMark("1a", 2).getRawBytes()), "Incorrect prefix match should return false");
        assertFalse(testBom2.matches(new ByteOrderMark("2", 1, 1).getRawBytes()), "Incorrect prefix match should return false");
        assertFalse(testBom3.matches(new ByteOrderMark("3", 1, 2, 4).getRawBytes()), "Incorrect prefix match should return false");
    }

    @Test
    public void testToString_ReturnsCorrectStringRepresentation() {
        assertEquals("ByteOrderMark[test1: 0x1]", testBom1.toString(), "test1 toString should match");
        assertEquals("ByteOrderMark[test2: 0x1,0x2]", testBom2.toString(), "test2 toString should match");
        assertEquals("ByteOrderMark[test3: 0x1,0x2,0x3]", testBom3.toString(), "test3 toString should match");
    }
}