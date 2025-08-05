/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for protected helper methods in {@link SegmentConstantPool}.
 */
@DisplayName("SegmentConstantPool Helper Methods")
class SegmentConstantPoolTest {

    /**
     * A mock of {@link SegmentConstantPool} to make protected methods accessible for testing.
     */
    private static class TestableSegmentConstantPool extends SegmentConstantPool {

        TestableSegmentConstantPool() {
            super(new CpBands(new Segment()));
        }

        @Override
        public int matchSpecificPoolEntryIndex(final String[] classNameArray, final String desiredClassName, final int desiredIndex) {
            return super.matchSpecificPoolEntryIndex(classNameArray, desiredClassName, desiredIndex);
        }

        @Override
        public int matchSpecificPoolEntryIndex(final String[] classNameArray, final String[] methodNameArray, final String desiredClassName,
                final String desiredMethodRegex, final int desiredIndex) {
            return super.matchSpecificPoolEntryIndex(classNameArray, methodNameArray, desiredClassName, desiredMethodRegex, desiredIndex);
        }

        public boolean isRegexMatch(final String regexString, final String compareString) {
            return SegmentConstantPool.regexMatches(regexString, compareString);
        }
    }

    private TestableSegmentConstantPool pool;

    @BeforeEach
    void setUp() {
        pool = new TestableSegmentConstantPool();
    }

    @Nested
    @DisplayName("when matching in a single array")
    class MatchInSingleArray {

        // Data: ["Object", "Object", "java/lang/String", "java/lang/String", "Object", "Other"]
        // Index:    0         1             2                   3               4         5
        private final String[] classNames = {"Object", "Object", "java/lang/String", "java/lang/String", "Object", "Other"};

        @Test
        @DisplayName("should find the correct index for the Nth occurrence of an entry")
        void shouldFindNthOccurrence() {
            assertEquals(0, pool.matchSpecificPoolEntryIndex(classNames, "Object", 0)); // 1st "Object"
            assertEquals(1, pool.matchSpecificPoolEntryIndex(classNames, "Object", 1)); // 2nd "Object"
            assertEquals(4, pool.matchSpecificPoolEntryIndex(classNames, "Object", 2)); // 3rd "Object"

            assertEquals(2, pool.matchSpecificPoolEntryIndex(classNames, "java/lang/String", 0)); // 1st "String"
            assertEquals(3, pool.matchSpecificPoolEntryIndex(classNames, "java/lang/String", 1)); // 2nd "String"

            assertEquals(5, pool.matchSpecificPoolEntryIndex(classNames, "Other", 0)); // 1st "Other"
        }

        @Test
        @DisplayName("should return -1 when the desired entry does not exist")
        void shouldReturnNegativeOneForMissingEntry() {
            final int index = pool.matchSpecificPoolEntryIndex(classNames, "NonExistentClass", 0);
            assertEquals(-1, index);
        }

        @Test
        @DisplayName("should return -1 when there are fewer occurrences than requested")
        void shouldReturnNegativeOneForInsufficientOccurrences() {
            // There are only 2 "java/lang/String" entries, so asking for the 3rd (index 2) should fail.
            final int index = pool.matchSpecificPoolEntryIndex(classNames, "java/lang/String", 2);
            assertEquals(-1, index);
        }
    }

    @Nested
    @DisplayName("when matching in two parallel arrays")
    class MatchInTwoArrays {

        // Data:    ["Object", "Object", "j/l/String", "j/l/String", "Object", "Other"]
        // Methods: ["<init>()", "clone()", "equals()", "<init>", "isNull()", "Other"]
        // Index:        0         1          2           3           4         5
        private final String[] classNames = {"Object", "Object", "java/lang/String", "java/lang/String", "Object", "Other"};
        private final String[] methodNames = {"<init>()", "clone()", "equals()", "<init>", "isNull()", "Other"};

        @Test
        @DisplayName("should find the correct index for matching class and method regex")
        void shouldFindMatchingClassAndMethod() {
            assertEquals(0, pool.matchSpecificPoolEntryIndex(classNames, methodNames, "Object", "^<init>.*", 0));
            assertEquals(2, pool.matchSpecificPoolEntryIndex(classNames, methodNames, "java/lang/String", ".*", 0));
            assertEquals(3, pool.matchSpecificPoolEntryIndex(classNames, methodNames, "java/lang/String", "^<init>.*", 0));
            assertEquals(5, pool.matchSpecificPoolEntryIndex(classNames, methodNames, "Other", ".*", 0));
        }

        @Test
        @DisplayName("should return -1 when the desired class does not exist")
        void shouldReturnNegativeOneForMissingClass() {
            final int index = pool.matchSpecificPoolEntryIndex(classNames, methodNames, "NonExistentClass", ".*", 0);
            assertEquals(-1, index);
        }

        @Test
        @DisplayName("should return -1 when there are fewer occurrences than requested")
        void shouldReturnNegativeOneForInsufficientOccurrences() {
            // There is only one "java/lang/String" with a method matching "^<init>.*", so asking for the 2nd (index 1) should fail.
            final int index = pool.matchSpecificPoolEntryIndex(classNames, methodNames, "java/lang/String", "^<init>.*", 1);
            assertEquals(-1, index);
        }
    }

    @DisplayName("should correctly perform simple regex matching")
    @ParameterizedTest(name = "[{index}] regex=\"{0}\", input=\"{1}\" -> should match = {2}")
    @CsvSource({
        // ".*" (match all) regex
        "'.*', 'anything',   true",
        "'.*', '',           true",
        // "^<init>.*" (starts with <init>) regex
        "'^<init>.*', '<init>',       true",
        "'^<init>.*', '<init>stuff',  true",
        "'^<init>.*', 'init>stuff',   false",
        "'^<init>.*', '<init',        false",
        "'^<init>.*', '',             false"
    })
    void regexMatches(final String regex, final String input, final boolean shouldMatch) {
        assertEquals(shouldMatch, pool.isRegexMatch(regex, input));
    }
}