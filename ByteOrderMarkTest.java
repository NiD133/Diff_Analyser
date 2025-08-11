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

import static org.apache.commons.io.ByteOrderMark.UTF_16BE;
import static org.apache.commons.io.ByteOrderMark.UTF_16LE;
import static org.apache.commons.io.ByteOrderMark.UTF_32BE;
import static org.apache.commons.io.ByteOrderMark.UTF_32LE;
import static org.apache.commons.io.ByteOrderMark.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link ByteOrderMark}.
 * 
 * The suite favors small, focused tests with descriptive names
 * and minimal duplication. Parameterized tests and helper methods
 * are used to clearly express each behavior under test.
 */
class ByteOrderMarkTest {

    // Simple, custom BOMs for targeted assertions.
    private static final ByteOrderMark TEST_BOM_1 = new ByteOrderMark("test1", 1);
    private static final ByteOrderMark TEST_BOM_2 = new ByteOrderMark("test2", 1, 2);
    private static final ByteOrderMark TEST_BOM_3 = new ByteOrderMark("test3", 1, 2, 3);

    private static Stream<ByteOrderMark> allCommonsConstants() {
        return Stream.of(UTF_8, UTF_16BE, UTF_16LE, UTF_32BE, UTF_32LE);
    }

    private static int sum(int... values) {
        int s = 0;
        for (int v : values) {
            s += v;
        }
        return s;
    }

    private static String expectedToString(ByteOrderMark bom) {
        StringBuilder sb = new StringBuilder("ByteOrderMark[")
            .append(bom.getCharsetName())
            .append(": ");
        int[] raw = bom.getRawBytes();
        for (int i = 0; i < raw.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append("0x").append(Integer.toHexString(raw[i]).toLowerCase());
        }
        return sb.append(']').toString();
    }

    @Nested
    @DisplayName("Construction and inputs")
    class ConstructionTests {

