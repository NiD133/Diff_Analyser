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
 * --------------------------
 * DeviationRendererTest.java
 * --------------------------
 * (C) Copyright 2007-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DeviationRenderer} class.
 * 
 * This test suite verifies the core functionality of DeviationRenderer including:
 * - Object equality and hash code contracts
 * - Cloning behavior and interface compliance
 * - Serialization support
 */
public class DeviationRendererTest {

    // Test constants
    private static final float CUSTOM_ALPHA_VALUE = 0.1f;
    private static final float DEFAULT_ALPHA_VALUE = 0.5f;

    /**
     * Tests that the equals() method correctly distinguishes between different renderer configurations.
     * 
     * Verifies:
     * - Default instances are equal
     * - Equality is symmetric (r1.equals(r2) == r2.equals(r1))
     * - Alpha property changes affect equality
     * - Renderers with same alpha values are equal
     */
    @Test
    public void testEquals() {
        // Given: Two default DeviationRenderer instances
        DeviationRenderer defaultRenderer1 = new DeviationRenderer();
        DeviationRenderer defaultRenderer2 = new DeviationRenderer();
        
        // Then: Default instances should be equal (both directions)
        assertEquals(defaultRenderer1, defaultRenderer2, 
            "Default DeviationRenderer instances should be equal");
        assertEquals(defaultRenderer2, defaultRenderer1, 
            "Equality should be symmetric");

        // When: First renderer's alpha is changed
        defaultRenderer1.setAlpha(CUSTOM_ALPHA_VALUE);
        
        // Then: Renderers should no longer be equal
        assertNotEquals(defaultRenderer1, defaultRenderer2, 
            "Renderers with different alpha values should not be equal");
        
        // When: Second renderer's alpha is set to the same value
        defaultRenderer2.setAlpha(CUSTOM_ALPHA_VALUE);
        
        // Then: Renderers should be equal again
        assertEquals(defaultRenderer1, defaultRenderer2, 
            "Renderers with same alpha values should be equal");
    }

    /**
     * Tests that the hashCode() method satisfies the contract: 
     * equal objects must return the same hash code.
     * 
     * This is critical for proper behavior when DeviationRenderer objects
     * are used in hash-based collections like HashMap or HashSet.
     */
    @Test
    public void testHashCodeContract() {
        // Given: Two equal DeviationRenderer instances
        DeviationRenderer renderer1 = new DeviationRenderer();
        DeviationRenderer renderer2 = new DeviationRenderer();
        
        // Verify they are equal
        assertEquals(renderer1, renderer2, 
            "Renderers should be equal for hash code test");
        
        // When: Getting hash codes
        int hashCode1 = renderer1.hashCode();
        int hashCode2 = renderer2.hashCode();
        
        // Then: Hash codes must be equal
        assertEquals(hashCode1, hashCode2, 
            "Equal objects must have equal hash codes");
    }

    /**
     * Tests that cloning creates a proper deep copy of the DeviationRenderer.
     * 
     * Verifies:
     * - Clone is a different object instance (not same reference)
     * - Clone has the same class type
     * - Clone is logically equal to the original
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Given: A DeviationRenderer instance
        DeviationRenderer originalRenderer = new DeviationRenderer();
        
        // When: Cloning the renderer
        DeviationRenderer clonedRenderer = (DeviationRenderer) originalRenderer.clone();
        
        // Then: Verify cloning behavior
        assertNotSame(originalRenderer, clonedRenderer, 
            "Clone should be a different object instance");
        assertSame(originalRenderer.getClass(), clonedRenderer.getClass(), 
            "Clone should have the same class as original");
        assertEquals(originalRenderer, clonedRenderer, 
            "Clone should be logically equal to original");
    }

    /**
     * Tests that DeviationRenderer implements the PublicCloneable interface.
     * 
     * This interface indicates that the class supports public cloning,
     * which is important for framework integration and user code that
     * needs to clone renderer instances.
     */
    @Test
    public void testImplementsPublicCloneableInterface() {
        // Given: A DeviationRenderer instance
        DeviationRenderer renderer = new DeviationRenderer();
        
        // Then: It should implement PublicCloneable
        assertTrue(renderer instanceof PublicCloneable, 
            "DeviationRenderer should implement PublicCloneable interface");
    }

    /**
     * Tests that DeviationRenderer can be serialized and deserialized correctly.
     * 
     * This is important for:
     * - Saving/loading chart configurations
     * - Network transmission of chart objects
     * - Caching mechanisms
     * 
     * The deserialized object should be logically equal to the original.
     */
    @Test
    public void testSerialization() {
        // Given: A DeviationRenderer instance
        DeviationRenderer originalRenderer = new DeviationRenderer();
        
        // When: Serializing and deserializing
        DeviationRenderer deserializedRenderer = TestUtils.serialised(originalRenderer);
        
        // Then: Deserialized object should equal the original
        assertEquals(originalRenderer, deserializedRenderer, 
            "Deserialized renderer should equal the original");
    }
}