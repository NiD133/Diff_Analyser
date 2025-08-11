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

import java.awt.geom.Rectangle2D;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    // Test data constants for better maintainability
    private static final double ENTITY_X = 1.0;
    private static final double ENTITY_Y = 2.0;
    private static final double ENTITY_WIDTH = 3.0;
    private static final double ENTITY_HEIGHT = 4.0;
    private static final int PIE_SECTION_INDEX = 0;
    private static final int PIE_SECTION_COUNT = 1;
    private static final String PIE_SECTION_KEY = "Key";
    private static final String ENTITY_TOOLTIP = "ToolTip";
    private static final String ENTITY_URL = "URL";

    /**
     * Tests that equals() method correctly identifies equal and unequal collections.
     * Verifies that:
     * - Empty collections are equal
     * - Collections with different entities are not equal
     * - Collections with identical entities are equal
     */
    @Test
    public void testEquals() {
        // Given: Two empty entity collections
        StandardEntityCollection emptyCollection1 = new StandardEntityCollection();
        StandardEntityCollection emptyCollection2 = new StandardEntityCollection();
        
        // Then: Empty collections should be equal
        assertEquals(emptyCollection1, emptyCollection2, 
            "Two empty StandardEntityCollections should be equal");

        // When: Adding an entity to the first collection
        PieSectionEntity<String> firstEntity = createTestPieSectionEntity();
        emptyCollection1.add(firstEntity);
        
        // Then: Collections with different content should not be equal
        assertNotEquals(emptyCollection1, emptyCollection2,
            "Collections with different entities should not be equal");
        
        // When: Adding an identical entity to the second collection
        PieSectionEntity<String> identicalEntity = createTestPieSectionEntity();
        emptyCollection2.add(identicalEntity);
        
        // Then: Collections with identical entities should be equal
        assertEquals(emptyCollection1, emptyCollection2,
            "Collections with identical entities should be equal");
    }

    /**
     * Tests that cloning creates a proper deep copy of the collection.
     * Verifies that:
     * - Cloned object is a different instance
     * - Cloned object has the same class
     * - Cloned object is equal to the original
     * - Changes to original don't affect the clone (independence)
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Given: A collection with one entity
        StandardEntityCollection originalCollection = new StandardEntityCollection();
        PieSectionEntity<String> testEntity = createTestPieSectionEntity();
        originalCollection.add(testEntity);
        
        // When: Cloning the collection
        StandardEntityCollection clonedCollection = CloneUtils.clone(originalCollection);
        
        // Then: Clone should be a different instance but equal in content
        assertNotSame(originalCollection, clonedCollection,
            "Cloned collection should be a different object instance");
        assertSame(originalCollection.getClass(), clonedCollection.getClass(),
            "Cloned collection should have the same class as original");
        assertEquals(originalCollection, clonedCollection,
            "Cloned collection should be equal to original collection");

        // When: Modifying the original collection
        originalCollection.clear();
        
        // Then: Clone should remain unchanged (proving independence)
        assertNotEquals(originalCollection, clonedCollection,
            "Original and clone should be independent after modification");
        
        // When: Clearing the clone as well
        clonedCollection.clear();
        
        // Then: Both empty collections should be equal again
        assertEquals(originalCollection, clonedCollection,
            "Both collections should be equal when both are empty");
    }

    /**
     * Tests that serialization and deserialization preserve object equality.
     * Verifies that a collection can be serialized and deserialized while
     * maintaining its content and equality with the original.
     */
    @Test
    public void testSerialization() {
        // Given: A collection with one entity
        StandardEntityCollection originalCollection = new StandardEntityCollection();
        PieSectionEntity<String> testEntity = createTestPieSectionEntity();
        originalCollection.add(testEntity);
        
        // When: Serializing and deserializing the collection
        StandardEntityCollection deserializedCollection = TestUtils.serialised(originalCollection);
        
        // Then: Deserialized collection should be equal to the original
        assertEquals(originalCollection, deserializedCollection,
            "Deserialized collection should be equal to original collection");
    }

    /**
     * Creates a test PieSectionEntity with predefined values for consistent testing.
     * This helper method reduces code duplication and makes test data management easier.
     * 
     * @return A PieSectionEntity configured with test data
     */
    private PieSectionEntity<String> createTestPieSectionEntity() {
        Rectangle2D entityArea = new Rectangle2D.Double(
            ENTITY_X, ENTITY_Y, ENTITY_WIDTH, ENTITY_HEIGHT);
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        
        return new PieSectionEntity<>(
            entityArea,
            dataset,
            PIE_SECTION_INDEX,
            PIE_SECTION_COUNT,
            PIE_SECTION_KEY,
            ENTITY_TOOLTIP,
            ENTITY_URL
        );
    }
}