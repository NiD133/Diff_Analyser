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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for the {@link IOCase} enum, focusing on its comparison and utility methods.
 */
@DisplayName("Tests for IOCase")
class IOCaseTest {

    /**
     * A flag indicating if the current operating system is Windows,
     * used for testing system-dependent case sensitivity.
     */
    private static final boolean IS_WINDOWS = File.separatorChar == '\\';

    /**
     * Serializes and then deserializes an IOCase instance to verify that
     * the singleton pattern is maintained.
     *
     * @param value The IOCase instance to serialize.
     * @return The deserialized IOCase instance.
     * @throws Exception if an I/O or class loading error occurs.
     */
    private IOCase serializeAndDeserialize(final IOCase value) throws Exception {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(buffer)) {
            out.writeObject(value);
        }

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer.toByteArray());
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            return (IOCase) in.readObject();
        }
    }

    @Test
    @DisplayName("forName() should return the correct IOCase instance or throw an exception")
    void testForName() {
        assertEquals(IOCase.SENSITIVE, IOCase.forName("Sensitive"));
        assertEquals(IOCase.INSENSITIVE, IOCase.forName("Insensitive"));
        assertEquals(IOCase.SYSTEM, IOCase.forName("System"));
        assertThrows(IllegalArgumentException.class, () -> IOCase.forName("InvalidName"));
        assertThrows(IllegalArgumentException.class, () -> IOCase.forName(null));
    }

    @Test
    @DisplayName("getName() should return the correct name of the constant")
    void testGetName() {
        assertEquals("Sensitive", IOCase.SENSITIVE.getName());
        assertEquals("Insensitive", IOCase.INSENSITIVE.getName());
        assertEquals("System", IOCase.SYSTEM.getName());
    }

    @Test
    @DisplayName("toString() should return the name of the constant")
    void testToString() {
        assertEquals("Sensitive", IOCase.SENSITIVE.toString());
        assertEquals("Insensitive", IOCase.INSENSITIVE.toString());
        assertEquals("System", IOCase.SYSTEM.toString());
    }

    @Test
    @DisplayName("isCaseSensitive() should correctly report sensitivity")
    void testIsCaseSensitive() {
        assertTrue(IOCase.SENSITIVE.isCaseSensitive());
        assertFalse(IOCase.INSENSITIVE.isCaseSensitive());
        assertEquals(!IS_WINDOWS, IOCase.SYSTEM.isCaseSensitive());
    }

    @Test
    @DisplayName("Serialization should preserve singleton instances")
    void testSerialization() throws Exception {
        assertSame(IOCase.SENSITIVE, serializeAndDeserialize(IOCase.SENSITIVE));
        assertSame(IOCase.INSENSITIVE, serializeAndDeserialize(IOCase.INSENSITIVE));
        assertSame(IOCase.SYSTEM, serializeAndDeserialize(IOCase.SYSTEM));
    }

    @Nested
    @DisplayName("checkCompareTo(String, String)")
    class CheckCompareToTest {

        @Test
        @DisplayName("SENSITIVE should perform a case-sensitive comparison")
        void testSensitive() {
            assertEquals(0, IOCase.SENSITIVE.checkCompareTo("ABC", "ABC"));
            assertTrue(IOCase.SENSITIVE.checkCompareTo("ABC", "abc") < 0);
            assertTrue(IOCase.SENSITIVE.checkCompareTo("abc", "ABC") > 0);
        }

        @Test
        @DisplayName("INSENSITIVE should perform a case-insensitive comparison")
        void testInsensitive() {
            assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("ABC", "ABC"));
            assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("ABC", "abc"));
            assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("abc", "ABC"));
        }

        @Test
        @DisplayName("SYSTEM should perform a system-dependent comparison")
        void testSystem() {
            assertEquals(0, IOCase.SYSTEM.checkCompareTo("ABC", "ABC"));
            if (IS_WINDOWS) {
                assertEquals(0, IOCase.SYSTEM.checkCompareTo("ABC", "abc"), "On Windows, comparison should be case-insensitive");
            } else {
                assertTrue(IOCase.SYSTEM.checkCompareTo("ABC", "abc") < 0, "On non-Windows, comparison should be case-sensitive");
            }
        }

        @Test
        @DisplayName("should throw NullPointerException for null input")
        void testNullInput() {
            assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo("ABC", null));
            assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo(null, "ABC"));
            assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo(null, null));
        }
    }

    @Nested
    @DisplayName("checkEquals(String, String)")
    class CheckEqualsTest {

        @Test
        @DisplayName("SENSITIVE should perform a case-sensitive equality check")
        void testSensitive() {
            assertTrue(IOCase.SENSITIVE.checkEquals("ABC", "ABC"));
            assertFalse(IOCase.SENSITIVE.checkEquals("ABC", "Abc"));
        }

        @Test
        @DisplayName("INSENSITIVE should perform a case-insensitive equality check")
        void testInsensitive() {
            assertTrue(IOCase.INSENSITIVE.checkEquals("ABC", "ABC"));
            assertTrue(IOCase.INSENSITIVE.checkEquals("ABC", "Abc"));
        }

        @Test
        @DisplayName("SYSTEM should perform a system-dependent equality check")
        void testSystem() {
            assertTrue(IOCase.SYSTEM.checkEquals("ABC", "ABC"));
            if (IS_WINDOWS) {
                assertTrue(IOCase.SYSTEM.checkEquals("ABC", "Abc"), "On Windows, equality should be case-insensitive");
            } else {
                assertFalse(IOCase.SYSTEM.checkEquals("ABC", "Abc"), "On non-Windows, equality should be case-sensitive");
            }
        }

        @Test
        @DisplayName("should handle null inputs correctly")
        void testNulls() {
            assertFalse(IOCase.SENSITIVE.checkEquals("ABC", null));
            assertFalse(IOCase.SENSITIVE.checkEquals(null, "ABC"));
            assertTrue(IOCase.SENSITIVE.checkEquals(null, null));
        }

        @Test
        @DisplayName("should handle empty strings correctly")
        void testEmpty() {
            assertTrue(IOCase.SENSITIVE.checkEquals("", ""));
            assertFalse(IOCase.SENSITIVE.checkEquals("ABC", ""));
            assertFalse(IOCase.SENSITIVE.checkEquals("", "ABC"));
        }
    }

    @Nested
    @DisplayName("checkStartsWith(String, String)")
    class CheckStartsWithTest {

        @Test
        @DisplayName("SENSITIVE should perform a case-sensitive check")
        void testSensitive() {
            assertTrue(IOCase.SENSITIVE.checkStartsWith("ABC", "AB"));
            assertFalse(IOCase.SENSITIVE.checkStartsWith("ABC", "Ab"));
        }

        @Test
        @DisplayName("INSENSITIVE should perform a case-insensitive check")
        void testInsensitive() {
            assertTrue(IOCase.INSENSITIVE.checkStartsWith("ABC", "AB"));
            assertTrue(IOCase.INSENSITIVE.checkStartsWith("ABC", "Ab"));
        }

        @Test
        @DisplayName("SYSTEM should perform a system-dependent check")
        void testSystem() {
            assertTrue(IOCase.SYSTEM.checkStartsWith("ABC", "AB"));
            if (IS_WINDOWS) {
                assertTrue(IOCase.SYSTEM.checkStartsWith("ABC", "Ab"), "On Windows, check should be case-insensitive");
            } else {
                assertFalse(IOCase.SYSTEM.checkStartsWith("ABC", "Ab"), "On non-Windows, check should be case-sensitive");
            }
        }

        @Test
        @DisplayName("should handle null inputs by returning false")
        void testNulls() {
            assertFalse(IOCase.SENSITIVE.checkStartsWith("ABC", null));
            assertFalse(IOCase.SENSITIVE.checkStartsWith(null, "ABC"));
            assertFalse(IOCase.SENSITIVE.checkStartsWith(null, null));
        }

        @Test
        @DisplayName("should return true for any string starting with an empty string")
        void testEmpty() {
            assertTrue(IOCase.SENSITIVE.checkStartsWith("ABC", ""));
        }
    }

    @Nested
    @DisplayName("checkEndsWith(String, String)")
    class CheckEndsWithTest {

        @Test
        @DisplayName("SENSITIVE should perform a case-sensitive check")
        void testSensitive() {
            assertTrue(IOCase.SENSITIVE.checkEndsWith("ABC", "BC"));
            assertFalse(IOCase.SENSITIVE.checkEndsWith("ABC", "Bc"));
        }

        @Test
        @DisplayName("INSENSITIVE should perform a case-insensitive check")
        void testInsensitive() {
            assertTrue(IOCase.INSENSITIVE.checkEndsWith("ABC", "BC"));
            assertTrue(IOCase.INSENSITIVE.checkEndsWith("ABC", "Bc"));
        }

        @Test
        @DisplayName("SYSTEM should perform a system-dependent check")
        void testSystem() {
            assertTrue(IOCase.SYSTEM.checkEndsWith("ABC", "BC"));
            if (IS_WINDOWS) {
                assertTrue(IOCase.SYSTEM.checkEndsWith("ABC", "Bc"), "On Windows, check should be case-insensitive");
            } else {
                assertFalse(IOCase.SYSTEM.checkEndsWith("ABC", "Bc"), "On non-Windows, check should be case-sensitive");
            }
        }

        @Test
        @DisplayName("should handle null inputs by returning false")
        void testNulls() {
            assertFalse(IOCase.SENSITIVE.checkEndsWith("ABC", null));
            assertFalse(IOCase.SENSITIVE.checkEndsWith(null, "ABC"));
            assertFalse(IOCase.SENSITIVE.checkEndsWith(null, null));
        }

        @Test
        @DisplayName("should return true for any string ending with an empty string")
        void testEmpty() {
            assertTrue(IOCase.SENSITIVE.checkEndsWith("ABC", ""));
        }
    }

    @Nested
    @DisplayName("checkIndexOf(String, int, String)")
    class CheckIndexOfTest {

        @Test
        @DisplayName("SENSITIVE should find the substring with case-sensitivity")
        void testSensitive() {
            assertEquals(1, IOCase.SENSITIVE.checkIndexOf("ABC", 0, "BC"));
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABC", 0, "Bc"));
        }

        @Test
        @DisplayName("INSENSITIVE should find the substring without case-sensitivity")
        void testInsensitive() {
            assertEquals(1, IOCase.INSENSITIVE.checkIndexOf("ABC", 0, "BC"));
            assertEquals(1, IOCase.INSENSITIVE.checkIndexOf("ABC", 0, "Bc"));
        }

        @Test
        @DisplayName("SYSTEM should find the substring based on OS case-sensitivity")
        void testSystem() {
            assertEquals(1, IOCase.SYSTEM.checkIndexOf("ABC", 0, "BC"));
            if (IS_WINDOWS) {
                assertEquals(1, IOCase.SYSTEM.checkIndexOf("ABC", 0, "Bc"), "On Windows, index should be found case-insensitively");
            } else {
                assertEquals(-1, IOCase.SYSTEM.checkIndexOf("ABC", 0, "Bc"), "On non-Windows, index should not be found");
            }
        }

        @Test
        @DisplayName("should return -1 for null inputs")
        void testNulls() {
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("ABC", 0, null));
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, "BC"));
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, null));
        }

        @DisplayName("should return correct index for various substrings and start positions")
        @ParameterizedTest(name = "should return {0} for indexOf(\"{3}\") in \"{1}\" starting at {2}")
        @CsvSource({
                // expected, str, startIdx, search
                " 0, 'ABCDEFGHIJ', 0, 'A'",
                "-1, 'ABCDEFGHIJ', 1, 'A'",
                " 0, 'ABCDEFGHIJ', 0, 'AB'",
                "-1, 'ABCDEFGHIJ', 1, 'AB'",
                " 0, 'ABCDEFGHIJ', 0, 'ABC'",
                "-1, 'ABCDEFGHIJ', 1, 'ABC'",
                " 3, 'ABCDEFGHIJ', 0, 'D'",
                " 3, 'ABCDEFGHIJ', 3, 'D'",
                "-1, 'ABCDEFGHIJ', 4, 'D'",
                " 3, 'ABCDEFGHIJ', 0, 'DE'",
                " 3, 'ABCDEFGHIJ', 3, 'DE'",
                "-1, 'ABCDEFGHIJ', 4, 'DE'",
                " 9, 'ABCDEFGHIJ', 0, 'J'",
                " 9, 'ABCDEFGHIJ', 9, 'J'",
                " 8, 'ABCDEFGHIJ', 8, 'IJ'",
                "-1, 'ABCDEFGHIJ', 9, 'IJ'",
                " 7, 'ABCDEFGHIJ', 7, 'HIJ'",
                "-1, 'ABCDEFGHIJ', 8, 'HIJ'",
                "-1, 'ABCDEFGHIJ', 0, 'DED'",
                "-1, 'DEF',      0, 'ABCDEFGHIJ'"
        })
        void testIndexOfFunctionality(int expected, String str, int startIdx, String search) {
            assertEquals(expected, IOCase.SENSITIVE.checkIndexOf(str, startIdx, search));
        }
    }

    @Nested
    @DisplayName("checkRegionMatches(String, int, String)")
    class CheckRegionMatchesTest {

        @Test
        @DisplayName("SENSITIVE should perform a case-sensitive region match")
        void testSensitive() {
            assertTrue(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, "AB"));
            assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, "Ab"));
        }

        @Test
        @DisplayName("INSENSITIVE should perform a case-insensitive region match")
        void testInsensitive() {
            assertTrue(IOCase.INSENSITIVE.checkRegionMatches("ABC", 0, "AB"));
            assertTrue(IOCase.INSENSITIVE.checkRegionMatches("ABC", 0, "Ab"));
        }

        @Test
        @DisplayName("SYSTEM should perform a system-dependent region match")
        void testSystem() {
            assertTrue(IOCase.SYSTEM.checkRegionMatches("ABC", 0, "AB"));
            if (IS_WINDOWS) {
                assertTrue(IOCase.SYSTEM.checkRegionMatches("ABC", 0, "Ab"), "On Windows, region should match case-insensitively");
            } else {
                assertFalse(IOCase.SYSTEM.checkRegionMatches("ABC", 0, "Ab"), "On non-Windows, region should not match");
            }
        }

        @Test
        @DisplayName("should handle various regions and edge cases")
        void testRegions() {
            assertTrue(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, ""));
            assertTrue(IOCase.SENSITIVE.checkRegionMatches("ABC", 1, "BC"));
            assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 1, "AB"));
        }

        @Test
        @DisplayName("should handle null inputs by returning false")
        void testNulls() {
            assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, null));
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 0, "ABC"));
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 0, null));
        }
    }
}