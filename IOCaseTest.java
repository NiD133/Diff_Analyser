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
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests {@link IOCase} enumeration for case-sensitive file operations.
 */
@DisplayName("IOCase Tests")
class IOCaseTest {

    private static final boolean IS_WINDOWS = File.separatorChar == '\\';
    
    // Test data constants
    private static final String UPPERCASE_ABC = "ABC";
    private static final String LOWERCASE_ABC = "abc";
    private static final String MIXED_CASE_ABC = "Abc";
    private static final String EMPTY_STRING = "";
    private static final String LONG_STRING = "ABCDEFGHIJ";

    @Nested
    @DisplayName("Basic Properties Tests")
    class BasicPropertiesTests {

        @Test
        @DisplayName("Should return correct case sensitivity flags")
        void shouldReturnCorrectCaseSensitivityFlags() {
            assertTrue(IOCase.SENSITIVE.isCaseSensitive(), "SENSITIVE should be case sensitive");
            assertFalse(IOCase.INSENSITIVE.isCaseSensitive(), "INSENSITIVE should not be case sensitive");
            assertEquals(!IS_WINDOWS, IOCase.SYSTEM.isCaseSensitive(), 
                "SYSTEM case sensitivity should match OS (Unix=true, Windows=false)");
        }

        @Test
        @DisplayName("Should return correct names")
        void shouldReturnCorrectNames() {
            assertEquals("Sensitive", IOCase.SENSITIVE.getName());
            assertEquals("Insensitive", IOCase.INSENSITIVE.getName());
            assertEquals("System", IOCase.SYSTEM.getName());
        }

        @Test
        @DisplayName("Should return correct string representation")
        void shouldReturnCorrectStringRepresentation() {
            assertEquals("Sensitive", IOCase.SENSITIVE.toString());
            assertEquals("Insensitive", IOCase.INSENSITIVE.toString());
            assertEquals("System", IOCase.SYSTEM.toString());
        }

        @Test
        @DisplayName("Should find IOCase by name")
        void shouldFindIOCaseByName() {
            assertEquals(IOCase.SENSITIVE, IOCase.forName("Sensitive"));
            assertEquals(IOCase.INSENSITIVE, IOCase.forName("Insensitive"));
            assertEquals(IOCase.SYSTEM, IOCase.forName("System"));
        }

        @Test
        @DisplayName("Should throw exception for invalid names")
        void shouldThrowExceptionForInvalidNames() {
            assertThrows(IllegalArgumentException.class, () -> IOCase.forName("InvalidName"));
            assertThrows(IllegalArgumentException.class, () -> IOCase.forName(null));
        }

        @Test
        @DisplayName("Should handle static case sensitivity check")
        void shouldHandleStaticCaseSensitivityCheck() {
            assertTrue(IOCase.isCaseSensitive(IOCase.SENSITIVE));
            assertFalse(IOCase.isCaseSensitive(IOCase.INSENSITIVE));
            assertEquals(!IS_WINDOWS, IOCase.isCaseSensitive(IOCase.SYSTEM));
        }
    }

    @Nested
    @DisplayName("String Equality Tests")
    class StringEqualityTests {

        @Test
        @DisplayName("Should handle case sensitivity in equals check")
        void shouldHandleCaseSensitivityInEqualsCheck() {
            // SENSITIVE: exact match required
            assertTrue(IOCase.SENSITIVE.checkEquals(UPPERCASE_ABC, UPPERCASE_ABC));
            assertFalse(IOCase.SENSITIVE.checkEquals(UPPERCASE_ABC, MIXED_CASE_ABC));

            // INSENSITIVE: case doesn't matter
            assertTrue(IOCase.INSENSITIVE.checkEquals(UPPERCASE_ABC, UPPERCASE_ABC));
            assertTrue(IOCase.INSENSITIVE.checkEquals(UPPERCASE_ABC, MIXED_CASE_ABC));

            // SYSTEM: depends on OS
            assertTrue(IOCase.SYSTEM.checkEquals(UPPERCASE_ABC, UPPERCASE_ABC));
            assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkEquals(UPPERCASE_ABC, MIXED_CASE_ABC));
        }

