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
 * (C) Copyright 2008-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.title;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ShortTextTitle} class.
 */
class ShortTextTitleTest {

    /**
     * Verifies that titles with the same text are considered equal.
     */
    @Test
    void equals_ShouldReturnTrueForSameText() {
        ShortTextTitle title1 = new ShortTextTitle("ABC");
        ShortTextTitle title2 = new ShortTextTitle("ABC");
        assertEquals(title1, title2, "Titles with same text should be equal");
    }

    /**
     * Verifies that titles become unequal when one title's text changes.
     */
    @Test
    void equals_ShouldReturnFalseWhenTextDiffers() {
        ShortTextTitle title1 = new ShortTextTitle("ABC");
        ShortTextTitle title2 = new ShortTextTitle("ABC");
        title1.setText("Test 1");
        assertNotEquals(title1, title2, "Titles should not be equal after changing one title's text");
    }

    /**
     * Verifies that titles become equal again when both have the same updated text.
     */
    @Test
    void equals_ShouldReturnTrueAfterReconcilingText() {
        ShortTextTitle title1 = new ShortTextTitle("ABC");
        ShortTextTitle title2 = new ShortTextTitle("ABC");
        title1.setText("Test 1");
        title2.setText("Test 1");
        assertEquals(title1, title2, "Titles should be equal after setting same text");
    }

    /**
     * Verifies that equal titles produce identical hash codes.
     */
    @Test
    void hashCode_ShouldBeConsistentWithEquals() {
        ShortTextTitle title1 = new ShortTextTitle("ABC");
        ShortTextTitle title2 = new ShortTextTitle("ABC");
        assertEquals(title1, title2, "Titles must be equal for hashcode comparison");
        assertEquals(title1.hashCode(), title2.hashCode(), "Equal titles must have same hashcode");
    }

    /**
     * Verifies that cloning creates a distinct copy with same content.
     */
    @Test
    void cloning_ShouldCreateEqualButDistinctInstance() throws CloneNotSupportedException {
        ShortTextTitle original = new ShortTextTitle("ABC");
        ShortTextTitle clone = CloneUtils.clone(original);
        
        assertNotSame(original, clone, "Clone should be a different object instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should have same runtime class");
        assertEquals(original, clone, "Clone should be logically equal to original");
    }

    /**
     * Verifies serialization and deserialization produce equal titles.
     */
    @Test
    void serialization_ShouldRoundTripCorrectly() {
        ShortTextTitle original = new ShortTextTitle("ABC");
        ShortTextTitle deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized title should equal original");
    }

}