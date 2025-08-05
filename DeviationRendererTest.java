/* 
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 * Project Info: https://www.jfree.org/jfreechart/index.html
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
 * --------------------------
 * DeviationRendererTest.java
 * --------------------------
 * (C) Copyright 2007-present, by David Gilbert and Contributors.
 * Original Author: David Gilbert;
 */

package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DeviationRenderer} class.
 */
public class DeviationRendererTest {

    // ========================================================================
    // Equality Tests
    // ========================================================================

    /**
     * Tests that two newly created instances of DeviationRenderer are equal.
     */
    @Test
    public void equals_NewInstances_ShouldBeEqual() {
        DeviationRenderer r1 = new DeviationRenderer();
        DeviationRenderer r2 = new DeviationRenderer();
        assertEquals(r1, r2, "Default instances should be equal");
    }

    /**
     * Tests that instances with the same alpha value are equal.
     */
    @Test
    public void equals_SameAlpha_ShouldBeEqual() {
        DeviationRenderer r1 = new DeviationRenderer();
        r1.setAlpha(0.1f);
        DeviationRenderer r2 = new DeviationRenderer();
        r2.setAlpha(0.1f);
        assertEquals(r1, r2, "Instances with same alpha should be equal");
    }

    /**
     * Tests that instances with different alpha values are not equal.
     */
    @Test
    public void equals_DifferentAlpha_ShouldNotBeEqual() {
        DeviationRenderer r1 = new DeviationRenderer();
        r1.setAlpha(0.1f);
        DeviationRenderer r2 = new DeviationRenderer();
        assertNotEquals(r1, r2, "Instances with different alpha should not be equal");
    }

    // ========================================================================
    // HashCode Tests
    // ========================================================================

    /**
     * Tests that equal instances produce the same hash code.
     */
    @Test
    public void hashCode_EqualInstances_ShouldHaveSameHashCode() {
        DeviationRenderer r1 = new DeviationRenderer();
        DeviationRenderer r2 = new DeviationRenderer();
        assertEquals(r1, r2, "Instances must be equal for hash code test");
        assertEquals(r1.hashCode(), r2.hashCode(), "Equal instances must have same hash code");
    }

    // ========================================================================
    // Cloning Tests
    // ========================================================================

    /**
     * Tests that cloning creates a distinct copy with identical content.
     */
    @Test
    public void cloning_ShouldCreateDistinctButEqualCopy() throws CloneNotSupportedException {
        DeviationRenderer original = new DeviationRenderer();
        DeviationRenderer clone = (DeviationRenderer) original.clone();
        
        assertNotSame(original, clone, "Cloned instance should be a different object");
        assertSame(original.getClass(), clone.getClass(), "Cloned instance should be same class");
        assertEquals(original, clone, "Cloned instance should be equal to original");
    }

    // ========================================================================
    // PublicCloneable Interface Tests
    // ========================================================================

    /**
     * Tests that DeviationRenderer implements the PublicCloneable interface.
     */
    @Test
    public void publicCloneable_ShouldImplementInterface() {
        DeviationRenderer renderer = new DeviationRenderer();
        assertTrue(renderer instanceof PublicCloneable, "Must implement PublicCloneable");
    }

    // ========================================================================
    // Serialization Tests
    // ========================================================================

    /**
     * Tests that serialization preserves object equality.
     */
    @Test
    public void serialization_ShouldPreserveEquality() {
        DeviationRenderer original = new DeviationRenderer();
        DeviationRenderer deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized instance should equal original");
    }
}