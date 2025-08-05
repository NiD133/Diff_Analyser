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
 * -------------------------------------
 * SymbolicXYItemLabelGeneratorTest.java
 * -------------------------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.labels;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link SymbolicXYItemLabelGenerator} class.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * Tests that two distinct instances of the generator are equal.
     */
    @Test
    public void twoNewInstancesShouldBeEqual() {
        // Setup
        SymbolicXYItemLabelGenerator generator1 = new SymbolicXYItemLabelGenerator();
        SymbolicXYItemLabelGenerator generator2 = new SymbolicXYItemLabelGenerator();

        // Verify
        assertEquals(generator1, generator2, "Instances should be equal");
        assertEquals(generator2, generator1, "Equality should be symmetric");
    }

    /**
     * Tests that two equal instances have the same hash code.
     */
    @Test
    public void equalInstancesShouldHaveSameHashCode() {
        // Setup
        SymbolicXYItemLabelGenerator generator1 = new SymbolicXYItemLabelGenerator();
        SymbolicXYItemLabelGenerator generator2 = new SymbolicXYItemLabelGenerator();

        // Verify
        assertEquals(generator1, generator2, "Instances must be equal for hash code check");
        assertEquals(generator1.hashCode(), generator2.hashCode(), "Equal instances must have same hash code");
    }

    /**
     * Tests that cloning creates a distinct but equal instance.
     */
    @Test
    public void cloneShouldCreateEqualButDistinctInstance() throws CloneNotSupportedException {
        // Setup
        SymbolicXYItemLabelGenerator original = new SymbolicXYItemLabelGenerator();

        // Exercise
        SymbolicXYItemLabelGenerator clone = CloneUtils.clone(original);

        // Verify
        assertNotSame(original, clone, "Cloned instance should be a different object");
        assertEquals(original.getClass(), clone.getClass(), "Cloned instance should have the same class");
        assertEquals(original, clone, "Cloned instance should be equal to the original");
    }

    /**
     * Tests that the generator implements the PublicCloneable interface.
     */
    @Test
    public void shouldImplementPublicCloneable() {
        // Setup & Exercise
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();

        // Verify
        assertTrue(generator instanceof PublicCloneable, "Generator should implement PublicCloneable");
    }

    /**
     * Tests that serialization and deserialization produce an equal instance.
     */
    @Test
    public void shouldRoundTripThroughSerialization() {
        // Setup
        SymbolicXYItemLabelGenerator original = new SymbolicXYItemLabelGenerator();

        // Exercise
        SymbolicXYItemLabelGenerator deserialized = TestUtils.serialised(original);

        // Verify
        assertNotSame(original, deserialized, "Deserialized instance should be a new object");
        assertEquals(original, deserialized, "Deserialized instance should be equal to the original");
    }

}