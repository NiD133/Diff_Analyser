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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SegmentConstantPool}.
 */
class SegmentConstantPoolTest {

    // Mock class to test protected methods
    static class MockSegmentConstantPool extends SegmentConstantPool {
        MockSegmentConstantPool() {
            super(new CpBands(new Segment()));
        }

        @Override
        public int matchSpecificPoolEntryIndex(String[] classNameArray, String desiredClassName, int desiredIndex) {
            return super.matchSpecificPoolEntryIndex(classNameArray, desiredClassName, desiredIndex);
        }

        @Override
        public int matchSpecificPoolEntryIndex(String[] classNameArray, String[] methodNameArray, String desiredClassName,
                String desiredMethodRegex, int desiredIndex) {
            return super.matchSpecificPoolEntryIndex(classNameArray, methodNameArray, desiredClassName, desiredMethodRegex, desiredIndex);
        }

        public boolean regexMatchesVisible(String regexString, String compareString) {
            return SegmentConstantPool.regexMatches(regexString, compareString);
        }
    }

    private MockSegmentConstantPool mockInstance;
    private final String[] testClassArray = { "Object", "Object", "java/lang/String", "java/lang/String", "Object", "Other" };
    private final String[] testMethodArray = { "<init>()", "clone()", "equals()", "<init>", "isNull()", "Other" };

    @BeforeEach
    void setUp() {
        mockInstance = new MockSegmentConstantPool();
    }

    @Nested
    class MatchSpecificPoolEntryIndexWithTwoArraysTest {
        @Test
        void findFirstObjectWithInitMethod() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, testMethodArray, "Object", "^<init>.*", 0);
            assertEquals(0, index);
        }

        @Test
        void findFirstStringWithAnyMethod() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, testMethodArray, "java/lang/String", ".*", 0);
            assertEquals(2, index);
        }

        @Test
        void findStringWithInitMethod() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, testMethodArray, "java/lang/String", "^<init>.*", 0);
            assertEquals(3, index);
        }

        @Test
        void findOtherClassWithAnyMethod() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, testMethodArray, "Other", ".*", 0);
            assertEquals(5, index);
        }

        @Test
        void returnNotFoundForMissingClass() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, testMethodArray, "NotThere", "^<init>.*", 0);
            assertEquals(-1, index);
        }

        @Test
        void returnNotFoundWhenOccurrenceIndexExceedsMatches() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, testMethodArray, "java/lang/String", "^<init>.*", 1);
            assertEquals(-1, index);
        }
    }

    @Nested
    class MatchSpecificPoolEntryIndexWithOneArrayTest {
        @Test
        void findFirstObjectClass() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, "Object", 0);
            assertEquals(0, index);
        }

        @Test
        void findSecondObjectClass() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, "Object", 1);
            assertEquals(1, index);
        }

        @Test
        void findThirdObjectClass() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, "Object", 2);
            assertEquals(4, index);
        }

        @Test
        void findFirstStringClass() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, "java/lang/String", 0);
            assertEquals(2, index);
        }

        @Test
        void findSecondStringClass() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, "java/lang/String", 1);
            assertEquals(3, index);
        }

        @Test
        void findOtherClass() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, "Other", 0);
            assertEquals(5, index);
        }

        @Test
        void returnNotFoundForMissingClass() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, "NotThere", 0);
            assertEquals(-1, index);
        }

        @Test
        void returnNotFoundWhenOccurrenceIndexExceedsMatches() {
            int index = mockInstance.matchSpecificPoolEntryIndex(testClassArray, "java/lang/String", 2);
            assertEquals(-1, index);
        }
    }

    @Nested
    class RegexMatchingTest {
        @Test
        void matchAllPatternMatchesAnyString() {
            assertTrue(mockInstance.regexMatchesVisible(".*", "any content"));
        }

        @Test
        void matchAllPatternMatchesEmptyString() {
            assertTrue(mockInstance.regexMatchesVisible(".*", ""));
        }

        @Test
        void initPatternMatchesExactInitString() {
            assertTrue(mockInstance.regexMatchesVisible("^<init>.*", "<init>"));
        }

        @Test
        void initPatternMatchesInitWithSuffix() {
            assertTrue(mockInstance.regexMatchesVisible("^<init>.*", "<init>abc"));
        }

        @Test
        void initPatternDoesNotMatchMissingOpeningBracket() {
            assertFalse(mockInstance.regexMatchesVisible("^<init>.*", "init>abc"));
        }

        @Test
        void initPatternDoesNotMatchIncompleteInit() {
            assertFalse(mockInstance.regexMatchesVisible("^<init>.*", "<init"));
        }

        @Test
        void initPatternDoesNotMatchEmptyString() {
            assertFalse(mockInstance.regexMatchesVisible("^<init>.*", ""));
        }
    }
}