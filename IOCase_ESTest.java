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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    private static final String FOO_LOWER = "foo";
    private static final String FOO_UPPER = "FOO";
    private static final String FOOBAR_MIXED = "FooBar";

    //-----------------------------------------------------------------------
    // Factory method tests
    //-----------------------------------------------------------------------

    @Test
    public void forName_shouldReturnCorrectEnum() {
        assertEquals(IOCase.SENSITIVE, IOCase.forName("Sensitive"));
        assertEquals(IOCase.INSENSITIVE, IOCase.forName("Insensitive"));
        assertEquals(IOCase.SYSTEM, IOCase.forName("System"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void forName_shouldThrowExceptionForInvalidName() {
        IOCase.forName("InvalidName");
    }

    @Test
    public void value_shouldReturnGivenValueWhenNotNull() {
        assertEquals(IOCase.SENSITIVE, IOCase.value(IOCase.SENSITIVE, IOCase.INSENSITIVE));
    }

    @Test
    public void value_shouldReturnDefaultValueWhenGivenValueIsNull() {
        assertEquals(IOCase.SYSTEM, IOCase.value(null, IOCase.SYSTEM));
    }

    //-----------------------------------------------------------------------
    // Static utility method tests
    //-----------------------------------------------------------------------

    @Test
    public void isCaseSensitive_static_shouldReturnCorrectState() {
        assertTrue(IOCase.isCaseSensitive(IOCase.SENSITIVE));
        assertFalse(IOCase.isCaseSensitive(IOCase.INSENSITIVE));
        assertEquals(FileSystem.getCurrent().isCaseSensitive(), IOCase.isCaseSensitive(IOCase.SYSTEM));
    }

    @Test
    public void isCaseSensitive_static_shouldReturnFalseForNull() {
        assertFalse(IOCase.isCaseSensitive(null));
    }

    //-----------------------------------------------------------------------
    // Instance method tests
    //-----------------------------------------------------------------------

    @Test
    public void getName_shouldReturnEnumName() {
        assertEquals("Sensitive", IOCase.SENSITIVE.getName());
        assertEquals("Insensitive", IOCase.INSENSITIVE.getName());
        assertEquals("System", IOCase.SYSTEM.getName());
    }

    @Test
    public void toString_shouldReturnEnumName() {
        assertEquals("Sensitive", IOCase.SENSITIVE.toString());
        assertEquals("Insensitive", IOCase.INSENSITIVE.toString());
        assertEquals("System", IOCase.SYSTEM.toString());
    }

    @Test
    public void isCaseSensitive_instance_shouldReturnCorrectState() {
        assertTrue(IOCase.SENSITIVE.isCaseSensitive());
        assertFalse(IOCase.INSENSITIVE.isCaseSensitive());
        assertEquals(FileSystem.getCurrent().isCaseSensitive(), IOCase.SYSTEM.isCaseSensitive());
    }

    //-----------------------------------------------------------------------
    // checkEquals() tests
    //-----------------------------------------------------------------------

    @Test
    public void checkEquals_shouldHandleComparisonsCorrectly() {
        // Sensitive
        assertTrue(IOCase.SENSITIVE.checkEquals(FOO_LOWER, FOO_LOWER));
        assertFalse(IOCase.SENSITIVE.checkEquals(FOO_LOWER, FOO_UPPER));

        // Insensitive
        assertTrue(IOCase.INSENSITIVE.checkEquals(FOO_LOWER, FOO_LOWER));
        assertTrue(IOCase.INSENSITIVE.checkEquals(FOO_LOWER, FOO_UPPER));
        assertFalse(IOCase.INSENSITIVE.checkEquals(FOO_LOWER, "bar"));

        // System
        if (IOCase.SYSTEM.isCaseSensitive()) {
            assertTrue(IOCase.SYSTEM.checkEquals(FOO_LOWER, FOO_LOWER));
            assertFalse(IOCase.SYSTEM.checkEquals(FOO_LOWER, FOO_UPPER));
        } else {
            assertTrue(IOCase.SYSTEM.checkEquals(FOO_LOWER, FOO_LOWER));
            assertTrue(IOCase.SYSTEM.checkEquals(FOO_LOWER, FOO_UPPER));
        }
    }

    @Test
    public void checkEquals_shouldBeNullSafe() {
        assertTrue(IOCase.SENSITIVE.checkEquals(null, null));
        assertFalse(IOCase.SENSITIVE.checkEquals(FOO_LOWER, null));
        assertFalse(IOCase.SENSITIVE.checkEquals(null, FOO_LOWER));
    }

    //-----------------------------------------------------------------------
    // checkCompareTo() tests
    //-----------------------------------------------------------------------

    @Test
    public void checkCompareTo_shouldReturnCorrectOrder() {
        // Sensitive
        assertTrue(IOCase.SENSITIVE.checkCompareTo(FOO_LOWER, FOO_UPPER) > 0);
        assertEquals(0, IOCase.SENSITIVE.checkCompareTo(FOO_LOWER, FOO_LOWER));

        // Insensitive
        assertEquals(0, IOCase.INSENSITIVE.checkCompareTo(FOO_LOWER, FOO_UPPER));
        assertTrue(IOCase.INSENSITIVE.checkCompareTo(FOO_LOWER, "bar") > 0);
    }

    @Test(expected = NullPointerException.class)
    public void checkCompareTo_shouldThrowExceptionForNullInput() {
        IOCase.SENSITIVE.checkCompareTo(null, FOO_LOWER);
    }

    //-----------------------------------------------------------------------
    // checkStartsWith() tests
    //-----------------------------------------------------------------------

    @Test
    public void checkStartsWith_shouldReturnCorrectResult() {
        // Sensitive
        assertTrue(IOCase.SENSITIVE.checkStartsWith(FOOBAR_MIXED, "Foo"));
        assertFalse(IOCase.SENSITIVE.checkStartsWith(FOOBAR_MIXED, "foo"));

        // Insensitive
        assertTrue(IOCase.INSENSITIVE.checkStartsWith(FOOBAR_MIXED, "Foo"));
        assertTrue(IOCase.INSENSITIVE.checkStartsWith(FOOBAR_MIXED, "foo"));
        assertFalse(IOCase.INSENSITIVE.checkStartsWith(FOOBAR_MIXED, "bar"));
    }

    @Test
    public void checkStartsWith_shouldBeNullSafe() {
        assertFalse(IOCase.SENSITIVE.checkStartsWith(null, FOO_LOWER));
        assertFalse(IOCase.SENSITIVE.checkStartsWith(FOO_LOWER, null));
        assertFalse(IOCase.SENSITIVE.checkStartsWith(null, null));
    }

    //-----------------------------------------------------------------------
    // checkEndsWith() tests
    //-----------------------------------------------------------------------

    @Test
    public void checkEndsWith_shouldReturnCorrectResult() {
        // Sensitive
        assertTrue(IOCase.SENSITIVE.checkEndsWith(FOOBAR_MIXED, "Bar"));
        assertFalse(IOCase.SENSITIVE.checkEndsWith(FOOBAR_MIXED, "bar"));

        // Insensitive
        assertTrue(IOCase.INSENSITIVE.checkEndsWith(FOOBAR_MIXED, "Bar"));
        assertTrue(IOCase.INSENSITIVE.checkEndsWith(FOOBAR_MIXED, "bar"));
        assertFalse(IOCase.INSENSITIVE.checkEndsWith(FOOBAR_MIXED, "Foo"));
    }

    @Test
    public void checkEndsWith_shouldBeNullSafe() {
        assertFalse(IOCase.SENSITIVE.checkEndsWith(null, FOO_LOWER));
        assertFalse(IOCase.SENSITIVE.checkEndsWith(FOO_LOWER, null));
        assertFalse(IOCase.SENSITIVE.checkEndsWith(null, null));
    }

    //-----------------------------------------------------------------------
    // checkIndexOf() tests
    //-----------------------------------------------------------------------

    @Test
    public void checkIndexOf_shouldReturnCorrectIndex() {
        // Sensitive
        assertEquals(3, IOCase.SENSITIVE.checkIndexOf(FOOBAR_MIXED, 0, "Bar"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(FOOBAR_MIXED, 0, "bar"));

        // Insensitive
        assertEquals(3, IOCase.INSENSITIVE.checkIndexOf(FOOBAR_MIXED, 0, "Bar"));
        assertEquals(3, IOCase.INSENSITIVE.checkIndexOf(FOOBAR_MIXED, 0, "bar"));
        assertEquals(-1, IOCase.INSENSITIVE.checkIndexOf(FOOBAR_MIXED, 0, "baz"));
    }

    @Test
    public void checkIndexOf_shouldRespectStartIndex() {
        assertEquals(6, IOCase.SENSITIVE.checkIndexOf("FooBarFoo", 1, "Foo"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("FooBarFoo", 7, "Foo"));
    }

    @Test
    public void checkIndexOf_shouldBeNullSafe() {
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf(null, 0, "a"));
        assertEquals(-1, IOCase.SENSITIVE.checkIndexOf("a", 0, null));
    }

    //-----------------------------------------------------------------------
    // checkRegionMatches() tests
    //-----------------------------------------------------------------------

    @Test
    public void checkRegionMatches_shouldReturnCorrectResult() {
        // Sensitive
        assertTrue(IOCase.SENSITIVE.checkRegionMatches(FOOBAR_MIXED, 3, "Bar"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(FOOBAR_MIXED, 3, "bar"));

        // Insensitive
        assertTrue(IOCase.INSENSITIVE.checkRegionMatches(FOOBAR_MIXED, 3, "Bar"));
        assertTrue(IOCase.INSENSITIVE.checkRegionMatches(FOOBAR_MIXED, 3, "bar"));
        assertFalse(IOCase.INSENSITIVE.checkRegionMatches(FOOBAR_MIXED, 3, "baz"));
    }

    @Test
    public void checkRegionMatches_shouldHandleInvalidRegion() {
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(FOO_LOWER, -1, "f"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(FOO_LOWER, 20, "f"));
        assertFalse(IOCase.SENSITIVE.checkRegionMatches(FOO_LOWER, 0, "food"));
    }
}