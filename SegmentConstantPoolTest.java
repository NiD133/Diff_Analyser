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

import org.junit.jupiter.api.Test;

/**
 * Tests for SegmentConstantPool's search helpers and regex matcher.
 *
 * The arrays below intentionally contain repeated class names and method
 * names so we can verify "nth occurrence" lookups and class+method pair filtering.
 */
class SegmentConstantPoolTest {

    /**
     * Simple subclass that exposes protected APIs we want to test and the protected regex helper.
     */
    static class MockSegmentConstantPool extends SegmentConstantPool {
        MockSegmentConstantPool() {
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
        boolean regexMatches(final String regexString, final String compareString) {
            return SegmentConstantPool.regexMatches(regexString, compareString);
        }
    }

    // Class names aligned with comments to make "nth occurrence" readable at a glance.
    // index: value         // occurrence counter in parentheses for that value
    private static final String[] CLASS_NAMES = {
        "Object",            // 0: Object (0)
        "Object",            // 1: Object (1)
        "java/lang/String",  // 2: String (0)
        "java/lang/String",  // 3: String (1)
        "Object",            // 4: Object (2)
        "Other"              // 5: Other (0)
    };

    // Method names aligned to the same indices as CLASS_NAMES.
    private static final String[] METHOD_NAMES = {
        "<init>()",  // 0
        "clone()",   // 1
        "equals()",  // 2
        "<init>",    // 3
        "isNull()",  // 4
        "Other"      // 5
    };

    private static MockSegmentConstantPool newPool() {
        return new MockSegmentConstantPool();
    }

    // Helpers to express intent clearly in the tests below.
    private static void assertMatchInSingleArray(final int expectedIndex, final String className, final int nthOccurrence) {
        assertEquals(expectedIndex, newPool().matchSpecificPoolEntryIndex(CLASS_NAMES, className, nthOccurrence),
                () -> "Expected " + className + " occurrence #" + nthOccurrence + " at index " + expectedIndex);
    }

    private static void assertMatchInDoubleArray(final int expectedIndex, final String className, final String methodRegex, final int nthOccurrence) {
        assertEquals(expectedIndex, newPool().matchSpecificPoolEntryIndex(CLASS_NAMES, METHOD_NAMES, className, methodRegex, nthOccurrence),
                () -> "Expected (" + className + ", " + methodRegex + ") occurrence #" + nthOccurrence + " at index " + expectedIndex);
    }

    @Test
    void matchSpecificPoolEntryIndex_singleArray_findsNthOccurrence() {
        // Verify correct index for each nth occurrence of a class name.
        assertMatchInSingleArray(0, "Object", 0);
        assertMatchInSingleArray(1, "Object", 1);
        assertMatchInSingleArray(4, "Object", 2);

        assertMatchInSingleArray(2, "java/lang/String", 0);
        assertMatchInSingleArray(3, "java/lang/String", 1);

        assertMatchInSingleArray(5, "Other", 0);
    }

    @Test
    void matchSpecificPoolEntryIndex_singleArray_returnsMinusOneWhenNotFound() {
        // Class not present
        assertMatchInSingleArray(-1, "NotThere", 0);
        // Class present but not enough occurrences
        assertMatchInSingleArray(-1, "java/lang/String", 2);
    }

    @Test
    void matchSpecificPoolEntryIndex_doubleArray_findsClassAndRegexPairs() {
        // nth occurrence among pairs (class equals, method matches regex)
        assertMatchInDoubleArray(0, "Object", "^<init>.*", 0);         // "<init>()" matches
        assertMatchInDoubleArray(2, "java/lang/String", ".*", 0);      // first String, any method
        assertMatchInDoubleArray(3, "java/lang/String", "^<init>.*", 0); // "<init>" matches
        assertMatchInDoubleArray(5, "Other", ".*", 0);                 // any method
    }

    @Test
    void matchSpecificPoolEntryIndex_doubleArray_returnsMinusOneWhenNoMatch() {
        // Class not present at all
        assertMatchInDoubleArray(-1, "NotThere", "^<init>.*", 0);
        // Class present but not enough matches of the regex
        assertMatchInDoubleArray(-1, "java/lang/String", "^<init>.*", 1);
    }

    @Test
    void regexMatches_supportsTheTwoSpecialForms() {
        final MockSegmentConstantPool pool = newPool();

        // ".*" matches everything, including empty
        assertTrue(pool.regexMatches(".*", "anything"));
        assertTrue(pool.regexMatches(".*", ""));

        // "^<init>.*" must start with "<init>"
        assertTrue(pool.regexMatches("^<init>.*", "<init>"));
        assertTrue(pool.regexMatches("^<init>.*", "<init>stuff"));

        // Negative cases
        assertFalse(pool.regexMatches("^<init>.*", "init>stuff")); // missing leading '<'
        assertFalse(pool.regexMatches("^<init>.*", "<init"));      // no trailing characters allowed by ".*" but prefix must be complete
        assertFalse(pool.regexMatches("^<init>.*", ""));
    }
}