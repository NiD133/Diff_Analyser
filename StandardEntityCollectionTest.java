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
 * ---------------------------------
 * StandardEntityCollectionTest.java
 * ---------------------------------
 * (C) Copyright 2004-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.entity;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    /**
     * Creates a standard chart entity for use in tests.
     * @return A new {@link PieSectionEntity} instance.
     */
    private PieSectionEntity<String> createTestEntity() {
        Rectangle2D area = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        return new PieSectionEntity<>(area, dataset, 0, 1, "Key", "ToolTip", "URL");
    }

    /**
     * Tests that two newly created, empty collections are considered equal.
     */
    @Test
    public void equals_withTwoEmptyCollections_shouldReturnTrue() {
        // Arrange
        StandardEntityCollection collection1 = new StandardEntityCollection();
        StandardEntityCollection collection2 = new StandardEntityCollection();

        // Act & Assert
        assertEquals(collection1, collection2);
    }

    /**
     * Tests that a collection with an entity is not equal to an empty collection.
     */
    @Test
    public void equals_withOneEmptyAndOneNonEmptyCollection_shouldReturnFalse() {
        // Arrange
        StandardEntityCollection collection1 = new StandardEntityCollection();
        collection1.add(createTestEntity());
        StandardEntityCollection collection2 = new StandardEntityCollection();

        // Act & Assert
        assertNotEquals(collection1, collection2);
    }

    /**
     * Tests that two collections containing identical entities are considered equal.
     */
    @Test
    public void equals_withTwoCollectionsContainingSameEntities_shouldReturnTrue() {
        // Arrange
        StandardEntityCollection collection1 = new StandardEntityCollection();
        collection1.add(createTestEntity());

        StandardEntityCollection collection2 = new StandardEntityCollection();
        collection2.add(createTestEntity());

        // Act & Assert
        assertEquals(collection1, collection2);
    }

    /**
     * Tests that cloning creates an independent copy of the collection.
     */
    @Test
    public void clone_shouldCreateIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        StandardEntityCollection original = new StandardEntityCollection();
        original.add(createTestEntity());

        // Act
        StandardEntityCollection cloned = CloneUtils.clone(original);

        // Assert basic clone properties
        assertNotSame(original, cloned, "Clone should be a different object instance.");
        assertEquals(original, cloned, "Clone should be equal to the original.");

        // Assert independence by modifying the original and checking the clone
        original.clear();
        assertNotEquals(original, cloned, "Modifying the original collection should not affect the clone.");
    }

    /**
     * Tests that serialization and deserialization preserves the collection's state.
     */
    @Test
    public void serialization_shouldPreserveObjectState() {
        // Arrange
        StandardEntityCollection original = new StandardEntityCollection();
        original.add(createTestEntity());

        // Act
        StandardEntityCollection deserialized = TestUtils.serialised(original);

        // Assert
        assertEquals(original, deserialized);
    }
}