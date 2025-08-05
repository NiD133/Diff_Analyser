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
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SegmentConstantPool class.
 */
class SegmentConstantPoolTest {

    private MockSegmentConstantPool mockPool;
    private String[] classNames;
    private String[] methodNames;

    /**
     * Mock implementation of SegmentConstantPool for testing purposes.
     */
    public class MockSegmentConstantPool extends SegmentConstantPool {

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

        public boolean regexMatchesVisible(final String regexString, final String compareString) {
            return SegmentConstantPool.regexMatches(regexString, compareString);
        }
    }

    @BeforeEach
    void setUp() {
        mockPool = new MockSegmentConstantPool();
        classNames = new String[] { "Object", "Object", "java/lang/String", "java/lang/String", "Object", "Other" };
        methodNames = new String[] { "<init>()", "clone()", "equals()", "<init>", "isNull()", "Other" };
    }

    @Test
    void testMatchSpecificPoolEntryIndex_WithClassAndMethodNames() {
        // Test matching class and method names with regex
        assertEquals(0, mockPool.matchSpecificPoolEntryIndex(classNames, methodNames, "Object", "^<init>.*", 0));
        assertEquals(2, mockPool.matchSpecificPoolEntryIndex(classNames, methodNames, "java/lang/String", ".*", 0));
        assertEquals(3, mockPool.matchSpecificPoolEntryIndex(classNames, methodNames, "java/lang/String", "^<init>.*", 0));
        assertEquals(5, mockPool.matchSpecificPoolEntryIndex(classNames, methodNames, "Other", ".*", 0));

        // Test non-existing elements
        assertEquals(-1, mockPool.matchSpecificPoolEntryIndex(classNames, methodNames, "NotThere", "^<init>.*", 0));

        // Test existing elements with insufficient matches
        assertEquals(-1, mockPool.matchSpecificPoolEntryIndex(classNames, methodNames, "java/lang/String", "^<init>.*", 1));
    }

    @Test
    void testMatchSpecificPoolEntryIndex_WithClassNamesOnly() {
        // Test matching class names only
        assertEquals(0, mockPool.matchSpecificPoolEntryIndex(classNames, "Object", 0));
        assertEquals(1, mockPool.matchSpecificPoolEntryIndex(classNames, "Object", 1));
        assertEquals(2, mockPool.matchSpecificPoolEntryIndex(classNames, "java/lang/String", 0));
        assertEquals(3, mockPool.matchSpecificPoolEntryIndex(classNames, "java/lang/String", 1));
        assertEquals(4, mockPool.matchSpecificPoolEntryIndex(classNames, "Object", 2));
        assertEquals(5, mockPool.matchSpecificPoolEntryIndex(classNames, "Other", 0));

        // Test non-existing elements
        assertEquals(-1, mockPool.matchSpecificPoolEntryIndex(classNames, "NotThere", 0));

        // Test existing elements with insufficient matches
        assertEquals(-1, mockPool.matchSpecificPoolEntryIndex(classNames, "java/lang/String", 2));
    }

    @Test
    void testRegexMatches() {
        // Test regex matching functionality
        assertTrue(mockPool.regexMatchesVisible(".*", "anything"));
        assertTrue(mockPool.regexMatchesVisible(".*", ""));
        assertTrue(mockPool.regexMatchesVisible("^<init>.*", "<init>"));
        assertTrue(mockPool.regexMatchesVisible("^<init>.*", "<init>stuff"));
        assertFalse(mockPool.regexMatchesVisible("^<init>.*", "init>stuff"));
        assertFalse(mockPool.regexMatchesVisible("^<init>.*", "<init"));
        assertFalse(mockPool.regexMatchesVisible("^<init>.*", ""));
    }
}