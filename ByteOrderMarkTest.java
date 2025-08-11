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
 * Test suite for {@link ByteOrderMark} class functionality.
 * Tests BOM creation, validation, comparison, and utility methods.
 */
class ByteOrderMarkTest {

    // Test fixtures with different byte sequences for comprehensive testing
    private static final ByteOrderMark SINGLE_BYTE_BOM = new ByteOrderMark("test1", 1);
    private static final ByteOrderMark TWO_BYTE_BOM = new ByteOrderMark("test2", 1, 2);
    private static final ByteOrderMark THREE_BYTE_BOM = new ByteOrderMark("test3", 1, 2, 3);

    @Test
    void shouldLoadAllPredefinedCharsetNamesAsValidCharsets() {
        // Verify that all predefined BOM constants have valid charset names
        // that can be loaded by the Java Charset API
        assertNotNull(Charset.forName(ByteOrderMark.UTF_8.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_16BE.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_16LE.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_32BE.getCharsetName()));
        assertNotNull(Charset.forName(ByteOrderMark.UTF_32LE.getCharsetName()));
    }

    @Test
    void shouldThrowExceptionForInvalidConstructorArguments() {
        // Test null charset name
        assertThrows(NullPointerException.class, 
            () -> new ByteOrderMark(null, 1, 2, 3),
            "Should reject null charset name");
        
        // Test empty charset name
        assertThrows(IllegalArgumentException.class, 
            () -> new ByteOrderMark("", 1, 2, 3),
            "Should reject empty charset name");
        
        // Test null bytes array
        assertThrows(NullPointerException.class, 
            () -> new ByteOrderMark("charset", (int[]) null),
            "Should reject null bytes array");
        
        // Test empty bytes array
        assertThrows(IllegalArgumentException.class, 
            () -> new ByteOrderMark("charset"),
            "Should reject empty bytes array");
    }

    @Test
    void shouldImplementEqualsCorrectlyForSameAndDifferentBOMs() {
        // Test reflexivity - objects should equal themselves
        assertEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_8);
        assertEquals(ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16BE);
        assertEquals(ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16LE);
        assertEquals(ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32BE);
        assertEquals(ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32LE);