        @Test
        @DisplayName("Constructor rejects null or empty charsetName and empty or null bytes")
        void rejectsInvalidArguments() {
            assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3));
            assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3));
            assertThrows(NullPointerException.class, () -> new ByteOrderMark("a", (int[]) null));
            assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("b"));
        }

        @ParameterizedTest(name = "Charset name for constant {0} must be loadable")
        @MethodSource("org.apache.commons.io.ByteOrderMarkTest#allCommonsConstants")
        void constantCharsetNamesAreLoadable(ByteOrderMark bom) {
            assertNotNull(Charset.forName(bom.getCharsetName()));
        }
    }

    @Nested
    @DisplayName("Equality and hashCode")
    class EqualityTests {

        @Test
        @DisplayName("Reflexive equality for constants and custom BOMs")
        @SuppressWarnings("EqualsWithItself")
        void equalsIsReflexive() {
            assertEquals(UTF_16BE, UTF_16BE);
            assertEquals(UTF_16LE, UTF_16LE);
            assertEquals(UTF_32BE, UTF_32BE);
            assertEquals(UTF_32LE, UTF_32LE);
            assertEquals(UTF_8, UTF_8);

            assertEquals(TEST_BOM_1, TEST_BOM_1);
            assertEquals(TEST_BOM_2, TEST_BOM_2);
            assertEquals(TEST_BOM_3, TEST_BOM_3);
        }

        @Test
        @DisplayName("BOMs with different bytes are not equal")
        void notEqualForDifferentBytesOrNames() {
            assertNotEquals(UTF_8, UTF_16BE);
            assertNotEquals(UTF_8, UTF_16LE);
            assertNotEquals(UTF_8, UTF_32BE);
            assertNotEquals(UTF_8, UTF_32LE);

            assertNotEquals(TEST_BOM_1, new Object(), "Different type");
            assertNotEquals(TEST_BOM_1, new ByteOrderMark("1a", 2), "Different bytes");
            assertNotEquals(TEST_BOM_1, new ByteOrderMark("1b", 1, 2), "Different length");
            assertNotEquals(TEST_BOM_2, new ByteOrderMark("2", 1, 1), "Different bytes");
            assertNotEquals(TEST_BOM_3, new ByteOrderMark("3", 1, 2, 4), "Different bytes");
        }

        @Test
        @DisplayName("hashCode is based on class and sum of bytes")
        void hashCodeMatchesContract() {
            int base = ByteOrderMark.class.hashCode();
            assertEquals(base + sum(1), TEST_BOM_1.hashCode());
            assertEquals(base + sum(1, 2), TEST_BOM_2.hashCode());
            assertEquals(base + sum(1, 2, 3), TEST_BOM_3.hashCode());
        }
    }

    @Nested
    @DisplayName("Accessors")
    class AccessorTests {

        @Test
        @DisplayName("getBytes returns a defensive copy")
        void getBytesReturnsDefensiveCopy() {
            assertArrayEquals(new byte[] { 1 }, TEST_BOM_1.getBytes());
            byte[] copy = TEST_BOM_1.getBytes();
            copy[0] = 99; // attempt to mutate
            assertArrayEquals(new byte[] { 1 }, TEST_BOM_1.getBytes(), "Original object must not change");

            assertArrayEquals(new byte[] { 1, 2 }, TEST_BOM_2.getBytes());
            assertArrayEquals(new byte[] { 1, 2, 3 }, TEST_BOM_3.getBytes());
        }

        @Test
        @DisplayName("getCharsetName returns provided name")
        void getCharsetNameReturnsProvidedName() {
            assertEquals("test1", TEST_BOM_1.getCharsetName());
            assertEquals("test2", TEST_BOM_2.getCharsetName());
            assertEquals("test3", TEST_BOM_3.getCharsetName());
        }

        @Test
        @DisplayName("get(index) returns the byte at the given position")
        void getByIndex() {
            assertEquals(1, TEST_BOM_1.get(0));

            assertEquals(1, TEST_BOM_2.get(0));
            assertEquals(2, TEST_BOM_2.get(1));

            assertEquals(1, TEST_BOM_3.get(0));
            assertEquals(2, TEST_BOM_3.get(1));
            assertEquals(3, TEST_BOM_3.get(2));
        }

        @Test
        @DisplayName("length returns number of BOM bytes")
        void lengthMatchesByteCount() {
            assertEquals(1, TEST_BOM_1.length());
            assertEquals(2, TEST_BOM_2.length());
            assertEquals(3, TEST_BOM_3.length());
        }
    }

    @Nested
    @DisplayName("Prefix matching")
    class MatchingTests {

        @Test
        @DisplayName("Constants match their own raw bytes")
        void constantsMatchOwnRawBytes() {
            assertTrue(UTF_16BE.matches(UTF_16BE.getRawBytes()));
            assertTrue(UTF_16LE.matches(UTF_16LE.getRawBytes()));
            assertTrue(UTF_32BE.matches(UTF_32BE.getRawBytes()));
            assertTrue(UTF_32LE.matches(UTF_32LE.getRawBytes()));
            assertTrue(UTF_8.matches(UTF_8.getRawBytes()));
        }

        @Test
        @DisplayName("Custom BOMs match exact and prefix arrays")
        void customBomsMatch() {
            assertTrue(TEST_BOM_1.matches(TEST_BOM_1.getRawBytes()));
            assertTrue(TEST_BOM_2.matches(TEST_BOM_2.getRawBytes()));
            assertTrue(TEST_BOM_3.matches(TEST_BOM_3.getRawBytes()));

            // "Prefix" behavior: a 1-byte BOM should match the start of a longer sequence starting with the same byte.
            assertTrue(TEST_BOM_1.matches(new ByteOrderMark("prefix", 1, 2).getRawBytes()));
        }

        @Test
        @DisplayName("Mismatches are detected")
        void mismatches() {
            assertFalse(TEST_BOM_1.matches(new ByteOrderMark("x", 2).getRawBytes()));
            assertFalse(TEST_BOM_2.matches(new ByteOrderMark("y", 1, 1).getRawBytes()));
            assertFalse(TEST_BOM_3.matches(new ByteOrderMark("z", 1, 2, 4).getRawBytes()));
        }
    }

    @Nested
    @DisplayName("Formatting")
    class FormattingTests {

        @Test
        @DisplayName("toString prints name and hex bytes")
        void toStringFormatsNameAndBytes() {
            assertEquals(expectedToString(TEST_BOM_1), TEST_BOM_1.toString());
            assertEquals(expectedToString(TEST_BOM_2), TEST_BOM_2.toString());
            assertEquals(expectedToString(TEST_BOM_3), TEST_BOM_3.toString());
        }
    }
}