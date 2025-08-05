/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
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

import org.junit.jupiter.api.Test;

/**
 * Test for {@link ByteOrderMark}.
 */
class ByteOrderMarkTest {

    private static final ByteOrderMark TEST_BOM_1 = new ByteOrderMark("test1", 1);
    private static final ByteOrderMark TEST_BOM_2 = new ByteOrderMark("test2", 1, 2);
    private static final ByteOrderMark TEST_BOM_3 = new ByteOrderMark("test3", 1, 2, 3);

    @Test
    void charsetName_ShouldBeLoadableForStandardBOMs() {
        assertNotNull(Charset.forName(ByteOrderMark.UTF_8.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_16BE.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_16LE.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_32BE.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_32LE.getCharsetName()));
    }

    @Test
    void constructor_ShouldThrowException_WhenInvalidArguments() {
        // Null charset name
        assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3));
        
        // Empty charset name
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3));
        
        // Null bytes array
        assertThrows(NullPointerException.class, () -> new ByteOrderMark("a", (int[]) null));
        
        // Empty bytes array
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("b"));
    }

    @Test
    void equals_ShouldReturnTrue_WhenComparingSameInstance() {
        assertEquals(ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16BE);
        assertEquals(ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16LE);
        assertEquals(ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32BE);
        assertEquals(ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32LE);
        assertEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_8);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingDifferentConstants() {
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32BE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32LE);
    }

    @Test
    void equals_ShouldReturnTrue_WhenComparingSameTestBOMInstance() {
        assertEquals(TEST_BOM_1, TEST_BOM_1);
        assertEquals(TEST_BOM_2, TEST_BOM_2);
        assertEquals(TEST_BOM_3, TEST_BOM_3);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingDifferentTestBOMs() {
        // Different single-byte BOM
        assertNotEquals(TEST_BOM_1, new ByteOrderMark("1a", 2));
        
        // Different length (1 vs 2 bytes)
        assertNotEquals(TEST_BOM_1, new ByteOrderMark("1b", 1, 2));
        
        // Same length but different second byte
        assertNotEquals(TEST_BOM_2, new ByteOrderMark("2", 1, 1));
        
        // Same first two bytes but different third byte
        assertNotEquals(TEST_BOM_3, new ByteOrderMark("3", 1, 2, 4));
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingWithNonBOMObject() {
        assertNotEquals(TEST_BOM_1, new Object());
    }

    @Test
    void getBytes_ShouldReturnExpectedByteArray() {
        assertArrayEquals(new byte[] {(byte) 1}, TEST_BOM_1.getBytes());
        assertArrayEquals(new byte[] {(byte) 1, (byte) 2}, TEST_BOM_2.getBytes());
        assertArrayEquals(new byte[] {(byte) 1, (byte) 2, (byte) 3}, TEST_BOM_3.getBytes());
    }

    @Test
    void getBytes_ShouldReturnDefensiveCopy() {
        // Modify returned array
        byte[] bom1Bytes = TEST_BOM_1.getBytes();
        bom1Bytes[0] = 2;

        // Original should remain unchanged
        assertArrayEquals(new byte[] {(byte) 1}, TEST_BOM_1.getBytes());
    }

    @Test
    void getCharsetName_ShouldReturnExpectedName() {
        assertEquals("test1", TEST_BOM_1.getCharsetName());
        assertEquals("test2", TEST_BOM_2.getCharsetName());
        assertEquals("test3", TEST_BOM_3.getCharsetName());
    }

    @Test
    void get_ShouldReturnExpectedByteAtPosition() {
        assertEquals(1, TEST_BOM_1.get(0));
        
        assertEquals(1, TEST_BOM_2.get(0));
        assertEquals(2, TEST_BOM_2.get(1));
        
        assertEquals(1, TEST_BOM_3.get(0));
        assertEquals(2, TEST_BOM_3.get(1));
        assertEquals(3, TEST_BOM_3.get(2));
    }

    @Test
    void hashCode_ShouldReturnExpectedValue() {
        final int bomClassHash = ByteOrderMark.class.hashCode();
        assertEquals(bomClassHash + 1, TEST_BOM_1.hashCode());
        assertEquals(bomClassHash + 3, TEST_BOM_2.hashCode());
        assertEquals(bomClassHash + 6, TEST_BOM_3.hashCode());
    }

    @Test
    void length_ShouldReturnNumberOfBytes() {
        assertEquals(1, TEST_BOM_1.length());
        assertEquals(2, TEST_BOM_2.length());
        assertEquals(3, TEST_BOM_3.length());
    }

    @Test
    void matches_ShouldReturnTrue_WhenBytesExactlyMatch() {
        // Standard BOMs
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_16LE.matches(ByteOrderMark.UTF_16LE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_32BE.matches(ByteOrderMark.UTF_32BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_8.matches(ByteOrderMark.UTF_8.getRawBytes()));
        
        // Test BOMs
        assertTrue(TEST_BOM_1.matches(TEST_BOM_1.getRawBytes()));
        assertTrue(TEST_BOM_2.matches(TEST_BOM_2.getRawBytes()));
        assertTrue(TEST_BOM_3.matches(TEST_BOM_3.getRawBytes()));
    }

    @Test
    void matches_ShouldReturnFalse_WhenBytesDoNotMatch() {
        // Different single byte
        assertFalse(TEST_BOM_1.matches(new ByteOrderMark("1a", 2).getRawBytes()));
        
        // Different length (prefix matches but length differs)
        assertFalse(TEST_BOM_1.matches(new ByteOrderMark("1b", 1, 2).getRawBytes()));
        
        // Same length but different second byte
        assertFalse(TEST_BOM_2.matches(new ByteOrderMark("2", 1, 1).getRawBytes()));
        
        // Same first two bytes but different third byte
        assertFalse(TEST_BOM_3.matches(new ByteOrderMark("3", 1, 2, 4).getRawBytes()));
    }

    @Test
    void toString_ShouldReturnExpectedFormat() {
        assertEquals("ByteOrderMark[test1: 0x1]", TEST_BOM_1.toString());
        assertEquals("ByteOrderMark[test2: 0x1,0x2]", TEST_BOM_2.toString());
        assertEquals("ByteOrderMark[test3: 0x1,0x2,0x3]", TEST_BOM_3.toString());
    }
}