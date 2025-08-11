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
 * Tests for org.apache.commons.compress.harmony.unpack200.SegmentConstantPool.
 */
class SegmentConstantPoolTest {

    // Test data representing class names in a constant pool
    private static final String[] CLASS_NAMES = { 
        "Object", "Object", "java/lang/String", "java/lang/String", "Object", "Other" 
    };
    
    // Test data representing method names corresponding to the class names
    private static final String[] METHOD_NAMES = { 
        "<init>()", "clone()", "equals()", "<init>", "isNull()", "Other" 
    };
    
    // Common regex patterns used in tests
    private static final String MATCH_ALL_REGEX = ".*";
    private static final String MATCH_INIT_REGEX = "^<init>.*";
    
    private TestableSegmentConstantPool constantPool;

    @BeforeEach
    void setUp() {
        constantPool = new TestableSegmentConstantPool();
    }

    @Test
    void testFindPoolEntryWithClassAndMethodArrays_ShouldReturnCorrectIndices() {
        // Test finding first occurrence of Object with <init> method
        int actualIndex = constantPool.matchSpecificPoolEntryIndex(
            CLASS_NAMES, METHOD_NAMES, "Object", MATCH_INIT_REGEX, 0);
        assertEquals(0, actualIndex, "Should find first Object with <init> method at index 0");

        // Test finding first occurrence of java/lang/String with any method
        actualIndex = constantPool.matchSpecificPoolEntryIndex(
            CLASS_NAMES, METHOD_NAMES, "java/lang/String", MATCH_ALL_REGEX, 0);
        assertEquals(2, actualIndex, "Should find first java/lang/String at index 2");

        // Test finding first occurrence of java/lang/String with <init> method
        actualIndex = constantPool.matchSpecificPoolEntryIndex(
            CLASS_NAMES, METHOD_NAMES, "java/lang/String", MATCH_INIT_REGEX, 0);
        assertEquals(3, actualIndex, "Should find java/lang/String with <init> method at index 3");

        // Test finding first occurrence of Other class
        actualIndex = constantPool.matchSpecificPoolEntryIndex(
            CLASS_NAMES, METHOD_NAMES, "Other", MATCH_ALL_REGEX, 0);
        assertEquals(5, actualIndex, "Should find Other class at index 5");
    }

    @Test
    void testFindPoolEntryWithClassAndMethodArrays_ShouldReturnMinusOneWhenNotFound() {
        // Test searching for non-existent class
        int actualIndex = constantPool.matchSpecificPoolEntryIndex(
            CLASS_NAMES, METHOD_NAMES, "NonExistentClass", MATCH_INIT_REGEX, 0);
        assertEquals(-1, actualIndex, "Should return -1 when class doesn't exist");

        // Test searching for second occurrence of java/lang/String with <init> (only one exists)
        actualIndex = constantPool.matchSpecificPoolEntryIndex(
            CLASS_NAMES, METHOD_NAMES, "java/lang/String", MATCH_INIT_REGEX, 1);
        assertEquals(-1, actualIndex, "Should return -1 when requesting more occurrences than exist");
    }

    @Test
    void testFindPoolEntryWithSingleArray_ShouldReturnCorrectIndices() {
        // Test finding multiple occurrences of the same class name
        assertEquals(0, constantPool.matchSpecificPoolEntryIndex(CLASS_NAMES, "Object", 0),
            "Should find first Object at index 0");
        assertEquals(1, constantPool.matchSpecificPoolEntryIndex(CLASS_NAMES, "Object", 1),
            "Should find second Object at index 1");
        assertEquals(4, constantPool.matchSpecificPoolEntryIndex(CLASS_NAMES, "Object", 2),
            "Should find third Object at index 4");

        // Test finding multiple occurrences of java/lang/String
        assertEquals(2, constantPool.matchSpecificPoolEntryIndex(CLASS_NAMES, "java/lang/String", 0),
            "Should find first java/lang/String at index 2");
        assertEquals(3, constantPool.matchSpecificPoolEntryIndex(CLASS_NAMES, "java/lang/String", 1),
            "Should find second java/lang/String at index 3");

        // Test finding single occurrence
        assertEquals(5, constantPool.matchSpecificPoolEntryIndex(CLASS_NAMES, "Other", 0),
            "Should find Other class at index 5");
    }

    @Test
    void testFindPoolEntryWithSingleArray_ShouldReturnMinusOneWhenNotFound() {
        // Test searching for non-existent class
        int actualIndex = constantPool.matchSpecificPoolEntryIndex(CLASS_NAMES, "NonExistentClass", 0);
        assertEquals(-1, actualIndex, "Should return -1 when class doesn't exist");

        // Test requesting more occurrences than available
        actualIndex = constantPool.matchSpecificPoolEntryIndex(CLASS_NAMES, "java/lang/String", 2);
        assertEquals(-1, actualIndex, "Should return -1 when requesting third occurrence of class that appears only twice");
    }

    @Test
    void testRegexMatching_ShouldHandleMatchAllPattern() {
        assertTrue(constantPool.isRegexMatch(MATCH_ALL_REGEX, "anything"),
            "Match-all regex should match any non-empty string");
        assertTrue(constantPool.isRegexMatch(MATCH_ALL_REGEX, ""),
            "Match-all regex should match empty string");
    }

    @Test
    void testRegexMatching_ShouldHandleInitPattern() {
        assertTrue(constantPool.isRegexMatch(MATCH_INIT_REGEX, "<init>"),
            "Init regex should match exact <init> string");
        assertTrue(constantPool.isRegexMatch(MATCH_INIT_REGEX, "<init>stuff"),
            "Init regex should match <init> followed by other characters");
        
        assertFalse(constantPool.isRegexMatch(MATCH_INIT_REGEX, "init>stuff"),
            "Init regex should not match string missing opening bracket");
        assertFalse(constantPool.isRegexMatch(MATCH_INIT_REGEX, "<init"),
            "Init regex should not match string missing closing bracket");
        assertFalse(constantPool.isRegexMatch(MATCH_INIT_REGEX, ""),
            "Init regex should not match empty string");
    }

    /**
     * Test double that exposes protected methods for testing.
     * This allows us to test the internal logic without relying on complex setup.
     */
    private static class TestableSegmentConstantPool extends SegmentConstantPool {

        TestableSegmentConstantPool() {
            super(new CpBands(new Segment()));
        }

        @Override
        public int matchSpecificPoolEntryIndex(final String[] classNameArray, 
                                             final String desiredClassName, 
                                             final int desiredIndex) {
            return super.matchSpecificPoolEntryIndex(classNameArray, desiredClassName, desiredIndex);
        }

        @Override
        public int matchSpecificPoolEntryIndex(final String[] classNameArray, 
                                             final String[] methodNameArray, 
                                             final String desiredClassName,
                                             final String desiredMethodRegex, 
                                             final int desiredIndex) {
            return super.matchSpecificPoolEntryIndex(classNameArray, methodNameArray, 
                                                   desiredClassName, desiredMethodRegex, desiredIndex);
        }

        /**
         * Exposes the protected regexMatches method with a more descriptive name.
         */
        public boolean isRegexMatch(final String regexPattern, final String testString) {
            return SegmentConstantPool.regexMatches(regexPattern, testString);
        }
    }
}