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
import org.jfree.chart.api.PublicCloneable;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link SymbolicXYItemLabelGenerator} class, focusing on its
 * object contract methods like equals, hashCode, cloning, and serialization.
 *
 * Note: This test suite does not cover the primary functionality of label
 * generation, as the class under test has no state to configure. A complete
 * test suite would require tests for the generateLabel() and generateToolTip()
 * methods with various XYDataset inputs.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies the equals() and hashCode() contract for two default instances.
     * Since SymbolicXYItemLabelGenerator is stateless, any two instances created
     * with the default constructor should be equal.
     */
    @Test
    public void equalsAndHashCode_shouldAdhereToContract() {
        // Arrange
        SymbolicXYItemLabelGenerator generator1 = new SymbolicXYItemLabelGenerator();
        SymbolicXYItemLabelGenerator generator2 = new SymbolicXYItemLabelGenerator();

        // Assert
        // Reflexive: an object must equal itself
        assertEquals(generator1, generator1);

        // Symmetric: if x.equals(y) is true, then y.equals(x) must be true
        assertEquals(generator1, generator2);
        assertEquals(generator2, generator1);

        // Not equal to null
        assertNotEquals(null, generator1);

        // Not equal to an object of a different type
        assertNotEquals(generator1, new Object());

        // Hash code: equal objects must have equal hash codes
        assertEquals(generator1.hashCode(), generator2.hashCode());
    }

    /**
     * Verifies that cloning creates a new, independent instance that is
     * logically equal to the original.
     */
    @Test
    public void clone_shouldReturnIndependentAndEqualCopy() throws CloneNotSupportedException {
        // Arrange
        SymbolicXYItemLabelGenerator original = new SymbolicXYItemLabelGenerator();

        // Act
        SymbolicXYItemLabelGenerator clone = CloneUtils.clone(original);

        // Assert
        assertNotSame(original, clone, "A clone must be a different object in memory.");
        assertEquals(original, clone, "A clone must be logically equal to the original.");
    }

    /**
     * Confirms that the class implements the PublicCloneable interface,
     * signaling that its clone() method is intended for public use.
     */
    @Test
    public void class_shouldImplementPublicCloneable() {
        // Arrange
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();

        // Assert
        assertTrue(generator instanceof PublicCloneable, "The class should be publicly cloneable.");
    }

    /**
     * Verifies that an instance can be serialized and deserialized, and that
     * the resulting object is equal to the original.
     */
    @Test
    public void serialization_shouldPreserveEquality() {
        // Arrange
        SymbolicXYItemLabelGenerator original = new SymbolicXYItemLabelGenerator();

        // Act
        SymbolicXYItemLabelGenerator deserialized = TestUtils.serialised(original);

        // Assert
        assertEquals(original, deserialized, "Deserialized object should be equal to the original.");
    }
}