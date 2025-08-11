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
 * ---------------------------
 * StandardBarPainterTest.java
 * ---------------------------
 * (C) Copyright 2008-present, by David Gilbert and Contributors.
 *
 */

package org.jfree.chart.renderer.category;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link StandardBarPainter} class, focusing on its contract
 * compliance for equals, hashCode, serialization, and cloning.
 */
class StandardBarPainterTest {

    /**
     * Tests the equals() method for the general contract. Since the class is
     * stateless, all instances should be equal. This test verifies:
     * 1. Reflexivity: an object equals itself.
     * 2. Symmetry: if a.equals(b) then b.equals(a).
     * 3. Inequality with null.
     * 4. Inequality with a different object type.
     */
    @Test
    @DisplayName("equals() should adhere to its contract")
    void equals_shouldAdhereToContract() {
        // Arrange
        StandardBarPainter painter1 = new StandardBarPainter();
        StandardBarPainter painter2 = new StandardBarPainter();

        // Assert: an object is equal to itself (reflexive)
        assertEquals(painter1, painter1);

        // Assert: two distinct instances are equal (symmetric)
        assertEquals(painter1, painter2);
        assertEquals(painter2, painter1);

        // Assert: an object is not equal to null
        assertNotEquals(null, painter1);

        // Assert: an object is not equal to an object of a different type
        assertNotEquals(new Object(), painter1);
    }

    /**
     * Verifies that two equal objects return the same hash code, fulfilling
     * the hashCode() contract.
     */
    @Test
    @DisplayName("hashCode() should be consistent for equal objects")
    void hashCode_shouldBeConsistentForEqualObjects() {
        // Arrange
        StandardBarPainter painter1 = new StandardBarPainter();
        StandardBarPainter painter2 = new StandardBarPainter();

        // Assert
        assertEquals(painter1, painter2, "Precondition: The two painters must be equal.");
        assertEquals(painter1.hashCode(), painter2.hashCode());
    }

    /**
     * Verifies that StandardBarPainter is not cloneable, which is the
     * expected behavior for an immutable class that does not need to be copied.
     */
    @Test
    @DisplayName("A StandardBarPainter instance should not be cloneable")
    void shouldNotBeCloneable() {
        // Arrange
        StandardBarPainter painter = new StandardBarPainter();

        // Assert
        assertFalse(painter instanceof Cloneable, "Should not implement Cloneable.");
        assertFalse(painter instanceof PublicCloneable, "Should not implement PublicCloneable.");
    }

    /**
     * Verifies that an instance can be serialized and deserialized, and that
     * the resulting object is equal to the original.
     */
    @Test
    @DisplayName("A StandardBarPainter instance should be serializable")
    void shouldBeSerializable() {
        // Arrange
        StandardBarPainter originalPainter = new StandardBarPainter();

        // Act
        StandardBarPainter deserializedPainter = TestUtils.serialised(originalPainter);

        // Assert
        assertEquals(originalPainter, deserializedPainter);
    }
}