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
 * Test suite for the {@link SymbolicXYItemLabelGenerator} class.
 * 
 * This test suite verifies the core object behavior including equality,
 * hashing, cloning, and serialization functionality.
 */
public class SymbolicXYItemLabelGeneratorTest {

    // Test Data Setup Methods
    
    /**
     * Creates a default SymbolicXYItemLabelGenerator instance for testing.
     */
    private SymbolicXYItemLabelGenerator createDefaultGenerator() {
        return new SymbolicXYItemLabelGenerator();
    }

    // Equality and Hash Code Tests
    
    /**
     * Verifies that two SymbolicXYItemLabelGenerator instances with default 
     * configuration are equal to each other.
     * 
     * Tests both directions of equality (symmetric property).
     */
    @Test
    public void testEquals_DefaultInstances_ShouldBeEqual() {
        // Given: Two default generator instances
        SymbolicXYItemLabelGenerator firstGenerator = createDefaultGenerator();
        SymbolicXYItemLabelGenerator secondGenerator = createDefaultGenerator();
        
        // Then: They should be equal in both directions
        assertEquals(firstGenerator, secondGenerator, 
            "Two default generators should be equal");
        assertEquals(secondGenerator, firstGenerator, 
            "Equality should be symmetric");
    }

    /**
     * Verifies that equal SymbolicXYItemLabelGenerator instances produce 
     * the same hash code.
     * 
     * This is required by the general contract of hashCode().
     */
    @Test
    public void testHashCode_EqualInstances_ShouldHaveSameHashCode() {
        // Given: Two equal generator instances
        SymbolicXYItemLabelGenerator firstGenerator = createDefaultGenerator();
        SymbolicXYItemLabelGenerator secondGenerator = createDefaultGenerator();
        
        // When: Generators are equal
        assertEquals(firstGenerator, secondGenerator, 
            "Generators should be equal for this test");
        
        // Then: Their hash codes should be equal
        assertEquals(firstGenerator.hashCode(), secondGenerator.hashCode(),
            "Equal objects must have equal hash codes");
    }

    // Cloning Tests
    
    /**
     * Verifies that SymbolicXYItemLabelGenerator can be successfully cloned
     * and that the clone is a separate instance with the same content.
     */
    @Test
    public void testCloning_DefaultInstance_ShouldCreateEqualButSeparateInstance() 
            throws CloneNotSupportedException {
        // Given: A generator instance
        SymbolicXYItemLabelGenerator originalGenerator = createDefaultGenerator();
        
        // When: Cloning the generator
        SymbolicXYItemLabelGenerator clonedGenerator = CloneUtils.clone(originalGenerator);
        
        // Then: Clone should be a separate but equal instance
        assertNotSame(originalGenerator, clonedGenerator,
            "Clone should be a different object instance");
        assertSame(originalGenerator.getClass(), clonedGenerator.getClass(),
            "Clone should be of the same class");
        assertEquals(originalGenerator, clonedGenerator,
            "Clone should be equal to the original");
    }

    /**
     * Verifies that SymbolicXYItemLabelGenerator implements the PublicCloneable 
     * interface, indicating it supports public cloning.
     */
    @Test
    public void testPublicCloneable_DefaultInstance_ShouldImplementInterface() {
        // Given: A generator instance
        SymbolicXYItemLabelGenerator generator = createDefaultGenerator();
        
        // Then: It should implement PublicCloneable
        assertTrue(generator instanceof PublicCloneable,
            "SymbolicXYItemLabelGenerator should implement PublicCloneable interface");
    }

    // Serialization Tests
    
    /**
     * Verifies that SymbolicXYItemLabelGenerator can be serialized and 
     * deserialized successfully, with the deserialized instance being 
     * equal to the original.
     */
    @Test
    public void testSerialization_DefaultInstance_ShouldPreserveEquality() {
        // Given: A generator instance
        SymbolicXYItemLabelGenerator originalGenerator = createDefaultGenerator();
        
        // When: Serializing and deserializing the generator
        SymbolicXYItemLabelGenerator deserializedGenerator = 
            TestUtils.serialised(originalGenerator);
        
        // Then: Deserialized instance should equal the original
        assertEquals(originalGenerator, deserializedGenerator,
            "Deserialized generator should be equal to the original");
    }
}