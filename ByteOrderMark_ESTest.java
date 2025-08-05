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

import org.junit.Test;
import static org.junit.Assert.*;

// Statically import BOM constants for cleaner test code
import static org.apache.commons.io.ByteOrderMark.UTF_16BE;
import static org.apache.commons.io.ByteOrderMark.UTF_16LE;
import static org.apache.commons.io.ByteOrderMark.UTF_32BE;
import static org.apache.commons.io.ByteOrderMark.UTF_32LE;
import static org.apache.commons.io.ByteOrderMark.UTF_8;

/**
 * Unit tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    private final ByteOrderMark customBom = new ByteOrderMark("CUSTOM", 1, 2, 3);

    // --- Constructor Tests ---

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithEmptyCharsetNameShouldThrowException() {
        new ByteOrderMark("", 1, 2, 3);
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullCharsetNameShouldThrowException() {
        new ByteOrderMark(null, 1, 2, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithEmptyBytesShouldThrowException() {
        new ByteOrderMark("EMPTY_BOM", new int[0]);
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullBytesShouldThrowException() {
        new ByteOrderMark("NULL_BOM", null);
    }

    // --- Getter Method Tests ---

    @Test
    public void getCharsetNameShouldReturnCorrectName() {
        assertEquals("UTF-8", UTF_8.getCharsetName());
        assertEquals("CUSTOM", customBom.getCharsetName());
    }

    @Test
    public void lengthShouldReturnCorrectNumberOfBytes() {
        assertEquals(3, UTF_8.length());
        assertEquals(2, UTF_16BE.length());
        assertEquals(4, UTF_32BE.length());
    }

    @Test
    public void getShouldReturnCorrectByteAtGivenIndex() {
        // UTF-32BE is [0x00, 0x00, 0xFE, 0xFF]
        assertEquals(0x00, UTF_32BE.get(0));
        assertEquals(0x00, UTF_32BE.get(1));
        assertEquals(0xFE, UTF_32BE.get(2));
        assertEquals(0xFF, UTF_32BE.get(3));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getWithNegativeIndexShouldThrowException() {
        UTF_8.get(-1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getWithIndexEqualToLengthShouldThrowException() {
        UTF_8.get(UTF_8.length());
    }

    @Test
    public void getBytesShouldReturnCorrectByteArray() {
        // UTF-32LE is [0xFF, 0xFE, 0x00, 0x00]
        byte[] expectedBytes = new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00};
        assertArrayEquals(expectedBytes, UTF_32LE.getBytes());
    }

    // --- matches() Method Tests ---

    @Test
    public void matchesShouldReturnTrueForMatchingBytes() {
        assertTrue(UTF_8.matches(new int[]{0xEF, 0xBB, 0xBF}));
        // Should match if the array starts with the BOM bytes
        assertTrue(UTF_8.matches(new int[]{0xEF, 0xBB, 0xBF, 0xCA, 0xFE}));
    }

    @Test
    public void matchesShouldReturnFalseForNonMatchingBytes() {
        assertFalse(UTF_16BE.matches(new int[]{0xFE, 0xED})); // Mismatch in second byte
        assertFalse(UTF_16BE.matches(new int[]{0xED, 0xFF})); // Mismatch in first byte
    }



    @Test
    public void matchesShouldReturnFalseForShorterArray() {
        assertFalse(UTF_8.matches(new int[]{0xEF, 0xBB}));
    }

    @Test
    public void matchesShouldReturnFalseForNullArray() {
        assertFalse(UTF_8.matches(null));
    }

    // --- equals() and hashCode() Method Tests ---

    @Test
    public void equalsShouldBeReflexive() {
        assertEquals(UTF_8, UTF_8);
    }

    @Test
    public void equalsShouldBeSymmetric() {
        ByteOrderMark bom1 = new ByteOrderMark("BOM", 1, 2);
        ByteOrderMark bom2 = new ByteOrderMark("BOM", 1, 2);
        assertEquals(bom1, bom2);
        assertEquals(bom2, bom1);
    }

    @Test
    public void equalsShouldBeTransitive() {
        ByteOrderMark bom1 = new ByteOrderMark("BOM", 1, 2);
        ByteOrderMark bom2 = new ByteOrderMark("BOM", 1, 2);
        ByteOrderMark bom3 = new ByteOrderMark("BOM", 1, 2);
        assertEquals(bom1, bom2);
        assertEquals(bom2, bom3);
        assertEquals(bom1, bom3);
    }

    @Test
    public void equalsShouldReturnFalseForDifferentBoms() {
        assertNotEquals(UTF_8, UTF_16BE);
        assertNotEquals(UTF_16BE, UTF_16LE);
        assertNotEquals(UTF_32BE, UTF_32LE);
    }

    @Test
    public void equalsShouldReturnFalseForDifferentByteArrays() {
        ByteOrderMark bom1 = new ByteOrderMark("BOM", 1, 2);
        ByteOrderMark bom2 = new ByteOrderMark("BOM", 1, 3);
        assertNotEquals(bom1, bom2);
    }
    
    @Test
    public void equalsShouldReturnFalseForDifferentCharsetNames() {
        ByteOrderMark bom1 = new ByteOrderMark("BOM1", 1, 2);
        ByteOrderMark bom2 = new ByteOrderMark("BOM2", 1, 2);
        assertNotEquals(bom1, bom2);
    }

    @Test
    public void equalsShouldReturnFalseForDifferentObjectType() {
        assertNotEquals(UTF_8, "not a BOM");
    }

    @Test
    public void equalsShouldReturnFalseForNull() {
        assertNotEquals(null, UTF_8);
    }

    @Test
    public void hashCodeShouldBeConsistentForEqualObjects() {
        ByteOrderMark bom1 = new ByteOrderMark("BOM", 1, 2);
        ByteOrderMark bom2 = new ByteOrderMark("BOM", 1, 2);
        assertEquals(bom1, bom2);
        assertEquals("Hash code should be the same for equal objects", bom1.hashCode(), bom2.hashCode());
    }

    // --- toString() Method Test ---

    @Test
    public void toStringShouldReturnCorrectStringRepresentation() {
        String expected = "ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]";
        assertEquals(expected, UTF_8.toString());

        String customExpected = "ByteOrderMark[CUSTOM: 0x1,0x2,0x3]";
        assertEquals(customExpected, customBom.toString());
    }
}