        // Test that different predefined BOMs are not equal
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32BE);
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32LE);

        // Test custom BOMs equal themselves
        assertEquals(SINGLE_BYTE_BOM, SINGLE_BYTE_BOM);
        assertEquals(TWO_BYTE_BOM, TWO_BYTE_BOM);
        assertEquals(THREE_BYTE_BOM, THREE_BYTE_BOM);

        // Test inequality with different object types and different BOMs
        assertNotEquals(SINGLE_BYTE_BOM, new Object(), "BOM should not equal non-BOM object");
        assertNotEquals(SINGLE_BYTE_BOM, new ByteOrderMark("different", 2), "BOMs with different bytes should not be equal");
        assertNotEquals(SINGLE_BYTE_BOM, new ByteOrderMark("different", 1, 2), "BOMs with different lengths should not be equal");
        assertNotEquals(TWO_BYTE_BOM, new ByteOrderMark("different", 1, 1), "BOMs with same length but different bytes should not be equal");
        assertNotEquals(THREE_BYTE_BOM, new ByteOrderMark("different", 1, 2, 4), "BOMs with different last byte should not be equal");
    }

    @Test
    void shouldReturnImmutableCopyOfBytesArray() {
        // Verify correct byte arrays are returned
        assertArrayEquals(new byte[]{1}, SINGLE_BYTE_BOM.getBytes());
        assertArrayEquals(new byte[]{1, 2}, TWO_BYTE_BOM.getBytes());
        assertArrayEquals(new byte[]{1, 2, 3}, THREE_BYTE_BOM.getBytes());
        
        // Verify immutability - modifying returned array should not affect original
        byte[] modifiableBytes = SINGLE_BYTE_BOM.getBytes();
        modifiableBytes[0] = 99; // Attempt to modify
        assertArrayEquals(new byte[]{1}, SINGLE_BYTE_BOM.getBytes(), "Original BOM bytes should remain unchanged");
    }

    @Test
    void shouldReturnCorrectCharsetNames() {
        assertEquals("test1", SINGLE_BYTE_BOM.getCharsetName());
        assertEquals("test2", TWO_BYTE_BOM.getCharsetName());
        assertEquals("test3", THREE_BYTE_BOM.getCharsetName());
    }

    @Test
    void shouldReturnCorrectByteAtSpecificIndex() {
        // Test single byte BOM
        assertEquals(1, SINGLE_BYTE_BOM.get(0));
        
        // Test two byte BOM
        assertEquals(1, TWO_BYTE_BOM.get(0));
        assertEquals(2, TWO_BYTE_BOM.get(1));
        
        // Test three byte BOM
        assertEquals(1, THREE_BYTE_BOM.get(0));
        assertEquals(2, THREE_BYTE_BOM.get(1));
        assertEquals(3, THREE_BYTE_BOM.get(2));
    }

    @Test
    void shouldCalculateHashCodeBasedOnByteSum() {
        final int baseHashCode = ByteOrderMark.class.hashCode();
        
        // Hash code should be class hash + sum of bytes
        assertEquals(baseHashCode + 1, SINGLE_BYTE_BOM.hashCode(), "Hash should be base + 1");
        assertEquals(baseHashCode + 3, TWO_BYTE_BOM.hashCode(), "Hash should be base + 1 + 2 = 3");
        assertEquals(baseHashCode + 6, THREE_BYTE_BOM.hashCode(), "Hash should be base + 1 + 2 + 3 = 6");
    }

    @Test
    void shouldReturnCorrectBOMLength() {
        assertEquals(1, SINGLE_BYTE_BOM.length());
        assertEquals(2, TWO_BYTE_BOM.length());
        assertEquals(3, THREE_BYTE_BOM.length());
    }

    @Test
    void shouldMatchBOMsWithSameBytesAndRejectDifferentOnes() {
        // Predefined BOMs should match their own byte sequences
        assertTrue(ByteOrderMark.UTF_8.matches(ByteOrderMark.UTF_8.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_16LE.matches(ByteOrderMark.UTF_16LE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_32BE.matches(ByteOrderMark.UTF_32BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_32LE.matches(ByteOrderMark.UTF_32LE.getRawBytes()));

        // Custom BOMs should match their own byte sequences
        assertTrue(SINGLE_BYTE_BOM.matches(SINGLE_BYTE_BOM.getRawBytes()));
        assertTrue(TWO_BYTE_BOM.matches(TWO_BYTE_BOM.getRawBytes()));
        assertTrue(THREE_BYTE_BOM.matches(THREE_BYTE_BOM.getRawBytes()));

        // Test matching behavior with different byte sequences
        assertFalse(SINGLE_BYTE_BOM.matches(new ByteOrderMark("different", 2).getRawBytes()), 
            "Should not match BOM with different bytes");
        assertTrue(SINGLE_BYTE_BOM.matches(new ByteOrderMark("longer", 1, 2).getRawBytes()), 
            "Should match if target starts with BOM bytes (prefix match)");
        assertFalse(TWO_BYTE_BOM.matches(new ByteOrderMark("different", 1, 1).getRawBytes()), 
            "Should not match BOM with different second byte");
        assertFalse(THREE_BYTE_BOM.matches(new ByteOrderMark("different", 1, 2, 4).getRawBytes()), 
            "Should not match BOM with different third byte");
    }

    @Test
    void shouldGenerateReadableStringRepresentation() {
        assertEquals("ByteOrderMark[test1: 0x1]", SINGLE_BYTE_BOM.toString());
        assertEquals("ByteOrderMark[test2: 0x1,0x2]", TWO_BYTE_BOM.toString());
        assertEquals("ByteOrderMark[test3: 0x1,0x2,0x3]", THREE_BYTE_BOM.toString());
    }
}