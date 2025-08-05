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
package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIndexOutOfBoundsException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for CharSequenceUtils utility class
 */
class CharSequenceUtilsTest extends AbstractLangTest {

    /**
     * Test data for regionMatches method testing.
     * Contains various combinations of source strings, target strings, offsets, and expected results.
     */
    static class RegionMatchesTestCase {
        final String sourceString;
        final boolean ignoreCase;
        final int sourceOffset;
        final String targetString;
        final int targetOffset;
        final int length;
        final boolean expectedResult;
        final Class<? extends Throwable> expectedException;

        /**
         * Constructor for test cases that should return a boolean result
         */
        RegionMatchesTestCase(String sourceString, boolean ignoreCase, int sourceOffset, 
                             String targetString, int targetOffset, int length, boolean expectedResult) {
            this.sourceString = sourceString;
            this.ignoreCase = ignoreCase;
            this.sourceOffset = sourceOffset;
            this.targetString = targetString;
            this.targetOffset = targetOffset;
            this.length = length;
            this.expectedResult = expectedResult;
            this.expectedException = null;
        }

        /**
         * Constructor for test cases that should throw an exception
         */
        RegionMatchesTestCase(String sourceString, boolean ignoreCase, int sourceOffset, 
                             String targetString, int targetOffset, int length, 
                             Class<? extends Throwable> expectedException) {
            this.sourceString = sourceString;
            this.ignoreCase = ignoreCase;
            this.sourceOffset = sourceOffset;
            this.targetString = targetString;
            this.targetOffset = targetOffset;
            this.length = length;
            this.expectedResult = false;
            this.expectedException = expectedException;
        }

        @Override
        public String toString() {
            return String.format("RegionMatches[source='%s'[%d], target='%s'[%d], len=%d, ignoreCase=%s] -> %s",
                    sourceString, sourceOffset, targetString, targetOffset, length, ignoreCase,
                    expectedException != null ? expectedException.getSimpleName() : expectedResult);
        }
    }

    /**
     * Simple wrapper around CharSequence to test non-String implementations
     */
    static class CharSequenceWrapper implements CharSequence {
        private final CharSequence wrapped;

        CharSequenceWrapper(CharSequence wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public char charAt(int index) {
            return wrapped.charAt(index);
        }

        @Override
        public IntStream chars() {
            return wrapped.chars();
        }

        @Override
        public IntStream codePoints() {
            return wrapped.codePoints();
        }

        @Override
        public int length() {
            return wrapped.length();
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return wrapped.subSequence(start, end);
        }

        @Override
        public String toString() {
            return wrapped.toString();
        }
    }

    /**
     * Test data for regionMatches functionality
     */
    private static final RegionMatchesTestCase[] REGION_MATCHES_TEST_CASES = {
            // Basic empty string cases
            new RegionMatchesTestCase("", true, -1, "", -1, -1, false),
            new RegionMatchesTestCase("", true, 0, "", 0, 1, false),
            
            // Simple matching cases
            new RegionMatchesTestCase("a", true, 0, "abc", 0, 0, true),
            new RegionMatchesTestCase("a", true, 0, "abc", 0, 1, true),
            
            // Null handling cases
            new RegionMatchesTestCase("a", true, 0, null, 0, 0, NullPointerException.class),
            new RegionMatchesTestCase(null, true, 0, null, 0, 0, NullPointerException.class),
            new RegionMatchesTestCase(null, true, 0, "", 0, 0, NullPointerException.class),
            
            // Case sensitivity tests
            new RegionMatchesTestCase("Abc", true, 0, "abc", 0, 3, true),   // ignore case
            new RegionMatchesTestCase("Abc", false, 0, "abc", 0, 3, false), // case sensitive
            
            // Offset tests
            new RegionMatchesTestCase("Abc", true, 1, "abc", 1, 2, true),   // both with offset
            new RegionMatchesTestCase("Abc", false, 1, "abc", 1, 2, true),  // case matches with offset
            new RegionMatchesTestCase("Abcd", true, 1, "abcD", 1, 2, true), // ignore case with offset
            new RegionMatchesTestCase("Abcd", false, 1, "abcD", 1, 2, true), // case matches with offset
    };

    /**
     * Provides test data for lastIndexOf tests with different CharSequence implementations
     */
    static Stream<Arguments> lastIndexOfTestData() {
        return Stream.of(
            arguments("abc", "b", 2, 1),
            arguments(new StringBuilder("abc"), "b", 2, 1),
            arguments(new StringBuffer("abc"), "b", 2, 1),
            arguments("abc", new StringBuilder("b"), 2, 1),
            arguments(new StringBuilder("abc"), new StringBuilder("b"), 2, 1),
            arguments(new StringBuffer("abc"), new StringBuffer("b"), 2, 1),
            arguments(new StringBuilder("abc"), new StringBuffer("b"), 2, 1)
        );
    }

