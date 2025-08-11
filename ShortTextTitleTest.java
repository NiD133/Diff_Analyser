/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.]
 *
 * -----------------------
 * ShortTextTitleTest.java
 * -----------------------
 * Tests for the ShortTextTitle class.
 */

package org.jfree.chart.title;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ShortTextTitle}.
 *
 * These tests focus on:
 * - equals() contract (reflexive, symmetric, null-safety, and field sensitivity)
 * - hashCode() consistency with equals()
 * - clone() behavior (deep copy vs. same instance)
 * - serialization round-trip equivalence
 */
public class ShortTextTitleTest {

    private static final String DEFAULT_TEXT = "ABC";

    private static ShortTextTitle title(String text) {
        return new ShortTextTitle(text);
    }

    @Test
    @DisplayName("equals(): reflexive, symmetric, null-safe")
    public void equals_contractBasics() {
        ShortTextTitle t1 = title(DEFAULT_TEXT);
        ShortTextTitle t2 = title(DEFAULT_TEXT);

        // reflexive
        assertEquals(t1, t1);

        // symmetric
        assertEquals(t1, t2);
        assertEquals(t2, t1);

        // null-safe and type-safe
        assertNotEquals(t1, null);
        assertNotEquals(t1, new Object());
    }

    @Test
    @DisplayName("equals(): detects differences in text and matches after alignment")
    public void equals_fieldSensitivity() {
        ShortTextTitle t1 = title(DEFAULT_TEXT);
        ShortTextTitle t2 = title(DEFAULT_TEXT);
        assertEquals(t1, t2);

        // change state in one instance -> not equal
        t1.setText("Test 1");
        assertNotEquals(t1, t2);

        // align state -> equal again
        t2.setText("Test 1");
        assertEquals(t1, t2);
    }

    @Test
    @DisplayName("hashCode(): equal objects must have equal hash codes")
    public void hashCode_consistentWithEquals() {
        ShortTextTitle t1 = title(DEFAULT_TEXT);
        ShortTextTitle t2 = title(DEFAULT_TEXT);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    @DisplayName("clone(): cloned instance is equal but not the same reference")
    public void clone_createsEqualButDistinctCopy() throws CloneNotSupportedException {
        ShortTextTitle original = title(DEFAULT_TEXT);
        ShortTextTitle clone = CloneUtils.clone(original);

        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);
    }

    @Test
    @DisplayName("serialization: round-trip preserves equality")
    public void serialization_roundTripPreservesEquality() {
        ShortTextTitle original = title(DEFAULT_TEXT);
        ShortTextTitle restored = TestUtils.serialised(original);

        assertEquals(original, restored);
    }
}