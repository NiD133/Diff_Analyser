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
 * -------------------
 * ModuloAxisTest.java
 * -------------------
 * (C) Copyright 2007-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ModuloAxis} class.
 */
public class ModuloAxisTest {

    // Constants for test values to improve readability
    private static final String AXIS_LABEL = "Test";
    private static final Range FIXED_RANGE = new Range(0.0, 1.0);
    private static final double DISPLAY_START = 0.1;
    private static final double DISPLAY_END = 1.1;

    /**
     * Tests that cloning creates a distinct copy of the original axis.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        ModuloAxis original = new ModuloAxis(AXIS_LABEL, FIXED_RANGE);
        ModuloAxis clone = (ModuloAxis) original.clone();

        // Verify clone is a separate instance with identical properties
        assertNotSame(original, clone, "Cloned instance should be a different object");
        assertSame(original.getClass(), clone.getClass(), "Cloned instance should be the same class");
        assertEquals(original, clone, "Cloned instance should be equal to the original");
    }

    /**
     * Tests that two axes with identical configurations are equal.
     */
    @Test
    public void testEquals_IdenticalConfiguration() {
        ModuloAxis axis1 = new ModuloAxis(AXIS_LABEL, FIXED_RANGE);
        ModuloAxis axis2 = new ModuloAxis(AXIS_LABEL, FIXED_RANGE);
        assertEquals(axis1, axis2, "Axes with identical configurations should be equal");
    }

    /**
     * Tests that axes become unequal after changing the display range of one axis.
     */
    @Test
    public void testEquals_AfterChangingDisplayRange() {
        ModuloAxis axis1 = new ModuloAxis(AXIS_LABEL, FIXED_RANGE);
        ModuloAxis axis2 = new ModuloAxis(AXIS_LABEL, FIXED_RANGE);
        
        // Change display range of first axis and verify inequality
        axis1.setDisplayRange(DISPLAY_START, DISPLAY_END);
        assertNotEquals(axis1, axis2, "Axes should differ after display range change");
        
        // Synchronize display ranges and verify equality is restored
        axis2.setDisplayRange(DISPLAY_START, DISPLAY_END);
        assertEquals(axis1, axis2, "Axes should be equal after same display range update");
    }

    /**
     * Tests that equal axes produce identical hash codes.
     */
    @Test
    public void testHashCode_ForEqualObjects() {
        ModuloAxis axis1 = new ModuloAxis(AXIS_LABEL, FIXED_RANGE);
        ModuloAxis axis2 = new ModuloAxis(AXIS_LABEL, FIXED_RANGE);
        
        assertEquals(axis1, axis2, "Axes must be equal for hash code comparison");
        assertEquals(axis1.hashCode(), axis2.hashCode(), "Equal axes must have identical hash codes");
    }

    /**
     * Tests that serialization preserves axis properties.
     */
    @Test
    public void testSerialization() {
        ModuloAxis original = new ModuloAxis(AXIS_LABEL, FIXED_RANGE);
        ModuloAxis deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized axis should match original");
    }

}