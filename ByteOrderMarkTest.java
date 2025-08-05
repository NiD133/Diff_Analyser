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
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link ByteOrderMark}.
 */
class ByteOrderMarkTest {

    private static final ByteOrderMark BOM_1_BYTE = new ByteOrderMark("test1", 1);
    private static final ByteOrderMark BOM_2_BYTES = new ByteOrderMark("test2", 1, 2);
    private static final ByteOrderMark BOM_3_BYTES = new ByteOrderMark("test3", 1, 2, 3);

    @Nested
    @DisplayName("Constructor")
    class ConstructorTest {

        @Test
        void constructor_withNullCharsetName_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3),
                "Constructor should not accept a null charset name.");
        }

        @Test
        void constructor_withEmptyCharsetName_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3),
                "Constructor should not accept an empty charset name.");
        }

        @Test
        void constructor_withNullBytes_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> new ByteOrderMark("a", (int[]) null),
                "Constructor should not accept a null byte array.");
        }

        @Test
        void constructor_withEmptyBytes_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("b"),
                "Constructor should not accept an empty byte array.");
        }
    }

    @Nested
    @DisplayName("Predefined BOM Constants")
    class PredefinedConstantsTest {

        static Stream<ByteOrderMark> predefinedBoms() {
            return Stream.of(
                ByteOrderMark.UTF_8,
                ByteOrderMark.UTF_16BE,
                ByteOrderMark.UTF_16LE,
                ByteOrderMark.UTF_32BE,
                ByteOrderMark.UTF_32LE
            );
        }

        @ParameterizedTest
        @MethodSource("predefinedBoms")
        void predefinedBoms_shouldHaveValidCharset(final ByteOrderMark bom) {
            assertNotNull(Charset.forName(bom.getCharsetName()),
                "Charset name '" + bom.getCharsetName() + "' should be valid.");
        }
    }

    @Nested
    @DisplayName("Getters")
    class GettersTest {

        @Test
        void getCharsetName_shouldReturnCorrectName() {
            assertEquals("test1", BOM_1_BYTE.getCharsetName());
            assertEquals("test2", BOM_2_BYTES.getCharsetName());
            assertEquals("test3", BOM_3_BYTES.getCharsetName());
        }

        @Test
        void length_shouldReturnCorrectByteCount() {
            assertEquals(1, BOM_1_BYTE.length());
            assertEquals(2, BOM_2_BYTES.length());
            assertEquals(3, BOM_3_BYTES.length());
        }

        @Test
        void get_shouldReturnCorrectByteAtIndex() {
            assertEquals(1, BOM_2_BYTES.get(0));
            assertEquals(2, BOM_2_BYTES.get(1));
        }

        @Test
        void getBytes_shouldReturnCorrectBytes() {
            assertArrayEquals(new byte[]{(byte) 1}, BOM_1_BYTE.getBytes());
            assertArrayEquals(new byte[]{(byte) 1, (byte) 2}, BOM_2_BYTES.getBytes());
            assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3}, BOM_3_BYTES.getBytes());
        }

        @Test
        void getBytes_shouldReturnDefensiveCopy() {
            final byte[] bytes = BOM_2_BYTES.getBytes();
            // Modify the returned array
            bytes[0] = 99;
            // Assert that the original BOM's internal state is unchanged
            assertArrayEquals(new byte[]{(byte) 1, (byte) 2}, BOM_2_BYTES.getBytes(),
                "Modifying the returned byte array should not affect the BOM's internal state.");
        }
    }

    @Nested
    @DisplayName("equals() and hashCode() Contract")
    class EqualsAndHashCodeContractTest {

        @Test
        void equals_shouldBeReflexive() {
            assertEquals(BOM_2_BYTES, BOM_2_BYTES);
        }

        @Test
        void equals_shouldBeSymmetricAndConsistent() {
            final ByteOrderMark bomA = new ByteOrderMark("test2", 1, 2);
            final ByteOrderMark bomB = new ByteOrderMark("test2", 1, 2);
            assertEquals(bomA, bomB, "Two BOMs with the same charset and bytes should be equal.");
            assertEquals(bomB, bomA, "Equals should be symmetric.");
        }

        @Test
        void hashCode_shouldBeConsistentWithEquals() {
            final ByteOrderMark bomA = new ByteOrderMark("test2", 1, 2);
            final ByteOrderMark bomB = new ByteOrderMark("test2", 1, 2);
            assertEquals(bomA, bomB, "Test objects should be equal for a valid hash code comparison.");
            assertEquals(bomA.hashCode(), bomB.hashCode(), "Hash codes must be equal for equal objects.");
        }

        @Test
        void equals_shouldReturnFalseForDifferentBytes() {
            final ByteOrderMark other = new ByteOrderMark("test2", 1, 99);
            assertNotEquals(BOM_2_BYTES, other);
        }

        @Test
        void equals_shouldReturnFalseForDifferentCharsetName() {
            final ByteOrderMark other = new ByteOrderMark("otherName", 1, 2);
            assertNotEquals(BOM_2_BYTES, other);
        }

        @Test
        void equals_shouldReturnFalseForDifferentLength() {
            assertNotEquals(BOM_2_BYTES, BOM_3_BYTES);
        }

        @Test
        void equals_shouldReturnFalseForNull() {
            assertNotEquals(null, BOM_1_BYTE);
        }

        @Test
        void equals_shouldReturnFalseForDifferentClass() {
            assertNotEquals(new Object(), BOM_1_BYTE);
        }
    }

    @Nested
    @DisplayName("Behavior")
    class BehaviorTest {

        @Test
        void matches_shouldReturnTrueForExactMatch() {
            assertTrue(BOM_2_BYTES.matches(new int[]{1, 2}),
                "matches() should return true for an identical byte sequence.");
        }

        @Test
        void matches_shouldReturnTrueForMatchingPrefix() {
            assertTrue(BOM_2_BYTES.matches(new int[]{1, 2, 3, 4}),
                "matches() should return true if the array starts with the BOM bytes.");
        }

        @Test
        void matches_shouldReturnFalseForNonMatchingBytes() {
            assertFalse(BOM_2_BYTES.matches(new int[]{1, 99}),
                "matches() should return false for a non-matching byte sequence.");
        }

        @Test
        void matches_shouldReturnFalseForShorterArray() {
            assertFalse(BOM_2_BYTES.matches(new int[]{1}),
                "matches() should return false if the array is shorter than the BOM.");
        }

        @Test
        void toString_shouldReturnCorrectRepresentation() {
            assertEquals("ByteOrderMark[test1: 0x1]", BOM_1_BYTE.toString());
            assertEquals("ByteOrderMark[test2: 0x1,0x2]", BOM_2_BYTES.toString());
            assertEquals("ByteOrderMark[test3: 0x1,0x2,0x3]", BOM_3_BYTES.toString());
        }
    }
}