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
 * Tests for the {@link ModuloAxis} class, focusing on its core object-oriented
 * contracts like cloning, equality, and serialization.
 */
public class ModuloAxisTest {

    /**
     * Verifies that cloning creates an independent but equivalent object.
     */
    @Test
    public void clone_shouldReturnIndependentButEqualInstance() throws CloneNotSupportedException {
        // Arrange
        ModuloAxis original = new ModuloAxis("Test", new Range(0.0, 1.0));

        // Act
        ModuloAxis clone = (ModuloAxis) original.clone();

        // Assert
        assertNotSame(original, clone, "Clone should be a different object instance.");
        assertEquals(original, clone, "Clone should be equal to the original object.");
    }

    /**
     * Confirms that two ModuloAxis instances with identical properties are considered equal.
     */
    @Test
    public void equals_shouldReturnTrueForIdenticalInstances() {
        // Arrange
        ModuloAxis axis1 = new ModuloAxis("Test", new Range(0.0, 1.0));
        ModuloAxis axis2 = new ModuloAxis("Test", new Range(0.0, 1.0));

        // Act & Assert
        assertEquals(axis1, axis2, "Axes with identical properties should be equal.");
    }

    /**
     * Ensures the equals method correctly handles changes in the displayRange property.
     */
    @Test
    public void equals_shouldBeSensitiveToDisplayRangeChanges() {
        // Arrange
        ModuloAxis axis1 = new ModuloAxis("Test", new Range(0.0, 1.0));
        ModuloAxis axis2 = new ModuloAxis("Test", new Range(0.0, 1.0));
        assertTrue(axis1.equals(axis2), "Axes should be equal initially.");

        // Act: Modify the display range of the first axis
        axis1.setDisplayRange(0.1, 1.1);

        // Assert: The axes should no longer be equal
        assertNotEquals(axis1, axis2, "Axes should not be equal after one's display range changes.");

        // Act: Modify the second axis to match the first
        axis2.setDisplayRange(0.1, 1.1);

        // Assert: The axes should be equal again
        assertEquals(axis1, axis2, "Axes should be equal again once their display ranges match.");
    }

    /**
     * Verifies that two equal objects produce the same hash code, fulfilling the
     * hashCode contract.
     */
    @Test
    public void hashCode_shouldBeConsistentWithEquals() {
        // Arrange
        ModuloAxis axis1 = new ModuloAxis("Test", new Range(0.0, 1.0));
        ModuloAxis axis2 = new ModuloAxis("Test", new Range(0.0, 1.0));
        assertEquals(axis1, axis2, "Precondition: The two axes must be equal.");

        // Act
        int hash1 = axis1.hashCode();
        int hash2 = axis2.hashCode();

        // Assert
        assertEquals(hash1, hash2, "Equal objects must have equal hash codes.");
    }

    /**
     * Checks that an instance can be serialized and then deserialized back into
     * an equivalent object.
     */
    @Test
    public void serialization_shouldPreserveObjectState() {
        // Arrange
        ModuloAxis original = new ModuloAxis("Test", new Range(0.0, 1.0));

        // Act
        ModuloAxis deserialized = TestUtils.serialised(original);

        // Assert
        assertEquals(original, deserialized, "Deserialized object should be equal to the original.");
    }
}