    @Test
    void testConstructorAccessibility() {
        // Verify constructor can be instantiated
        assertNotNull(new CharSequenceUtils());
        
        // Verify class structure
        Constructor<?>[] constructors = CharSequenceUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()));
        assertTrue(Modifier.isPublic(CharSequenceUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(CharSequenceUtils.class.getModifiers()));
    }

    @ParameterizedTest
    @MethodSource("lastIndexOfTestData")
    void testLastIndexOfWithVariousCharSequenceTypes(CharSequence source, CharSequence searchString, 
                                                     int startIndex, int expectedIndex) {
        int actualIndex = CharSequenceUtils.lastIndexOf(source, searchString, startIndex);
        assertEquals(expectedIndex, actualIndex, 
                String.format("lastIndexOf failed for source='%s', search='%s', start=%d", 
                        source, searchString, startIndex));
    }

    @Test
    void testLastIndexOfComprehensive() {
        // Test specific known cases
        verifyLastIndexOfBehavior("808087847-1321060740-635567660180086727-925755305", 
                                 "-1321060740-635567660", 21);
        
        // Test edge cases
        verifyLastIndexOfBehavior("", "");
        verifyLastIndexOfBehavior("1", "");
        verifyLastIndexOfBehavior("", "1");
        verifyLastIndexOfBehavior("1", "1");
        verifyLastIndexOfBehavior("11", "1");
        verifyLastIndexOfBehavior("1", "11");

        // Test common patterns
        verifyLastIndexOfBehavior("apache", "a");
        verifyLastIndexOfBehavior("apache", "p");
        verifyLastIndexOfBehavior("apache", "e");
        verifyLastIndexOfBehavior("apache", "x");
        verifyLastIndexOfBehavior("oraoraoraora", "r");
        verifyLastIndexOfBehavior("mudamudamudamuda", "d");
        
        // Test partial match scenario - ensures proper handling of partial matches
        verifyLastIndexOfBehavior("junk-ststarting", "starting");

        // Test with large strings to verify performance edge cases
        testLastIndexOfWithLargeStrings();
    }

    /**
     * Tests lastIndexOf with randomly generated large strings to verify behavior
     * matches String.lastIndexOf in all cases
     */
    private void testLastIndexOfWithLargeStrings() {
        Random random = new Random(12345); // Fixed seed for reproducible tests
        StringBuilder segment = new StringBuilder();
        
        // Build a segment longer than TO_STRING_LIMIT
        while (segment.length() <= CharSequenceUtils.TO_STRING_LIMIT) {
            segment.append(random.nextInt());
        }
        
        StringBuilder testString = new StringBuilder(segment);
        verifyLastIndexOfBehavior(testString, segment);
        
        // Test with variations of the original string
        for (int i = 0; i < 100; i++) {
            if (random.nextDouble() < 0.5) {
                testString.append(random.nextInt() % 10);
            } else {
                testString = new StringBuilder().append(random.nextInt() % 100).append(testString);
            }
            verifyLastIndexOfBehavior(testString, segment);
        }
    }

    /**
     * Verifies that CharSequenceUtils.lastIndexOf behaves identically to String.lastIndexOf
     * for all reasonable start positions
     */
    private void verifyLastIndexOfBehavior(CharSequence source, CharSequence searchString) {
        int maxLength = Math.max(source.length(), searchString.length());
        
        // Test a range of start positions around the string boundaries
        for (int startPos = -maxLength - 10; startPos <= maxLength + 10; startPos++) {
            verifyLastIndexOfAtPosition(source, searchString, startPos);
        }
        
        // Test extreme values
        verifyLastIndexOfAtPosition(source, searchString, Integer.MIN_VALUE);
        verifyLastIndexOfAtPosition(source, searchString, Integer.MAX_VALUE);
    }

    /**
     * Verifies lastIndexOf behavior at a specific start position
     */
    private void verifyLastIndexOfAtPosition(CharSequence source, CharSequence searchString, int startPos) {
        // Test both directions to ensure symmetry
        compareWithStringLastIndexOf(source, searchString, startPos);
        compareWithStringLastIndexOf(searchString, source, startPos);
    }

    /**
     * Compares CharSequenceUtils.lastIndexOf with String.lastIndexOf for the given parameters
     */
    private void compareWithStringLastIndexOf(CharSequence source, CharSequence searchString, int startPos) {
        String sourceStr = source.toString();
        String searchStr = searchString.toString();
        
        int expectedResult = sourceStr.lastIndexOf(searchStr, startPos);
        int actualResult = CharSequenceUtils.lastIndexOf(
                new CharSequenceWrapper(sourceStr), 
                new CharSequenceWrapper(searchStr), 
                startPos);
        
        assertEquals(expectedResult, actualResult,
                String.format("lastIndexOf mismatch: source='%s', search='%s', start=%d", 
                        source, searchString, startPos));
    }

