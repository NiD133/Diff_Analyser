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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DeviationRenderer} class, focusing on object contracts
 * like equals, hashCode, cloning, and serialization.
 */
public class DeviationRendererTest {

    @Test
    @DisplayName("The equals method should correctly compare renderer instances")
    void equals_shouldCorrectlyCompareInstances() {
        // Arrange: Create two default renderers
        DeviationRenderer renderer1 = new DeviationRenderer();
        DeviationRenderer renderer2 = new DeviationRenderer();

        // Assert: Default instances should be equal
        assertEquals(renderer1, renderer2);

        // Act: Change a property on one renderer
        renderer1.setAlpha(0.1f);

        // Assert: Instances should now be unequal
        assertNotEquals(renderer1, renderer2);

        // Act: Make the properties equal again
        renderer2.setAlpha(0.1f);

        // Assert: Instances should be equal again
        assertEquals(renderer1, renderer2);
    }

    @Test
    @DisplayName("The hash code should be consistent with the equals method")
    void hashCode_shouldBeConsistentWithEquals() {
        // Arrange
        DeviationRenderer renderer1 = new DeviationRenderer();
        DeviationRenderer renderer2 = new DeviationRenderer();
        assertEquals(renderer1, renderer2, "Pre-condition: renderers must be equal for this test.");

        // Act & Assert
        assertEquals(renderer1.hashCode(), renderer2.hashCode());
    }

    @Test
    @DisplayName("Cloning should produce an independent copy")
    void clone_shouldProduceIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        DeviationRenderer original = new DeviationRenderer();

        // Act
        DeviationRenderer clone = (DeviationRenderer) original.clone();

        // Assert: The clone is a different object but is equal in value
        assertNotSame(original, clone);
        assertEquals(original, clone);

        // Assert: Modifying the clone does not affect the original
        clone.setAlpha(0.9f);
        assertNotEquals(original.getAlpha(), clone.getAlpha());
    }

    @Test
    @DisplayName("The renderer should be publicly cloneable")
    void isPublicCloneable() {
        // Arrange
        DeviationRenderer renderer = new DeviationRenderer();

        // Act & Assert
        assertInstanceOf(PublicCloneable.class, renderer, "DeviationRenderer must implement PublicCloneable.");
    }

    @Test
    @DisplayName("Serialization and deserialization should preserve object state")
    void serialization_shouldPreserveState() {
        // Arrange
        DeviationRenderer original = new DeviationRenderer();
        original.setAlpha(0.75f); // Use a non-default value for a more robust test

        // Act
        DeviationRenderer deserialized = TestUtils.serialised(original);

        // Assert
        assertEquals(original, deserialized, "Deserialized object should be equal to the original.");
    }
}