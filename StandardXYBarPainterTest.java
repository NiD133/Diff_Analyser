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
 * -----------------------------
 * StandardXYBarPainterTest.java
 * -----------------------------
 * (C) Copyright 2008-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.renderer.xy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link StandardXYBarPainter} class.
 * 
 * This test suite verifies the core object behavior of StandardXYBarPainter,
 * including equality, hashing, cloning restrictions, and serialization.
 */
public class StandardXYBarPainterTest {

    // Test Data Factory Methods
    
    /**
     * Creates a standard StandardXYBarPainter instance for testing.
     * 
     * @return a new StandardXYBarPainter instance
     */
    private StandardXYBarPainter createStandardPainter() {
        return new StandardXYBarPainter();
    }

    // Equality Contract Tests
    
    /**
     * Verifies that two StandardXYBarPainter instances are equal.
     * 
     * Since StandardXYBarPainter has no configurable fields, all instances
     * should be considered equal to each other.
     */
    @Test
    public void testEquals_TwoInstancesAreEqual() {
        // Given: Two StandardXYBarPainter instances
        StandardXYBarPainter firstPainter = createStandardPainter();
        StandardXYBarPainter secondPainter = createStandardPainter();
        
        // When & Then: They should be equal
        assertEquals(firstPainter, secondPainter, 
            "Two StandardXYBarPainter instances should be equal");
    }

    /**
     * Verifies that equal StandardXYBarPainter instances have the same hash code.
     * 
     * This test ensures compliance with the Java equals/hashCode contract:
     * if two objects are equal, they must have the same hash code.
     */
    @Test
    public void testHashCode_EqualInstancesHaveSameHashCode() {
        // Given: Two equal StandardXYBarPainter instances
        StandardXYBarPainter firstPainter = createStandardPainter();
        StandardXYBarPainter secondPainter = createStandardPainter();
        assertEquals(firstPainter, secondPainter, "Precondition: instances should be equal");
        
        // When: Getting hash codes from both instances
        int firstHashCode = firstPainter.hashCode();
        int secondHashCode = secondPainter.hashCode();
        
        // Then: Hash codes should be identical
        assertEquals(firstHashCode, secondHashCode, 
            "Equal StandardXYBarPainter instances must have identical hash codes");
    }

    // Cloning Behavior Tests
    
    /**
     * Verifies that StandardXYBarPainter does not implement cloning interfaces.
     * 
     * StandardXYBarPainter instances are immutable, so cloning is not necessary
     * or supported. This test confirms that the class correctly does not
     * implement Cloneable or PublicCloneable interfaces.
     */
    @Test
    public void testCloning_DoesNotSupportCloning() {
        // Given: A StandardXYBarPainter instance
        StandardXYBarPainter painter = createStandardPainter();
        
        // When & Then: It should not implement cloning interfaces
        assertFalse(painter instanceof Cloneable, 
            "StandardXYBarPainter should not implement Cloneable (immutable objects don't need cloning)");
        assertFalse(painter instanceof PublicCloneable, 
            "StandardXYBarPainter should not implement PublicCloneable (immutable objects don't need cloning)");
    }

    // Serialization Tests
    
    /**
     * Verifies that StandardXYBarPainter can be serialized and deserialized correctly.
     * 
     * This test ensures that:
     * 1. The object can be serialized without errors
     * 2. The deserialized object is equal to the original
     * 3. Serialization preserves object state
     */
    @Test
    public void testSerialization_PreservesEqualityAfterSerialization() {
        // Given: A StandardXYBarPainter instance
        StandardXYBarPainter originalPainter = createStandardPainter();
        
        // When: Serializing and deserializing the instance
        StandardXYBarPainter deserializedPainter = TestUtils.serialised(originalPainter);
        
        // Then: The deserialized instance should equal the original
        assertEquals(originalPainter, deserializedPainter, 
            "Deserialized StandardXYBarPainter should be equal to the original instance");
    }
}