    @Test
    void testRegionMatches() {
        for (RegionMatchesTestCase testCase : REGION_MATCHES_TEST_CASES) {
            testRegionMatchesWithString(testCase);
            testRegionMatchesWithCharSequenceUtils(testCase);
            testRegionMatchesWithNonStringCharSequence(testCase);
        }
    }

    /**
     * Tests String.regionMatches with the given test case
     */
    private void testRegionMatchesWithString(RegionMatchesTestCase testCase) {
        if (testCase.expectedException != null) {
            assertThrows(testCase.expectedException, 
                    () -> testCase.sourceString.regionMatches(testCase.ignoreCase, testCase.sourceOffset, 
                            testCase.targetString, testCase.targetOffset, testCase.length),
                    "String.regionMatches should throw " + testCase.expectedException.getSimpleName());
        } else {
            boolean result = testCase.sourceString.regionMatches(testCase.ignoreCase, testCase.sourceOffset, 
                    testCase.targetString, testCase.targetOffset, testCase.length);
            assertEquals(testCase.expectedResult, result, 
                    "String.regionMatches failed for: " + testCase);
        }
    }

    /**
     * Tests CharSequenceUtils.regionMatches with String inputs
     */
    private void testRegionMatchesWithCharSequenceUtils(RegionMatchesTestCase testCase) {
        if (testCase.expectedException != null) {
            assertThrows(testCase.expectedException, 
                    () -> CharSequenceUtils.regionMatches(testCase.sourceString, testCase.ignoreCase, 
                            testCase.sourceOffset, testCase.targetString, testCase.targetOffset, testCase.length),
                    "CharSequenceUtils.regionMatches should throw " + testCase.expectedException.getSimpleName());
        } else {
            boolean result = CharSequenceUtils.regionMatches(testCase.sourceString, testCase.ignoreCase, 
                    testCase.sourceOffset, testCase.targetString, testCase.targetOffset, testCase.length);
            assertEquals(testCase.expectedResult, result, 
                    "CharSequenceUtils.regionMatches failed for: " + testCase);
        }
    }

    /**
     * Tests CharSequenceUtils.regionMatches with StringBuilder (non-String CharSequence)
     */
    private void testRegionMatchesWithNonStringCharSequence(RegionMatchesTestCase testCase) {
        if (testCase.expectedException != null) {
            assertThrows(testCase.expectedException, 
                    () -> CharSequenceUtils.regionMatches(new StringBuilder(testCase.sourceString), 
                            testCase.ignoreCase, testCase.sourceOffset, testCase.targetString, 
                            testCase.targetOffset, testCase.length),
                    "CharSequenceUtils.regionMatches with StringBuilder should throw " + 
                    testCase.expectedException.getSimpleName());
        } else {
            boolean result = CharSequenceUtils.regionMatches(new StringBuilder(testCase.sourceString), 
                    testCase.ignoreCase, testCase.sourceOffset, testCase.targetString, 
                    testCase.targetOffset, testCase.length);
            assertEquals(testCase.expectedResult, result, 
                    "CharSequenceUtils.regionMatches with StringBuilder failed for: " + testCase);
        }
    }

    @Test
    void testSubSequenceWithValidInputs() {
        // Test with empty string
        assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence(StringUtils.EMPTY, 0));
        
        // Test with normal string
        assertEquals("012", CharSequenceUtils.subSequence("012", 0));
        assertEquals("12", CharSequenceUtils.subSequence("012", 1));
        assertEquals("2", CharSequenceUtils.subSequence("012", 2));
        assertEquals(StringUtils.EMPTY, CharSequenceUtils.subSequence("012", 3));
    }

    @Test
    void testSubSequenceWithNullInput() {
        assertNull(CharSequenceUtils.subSequence(null, -1));
        assertNull(CharSequenceUtils.subSequence(null, 0));
        assertNull(CharSequenceUtils.subSequence(null, 1));
    }

    @Test
    void testSubSequenceWithNegativeStart() {
        assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence(StringUtils.EMPTY, -1),
                "subSequence should throw IndexOutOfBoundsException for negative start");
    }

    @Test
    void testSubSequenceWithStartBeyondLength() {
        assertIndexOutOfBoundsException(() -> CharSequenceUtils.subSequence(StringUtils.EMPTY, 1),
                "subSequence should throw IndexOutOfBoundsException when start > length");
    }

    @Test
    void testToCharArray() {
        // Test with StringBuilder
        StringBuilder builder = new StringBuilder("abcdefg");
        char[] expected = builder.toString().toCharArray();
        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder),
                "toCharArray should work correctly with StringBuilder");
        
        // Test with String
        assertArrayEquals(expected, CharSequenceUtils.toCharArray(builder.toString()),
                "toCharArray should work correctly with String");
        
        // Test with null
        assertArrayEquals(ArrayUtils.EMPTY_CHAR_ARRAY, CharSequenceUtils.toCharArray(null),
                "toCharArray should return empty array for null input");
    }
}