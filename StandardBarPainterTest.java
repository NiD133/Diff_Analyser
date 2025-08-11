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
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.renderer.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link StandardBarPainter} class.
 * 
 * This test suite verifies the core object behavior of StandardBarPainter,
 * including equality, hashing, cloning restrictions, and serialization.
 */
public class StandardBarPainterTest {

    // Test Data Factory Methods
    
    /**
     * Creates a new StandardBarPainter instance for testing.
     * 
     * @return a new StandardBarPainter instance
     */
    private StandardBarPainter createStandardBarPainter() {
        return new StandardBarPainter();
    }

    // Equality Contract Tests

    /**
     * Verifies that two StandardBarPainter instances are equal.
     * 
     * Since StandardBarPainter has no configurable fields, all instances
     * should be considered equal to each other.
     */
    @Test
    public void testEquals_TwoInstancesAreEqual() {
        // Given
        StandardBarPainter firstPainter = createStandardBarPainter();
        StandardBarPainter secondPainter = createStandardBarPainter();
        
        // When & Then
        assertEquals(firstPainter, secondPainter, 
            "Two StandardBarPainter instances should be equal");
    }

    /**
     * Verifies that equal StandardBarPainter instances have the same hash code.
     * 
     * This test ensures compliance with the Java hashCode contract:
     * if two objects are equal according to equals(), they must have the same hash code.
     */
    @Test
    public void testHashCode_EqualInstancesHaveSameHashCode() {
        // Given
        StandardBarPainter firstPainter = createStandardBarPainter();
        StandardBarPainter secondPainter = createStandardBarPainter();
        
        // When
        int firstHashCode = firstPainter.hashCode();
        int secondHashCode = secondPainter.hashCode();
        
        // Then
        assertEquals(firstPainter, secondPainter, 
            "Painters should be equal before comparing hash codes");
        assertEquals(firstHashCode, secondHashCode, 
            "Equal StandardBarPainter instances must have identical hash codes");
    }

    // Cloning Behavior Tests

    /**
     * Verifies that StandardBarPainter does not implement cloning interfaces.
     * 
     * StandardBarPainter instances are immutable, so cloning is not necessary
     * or supported. This test confirms that the class correctly does not
     * implement Cloneable or PublicCloneable interfaces.
     */
    @Test
    public void testCloning_NotSupported() {
        // Given
        StandardBarPainter painter = createStandardBarPainter();
        
        // When & Then
        assertFalse(painter instanceof Cloneable, 
            "StandardBarPainter should not implement Cloneable interface");
        assertFalse(painter instanceof PublicCloneable, 
            "StandardBarPainter should not implement PublicCloneable interface");
    }

    // Serialization Tests

    /**
     * Verifies that StandardBarPainter can be serialized and deserialized correctly.
     * 
     * After serialization and deserialization, the restored instance should
     * be equal to the original instance.
     */
    @Test
    public void testSerialization_PreservesEquality() {
        // Given
        StandardBarPainter originalPainter = createStandardBarPainter();
        
        // When
        StandardBarPainter deserializedPainter = TestUtils.serialised(originalPainter);
        
        // Then
        assertEquals(originalPainter, deserializedPainter, 
            "Deserialized StandardBarPainter should be equal to the original");
    }
}