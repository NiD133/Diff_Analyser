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

    // Test data constants for better maintainability
    private static final String TEST_AXIS_LABEL = "Test";
    private static final Range STANDARD_FIXED_RANGE = new Range(0.0, 1.0);
    private static final double DISPLAY_START = 0.1;
    private static final double DISPLAY_END = 1.1;

    /**
     * Verifies that cloning creates a proper deep copy of ModuloAxis.
     * Tests that:
     * - Clone is a different object instance
     * - Clone has the same class type
     * - Clone is equal to the original
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Given: A ModuloAxis with standard configuration
        ModuloAxis originalAxis = createStandardModuloAxis();
        
        // When: Cloning the axis
        ModuloAxis clonedAxis = (ModuloAxis) originalAxis.clone();
        
        // Then: Clone should be a separate but equal instance
        assertNotSame(originalAxis, clonedAxis, 
            "Cloned axis should be a different object instance");
        assertSame(originalAxis.getClass(), clonedAxis.getClass(), 
            "Cloned axis should have the same class as original");
        assertEquals(originalAxis, clonedAxis, 
            "Cloned axis should be equal to the original");
    }

    /**
     * Verifies that the equals method correctly identifies equal and unequal ModuloAxis instances.
     * Tests equality based on:
     * - Initial configuration (label and fixed range)
     * - Display range settings
     */
    @Test
    public void testEquals() {
        // Given: Two ModuloAxis instances with identical initial configuration
        ModuloAxis firstAxis = createStandardModuloAxis();
        ModuloAxis secondAxis = createStandardModuloAxis();
        
        // Then: They should be equal initially
        assertEquals(firstAxis, secondAxis, 
            "Axes with identical configuration should be equal");

        // When: Changing display range on first axis only
        firstAxis.setDisplayRange(DISPLAY_START, DISPLAY_END);
        
        // Then: They should no longer be equal
        assertNotEquals(firstAxis, secondAxis, 
            "Axes with different display ranges should not be equal");
        
        // When: Setting the same display range on second axis
        secondAxis.setDisplayRange(DISPLAY_START, DISPLAY_END);
        
        // Then: They should be equal again
        assertEquals(firstAxis, secondAxis, 
            "Axes with matching display ranges should be equal");
    }

    /**
     * Verifies that equal ModuloAxis instances produce the same hash code.
     * This is required by the Java contract for hashCode() and equals().
     */
    @Test
    public void testHashCode() {
        // Given: Two equal ModuloAxis instances
        ModuloAxis firstAxis = createStandardModuloAxis();
        ModuloAxis secondAxis = createStandardModuloAxis();
        
        assertEquals(firstAxis, secondAxis, 
            "Precondition: axes should be equal for hash code test");
        
        // When: Getting hash codes
        int firstHashCode = firstAxis.hashCode();
        int secondHashCode = secondAxis.hashCode();
        
        // Then: Hash codes should be identical
        assertEquals(firstHashCode, secondHashCode, 
            "Equal objects must have equal hash codes");
    }

    /**
     * Verifies that ModuloAxis can be serialized and deserialized correctly.
     * Tests that the deserialized instance is equal to the original.
     */
    @Test
    public void testSerialization() {
        // Given: A ModuloAxis instance
        ModuloAxis originalAxis = createStandardModuloAxis();
        
        // When: Serializing and deserializing the axis
        ModuloAxis deserializedAxis = TestUtils.serialised(originalAxis);
        
        // Then: Deserialized axis should equal the original
        assertEquals(originalAxis, deserializedAxis, 
            "Deserialized axis should be equal to the original");
    }

    /**
     * Helper method to create a standard ModuloAxis for testing.
     * Centralizes test data creation for consistency and maintainability.
     * 
     * @return A new ModuloAxis with standard test configuration
     */
    private ModuloAxis createStandardModuloAxis() {
        return new ModuloAxis(TEST_AXIS_LABEL, STANDARD_FIXED_RANGE);
    }
}