        @Test
        @DisplayName("Should handle edge cases in equals check")
        void shouldHandleEdgeCasesInEqualsCheck() {
            // Different lengths
            assertFalse(IOCase.SENSITIVE.checkEquals(UPPERCASE_ABC, EMPTY_STRING));
            assertFalse(IOCase.SENSITIVE.checkEquals(UPPERCASE_ABC, "A"));
            assertFalse(IOCase.SENSITIVE.checkEquals(UPPERCASE_ABC, "ABCD"));

            // Empty strings
            assertTrue(IOCase.SENSITIVE.checkEquals(EMPTY_STRING, EMPTY_STRING));
            assertFalse(IOCase.SENSITIVE.checkEquals(EMPTY_STRING, UPPERCASE_ABC));

            // Null handling
            assertFalse(IOCase.SENSITIVE.checkEquals(UPPERCASE_ABC, null));
            assertFalse(IOCase.SENSITIVE.checkEquals(null, UPPERCASE_ABC));
            assertTrue(IOCase.SENSITIVE.checkEquals(null, null));
        }
    }

    @Nested
    @DisplayName("String Comparison Tests")
    class StringComparisonTests {

        @Test
        @DisplayName("Should handle case sensitivity in string comparison")
        void shouldHandleCaseSensitivityInStringComparison() {
            // SENSITIVE: case matters for ordering
            assertEquals(0, IOCase.SENSITIVE.checkCompareTo(UPPERCASE_ABC, UPPERCASE_ABC));
            assertTrue(IOCase.SENSITIVE.checkCompareTo(UPPERCASE_ABC, LOWERCASE_ABC) < 0);
            assertTrue(IOCase.SENSITIVE.checkCompareTo(LOWERCASE_ABC, UPPERCASE_ABC) > 0);

            // INSENSITIVE: case ignored
            assertEquals(0, IOCase.INSENSITIVE.checkCompareTo(UPPERCASE_ABC, UPPERCASE_ABC));
            assertEquals(0, IOCase.INSENSITIVE.checkCompareTo(UPPERCASE_ABC, LOWERCASE_ABC));
            assertEquals(0, IOCase.INSENSITIVE.checkCompareTo(LOWERCASE_ABC, UPPERCASE_ABC));

            // SYSTEM: depends on OS
            assertEquals(0, IOCase.SYSTEM.checkCompareTo(UPPERCASE_ABC, UPPERCASE_ABC));
            assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkCompareTo(UPPERCASE_ABC, LOWERCASE_ABC) == 0);
            assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkCompareTo(LOWERCASE_ABC, UPPERCASE_ABC) == 0);
        }

        @Test
        @DisplayName("Should handle basic string ordering")
        void shouldHandleBasicStringOrdering() {
            assertTrue(IOCase.SENSITIVE.checkCompareTo(UPPERCASE_ABC, EMPTY_STRING) > 0);
            assertTrue(IOCase.SENSITIVE.checkCompareTo(EMPTY_STRING, UPPERCASE_ABC) < 0);
            assertTrue(IOCase.SENSITIVE.checkCompareTo(UPPERCASE_ABC, "DEF") < 0);
            assertTrue(IOCase.SENSITIVE.checkCompareTo("DEF", UPPERCASE_ABC) > 0);
            assertEquals(0, IOCase.SENSITIVE.checkCompareTo(UPPERCASE_ABC, UPPERCASE_ABC));
            assertEquals(0, IOCase.SENSITIVE.checkCompareTo(EMPTY_STRING, EMPTY_STRING));
        }

        @Test
        @DisplayName("Should throw NPE for null arguments in comparison")
        void shouldThrowNPEForNullArgumentsInComparison() {
            assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo(UPPERCASE_ABC, null));
            assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo(null, UPPERCASE_ABC));
            assertThrows(NullPointerException.class, () -> IOCase.SENSITIVE.checkCompareTo(null, null));
        }
    }

    @Nested
    @DisplayName("String Prefix Tests")
    class StringPrefixTests {

        @Test
        @DisplayName("Should handle case sensitivity in startsWith check")
        void shouldHandleCaseSensitivityInStartsWithCheck() {
            assertTrue(IOCase.SENSITIVE.checkStartsWith(UPPERCASE_ABC, "AB"));
            assertFalse(IOCase.SENSITIVE.checkStartsWith(UPPERCASE_ABC, "Ab"));

            assertTrue(IOCase.INSENSITIVE.checkStartsWith(UPPERCASE_ABC, "AB"));
            assertTrue(IOCase.INSENSITIVE.checkStartsWith(UPPERCASE_ABC, "Ab"));

            assertTrue(IOCase.SYSTEM.checkStartsWith(UPPERCASE_ABC, "AB"));
            assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkStartsWith(UPPERCASE_ABC, "Ab"));
        }

        @Test
        @DisplayName("Should handle various prefix scenarios")
        void shouldHandleVariousPrefixScenarios() {
            // Valid prefixes
            assertTrue(IOCase.SENSITIVE.checkStartsWith(UPPERCASE_ABC, EMPTY_STRING));
            assertTrue(IOCase.SENSITIVE.checkStartsWith(UPPERCASE_ABC, "A"));
            assertTrue(IOCase.SENSITIVE.checkStartsWith(UPPERCASE_ABC, "AB"));
            assertTrue(IOCase.SENSITIVE.checkStartsWith(UPPERCASE_ABC, UPPERCASE_ABC));

            // Invalid prefixes
            assertFalse(IOCase.SENSITIVE.checkStartsWith(UPPERCASE_ABC, "BC"));
            assertFalse(IOCase.SENSITIVE.checkStartsWith(UPPERCASE_ABC, "C"));
            assertFalse(IOCase.SENSITIVE.checkStartsWith(UPPERCASE_ABC, "ABCD"));
            assertFalse(IOCase.SENSITIVE.checkStartsWith(EMPTY_STRING, UPPERCASE_ABC));

            // Edge cases
            assertTrue(IOCase.SENSITIVE.checkStartsWith(EMPTY_STRING, EMPTY_STRING));
            assertFalse(IOCase.SENSITIVE.checkStartsWith(UPPERCASE_ABC, null));
            assertFalse(IOCase.SENSITIVE.checkStartsWith(null, UPPERCASE_ABC));
            assertFalse(IOCase.SENSITIVE.checkStartsWith(null, null));
        }
    }

    @Nested
    @DisplayName("String Suffix Tests")
    class StringSuffixTests {

        @Test
        @DisplayName("Should handle case sensitivity in endsWith check")
        void shouldHandleCaseSensitivityInEndsWithCheck() {
            assertTrue(IOCase.SENSITIVE.checkEndsWith(UPPERCASE_ABC, "BC"));
            assertFalse(IOCase.SENSITIVE.checkEndsWith(UPPERCASE_ABC, "Bc"));

            assertTrue(IOCase.INSENSITIVE.checkEndsWith(UPPERCASE_ABC, "BC"));
            assertTrue(IOCase.INSENSITIVE.checkEndsWith(UPPERCASE_ABC, "Bc"));

            assertTrue(IOCase.SYSTEM.checkEndsWith(UPPERCASE_ABC, "BC"));
            assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkEndsWith(UPPERCASE_ABC, "Bc"));
        }

        @Test
        @DisplayName("Should handle various suffix scenarios")
        void shouldHandleVariousSuffixScenarios() {
            // Valid suffixes
            assertTrue(IOCase.SENSITIVE.checkEndsWith(UPPERCASE_ABC, EMPTY_STRING));
            assertTrue(IOCase.SENSITIVE.checkEndsWith(UPPERCASE_ABC, "C"));
            assertTrue(IOCase.SENSITIVE.checkEndsWith(UPPERCASE_ABC, "BC"));
            assertTrue(IOCase.SENSITIVE.checkEndsWith(UPPERCASE_ABC, UPPERCASE_ABC));

            // Invalid suffixes
            assertFalse(IOCase.SENSITIVE.checkEndsWith(UPPERCASE_ABC, "A"));
            assertFalse(IOCase.SENSITIVE.checkEndsWith(UPPERCASE_ABC, "AB"));
            assertFalse(IOCase.SENSITIVE.checkEndsWith(UPPERCASE_ABC, "ABCD"));
            assertFalse(IOCase.SENSITIVE.checkEndsWith(EMPTY_STRING, UPPERCASE_ABC));

            // Edge cases
            assertTrue(IOCase.SENSITIVE.checkEndsWith(EMPTY_STRING, EMPTY_STRING));
            assertFalse(IOCase.SENSITIVE.checkEndsWith(UPPERCASE_ABC, null));
            assertFalse(IOCase.SENSITIVE.checkEndsWith(null, UPPERCASE_ABC));
            assertFalse(IOCase.SENSITIVE.checkEndsWith(null, null));
        }
    }

    @Nested
    @DisplayName("String Search Tests")
    class StringSearchTests {

        @Test
        @DisplayName("Should handle case sensitivity in indexOf")
        void shouldHandleCaseSensitivityInIndexOf() {
            assertEquals(1, IOCase.SENSITIVE.checkIndexOf(UPPERCASE_ABC, 0, "BC"));
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(UPPERCASE_ABC, 0, "Bc"));

            assertEquals(1, IOCase.INSENSITIVE.checkIndexOf(UPPERCASE_ABC, 0, "BC"));
            assertEquals(1, IOCase.INSENSITIVE.checkIndexOf(UPPERCASE_ABC, 0, "Bc"));

            assertEquals(1, IOCase.SYSTEM.checkIndexOf(UPPERCASE_ABC, 0, "BC"));
            assertEquals(IS_WINDOWS ? 1 : -1, IOCase.SYSTEM.checkIndexOf(UPPERCASE_ABC, 0, "Bc"));
        }

        @Test
        @DisplayName("Should find substrings at different positions")
        void shouldFindSubstringsAtDifferentPositions() {
            // Search from beginning
            assertEquals(0, IOCase.SENSITIVE.checkIndexOf(LONG_STRING, 0, "A"));
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG_STRING, 1, "A"));
            assertEquals(0, IOCase.SENSITIVE.checkIndexOf(LONG_STRING, 0, "ABC"));
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG_STRING, 1, "ABC"));

            // Search from middle
            assertEquals(3, IOCase.SENSITIVE.checkIndexOf(LONG_STRING, 0, "D"));
            assertEquals(3, IOCase.SENSITIVE.checkIndexOf(LONG_STRING, 3, "D"));
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG_STRING, 4, "D"));
            assertEquals(3, IOCase.SENSITIVE.checkIndexOf(LONG_STRING, 0, "DEF"));

            // Search from end
            assertEquals(9, IOCase.SENSITIVE.checkIndexOf(LONG_STRING, 0, "J"));
            assertEquals(8, IOCase.SENSITIVE.checkIndexOf(LONG_STRING, 0, "IJ"));
            assertEquals(7, IOCase.SENSITIVE.checkIndexOf(LONG_STRING, 6, "HIJ"));
        }

        @Test
        @DisplayName("Should handle search edge cases")
        void shouldHandleSearchEdgeCases() {
            // Not found
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(LONG_STRING, 0, "XYZ"));
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("DEF", 0, LONG_STRING)); // search string too long

            // Null handling
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(UPPERCASE_ABC, 0, null));
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, UPPERCASE_ABC));
            assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, null));
        }
    }

    @Nested
    @DisplayName("Region Matching Tests")
    class RegionMatchingTests {

        @Test
        @DisplayName("Should handle case sensitivity in region matching")
        void shouldHandleCaseSensitivityInRegionMatching() {
            assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPERCASE_ABC, 0, "AB"));
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPERCASE_ABC, 0, "Ab"));

            assertTrue(IOCase.INSENSITIVE.checkRegionMatches(UPPERCASE_ABC, 0, "AB"));
            assertTrue(IOCase.INSENSITIVE.checkRegionMatches(UPPERCASE_ABC, 0, "Ab"));

            assertTrue(IOCase.SYSTEM.checkRegionMatches(UPPERCASE_ABC, 0, "AB"));
            assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkRegionMatches(UPPERCASE_ABC, 0, "Ab"));
        }

        @Test
        @DisplayName("Should match regions at different starting positions")
        void shouldMatchRegionsAtDifferentStartingPositions() {
            // From position 0
            assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPERCASE_ABC, 0, EMPTY_STRING));
            assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPERCASE_ABC, 0, "A"));
            assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPERCASE_ABC, 0, "AB"));
            assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPERCASE_ABC, 0, UPPERCASE_ABC));
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPERCASE_ABC, 0, "ABCD"));

            // From position 1
            assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPERCASE_ABC, 1, EMPTY_STRING));
            assertTrue(IOCase.SENSITIVE.checkRegionMatches(UPPERCASE_ABC, 1, "BC"));
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPERCASE_ABC, 1, "A"));
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPERCASE_ABC, 1, "ABC"));
        }

        @Test
        @DisplayName("Should handle region matching edge cases")
        void shouldHandleRegionMatchingEdgeCases() {
            // Empty strings
            assertTrue(IOCase.SENSITIVE.checkRegionMatches(EMPTY_STRING, 0, EMPTY_STRING));
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(EMPTY_STRING, 0, UPPERCASE_ABC));
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(EMPTY_STRING, 1, EMPTY_STRING));

            // Null handling
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(UPPERCASE_ABC, 0, null));
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 0, UPPERCASE_ABC));
            assertFalse(IOCase.SENSITIVE.checkRegionMatches(null, 0, null));
        }
    }

    @Nested
    @DisplayName("Serialization Tests")
    class SerializationTests {

        @Test
        @DisplayName("Should maintain singleton property after serialization")
        void shouldMaintainSingletonPropertyAfterSerialization() throws Exception {
            assertSame(IOCase.SENSITIVE, serializeAndDeserialize(IOCase.SENSITIVE));
            assertSame(IOCase.INSENSITIVE, serializeAndDeserialize(IOCase.INSENSITIVE));
            assertSame(IOCase.SYSTEM, serializeAndDeserialize(IOCase.SYSTEM));
        }

        private IOCase serializeAndDeserialize(final IOCase ioCase) throws Exception {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(buffer)) {
                out.writeObject(ioCase);
                out.flush();
            }

            final ByteArrayInputStream inputBuffer = new ByteArrayInputStream(buffer.toByteArray());
            try (ObjectInputStream in = new ObjectInputStream(inputBuffer)) {
                return (IOCase) in.readObject();
            }
        }
    }

    // Note: The following tests appear to be misplaced IOUtils tests and should be moved to IOUtilsTest
    @Nested
    @DisplayName("Scratch Array Tests (Should be moved to IOUtilsTest)")
    class ScratchArrayTests {

        @Test
        @DisplayName("Should provide clean byte arrays")
        void shouldProvideCleanByteArrays() {
            final byte[] array = IOUtils.getScratchByteArray();
            assertAllElementsAreZero(array);
            Arrays.fill(array, (byte) 1);
            assertAllElementsAreZero(IOUtils.getScratchCharArray());
        }

        @Test
        @DisplayName("Should provide clean write-only byte arrays")
        void shouldProvideCleanWriteOnlyByteArrays() {
            final byte[] array = IOUtils.getScratchByteArrayWriteOnly();
            assertAllElementsAreZero(array);
            Arrays.fill(array, (byte) 1);
            assertAllElementsAreZero(IOUtils.getScratchCharArray());
        }

        @Test
        @DisplayName("Should provide clean char arrays")
        void shouldProvideCleanCharArrays() {
            final char[] array = IOUtils.getScratchCharArray();
            assertAllElementsAreZero(array);
            Arrays.fill(array, (char) 1);
            assertAllElementsAreZero(IOUtils.getScratchCharArray());
        }

        @Test
        @DisplayName("Should provide clean write-only char arrays")
        void shouldProvideCleanWriteOnlyCharArrays() {
            final char[] array = IOUtils.getScratchCharArrayWriteOnly();
            assertAllElementsAreZero(array);
            Arrays.fill(array, (char) 1);
            assertAllElementsAreZero(IOUtils.getScratchCharArray());
        }

        private void assertAllElementsAreZero(final byte[] array) {
            for (final byte element : array) {
                assertEquals(0, element, "All byte array elements should be zero");
            }
        }

        private void assertAllElementsAreZero(final char[] array) {
            for (final char element : array) {
                assertEquals(0, element, "All char array elements should be zero");
            }
        }
    }
}