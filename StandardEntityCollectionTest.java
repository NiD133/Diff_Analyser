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

    /**
     * Creates a sample PieSectionEntity for testing.
     */
    private static PieSectionEntity<String> createSampleEntity() {
        return new PieSectionEntity<>(
            new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
            new DefaultPieDataset<String>(),
            0, 1, "Key", "ToolTip", "URL"
        );
    }

    /**
     * Tests that two empty collections are equal.
     */
    @Test
    public void emptyCollectionsAreEqual() {
        StandardEntityCollection collection1 = new StandardEntityCollection();
        StandardEntityCollection collection2 = new StandardEntityCollection();
        assertEquals(collection1, collection2, "Empty collections should be equal");
    }

    /**
     * Tests that collections are not equal after adding an entity to one collection.
     */
    @Test
    public void collectionsDifferAfterEntityAddition() {
        StandardEntityCollection collection = new StandardEntityCollection();
        StandardEntityCollection emptyCollection = new StandardEntityCollection();
        
        collection.add(createSampleEntity());
        
        assertNotEquals(collection, emptyCollection, 
            "Collections should differ after adding an entity to one");
    }

    /**
     * Tests that collections become equal again after adding equivalent entities.
     */
    @Test
    public void collectionsEqualAfterAddingEquivalentEntities() {
        StandardEntityCollection collection1 = new StandardEntityCollection();
        StandardEntityCollection collection2 = new StandardEntityCollection();
        
        collection1.add(createSampleEntity());
        collection2.add(createSampleEntity());
        
        assertEquals(collection1, collection2, 
            "Collections should be equal after adding equivalent entities");
    }

    /**
     * Tests that cloning creates an independent copy with identical content.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        StandardEntityCollection original = new StandardEntityCollection();
        original.add(createSampleEntity());
        
        // Clone the collection
        StandardEntityCollection clone = CloneUtils.clone(original);
        
        // Verify clone properties
        assertNotSame(original, clone, "Cloned collection should be a different object");
        assertSame(original.getClass(), clone.getClass(), "Cloned collection should have the same class");
        assertEquals(original, clone, "Clone should have identical content to original");
        
        // Verify independence by modifying original
        original.clear();
        assertNotEquals(original, clone, "Clone should remain unchanged when original is modified");
        
        // Modify clone to match original
        clone.clear();
        assertEquals(original, clone, "Cleared collections should be equal");
    }

    /**
     * Tests that serialization preserves collection content.
     */
    @Test
    public void testSerialization() {
        StandardEntityCollection original = new StandardEntityCollection();
        original.add(createSampleEntity());
        
        // Serialize and deserialize
        StandardEntityCollection deserialized = TestUtils.serialised(original);
        
        assertEquals(original, deserialized, "Deserialized collection should equal original");
    }

}