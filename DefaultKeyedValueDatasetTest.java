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
 * DefaultKeyedValueDatasetTest.java
 * ---------------------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetTest {

    // Test data constants for better maintainability
    private static final String SAMPLE_KEY = "Test";
    private static final String DIFFERENT_KEY = "Test 2";
    private static final Double SAMPLE_VALUE = 45.5;
    private static final Double DIFFERENT_VALUE = 45.6;
    private static final Double UPDATED_VALUE = 99.9;
    private static final Double SERIALIZATION_VALUE = 25.3;

    /**
     * Tests that the equals method correctly identifies equal and unequal datasets.
     * Verifies equality based on both key and value fields.
     */
    @Test
    public void testEquals() {
        // Test: Identical datasets should be equal
        DefaultKeyedValueDataset identicalDataset1 = new DefaultKeyedValueDataset(SAMPLE_KEY, SAMPLE_VALUE);
        DefaultKeyedValueDataset identicalDataset2 = new DefaultKeyedValueDataset(SAMPLE_KEY, SAMPLE_VALUE);
        
        assertEquals(identicalDataset1, identicalDataset2, "Datasets with identical key and value should be equal");
        assertEquals(identicalDataset2, identicalDataset1, "Equality should be symmetric");

        // Test: Datasets with different keys should not be equal
        DefaultKeyedValueDataset datasetWithDifferentKey = new DefaultKeyedValueDataset(DIFFERENT_KEY, SAMPLE_VALUE);
        DefaultKeyedValueDataset originalDataset = new DefaultKeyedValueDataset(SAMPLE_KEY, SAMPLE_VALUE);
        
        assertNotEquals(originalDataset, datasetWithDifferentKey, 
            "Datasets with different keys should not be equal");

        // Test: Datasets with different values should not be equal
        DefaultKeyedValueDataset datasetWithDifferentValue = new DefaultKeyedValueDataset(SAMPLE_KEY, DIFFERENT_VALUE);
        DefaultKeyedValueDataset datasetWithOriginalValue = new DefaultKeyedValueDataset(SAMPLE_KEY, SAMPLE_VALUE);
        
        assertNotEquals(datasetWithOriginalValue, datasetWithDifferentValue, 
            "Datasets with different values should not be equal");
    }

    /**
     * Tests that cloning creates a proper copy of the dataset.
     * Verifies that the clone is a separate instance but contains equal data.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Given: A dataset with sample data
        DefaultKeyedValueDataset originalDataset = new DefaultKeyedValueDataset(SAMPLE_KEY, SAMPLE_VALUE);
        
        // When: Cloning the dataset
        DefaultKeyedValueDataset clonedDataset = (DefaultKeyedValueDataset) originalDataset.clone();
        
        // Then: Clone should be a separate instance with equal content
        assertNotSame(originalDataset, clonedDataset, 
            "Clone should be a different object instance");
        assertSame(originalDataset.getClass(), clonedDataset.getClass(), 
            "Clone should be of the same class");
        assertEquals(originalDataset, clonedDataset, 
            "Clone should contain equal data to the original");
    }

    /**
     * Tests that cloned datasets are independent of each other.
     * Verifies that modifying one dataset doesn't affect its clone.
     */
    @Test
    public void testCloneIndependence() throws CloneNotSupportedException {
        // Given: A dataset and its clone
        DefaultKeyedValueDataset originalDataset = new DefaultKeyedValueDataset("Key", 10.0);
        DefaultKeyedValueDataset clonedDataset = CloneUtils.clone(originalDataset);
        
        assertEquals(originalDataset, clonedDataset, "Initially, clone should equal original");
        
        // When: Modifying the clone's value
        clonedDataset.updateValue(UPDATED_VALUE);
        
        // Then: Original and clone should no longer be equal
        assertNotEquals(originalDataset, clonedDataset, 
            "After modification, clone should differ from original");
        
        // When: Restoring the clone's value to match original
        clonedDataset.updateValue(10.0);
        
        // Then: They should be equal again
        assertEquals(originalDataset, clonedDataset, 
            "After restoring value, clone should equal original again");
    }

    /**
     * Tests that the dataset can be properly serialized and deserialized.
     * Verifies that serialization preserves the dataset's state.
     */
    @Test
    public void testSerialization() {
        // Given: A dataset with sample data
        DefaultKeyedValueDataset originalDataset = new DefaultKeyedValueDataset(SAMPLE_KEY, SERIALIZATION_VALUE);
        
        // When: Serializing and deserializing the dataset
        DefaultKeyedValueDataset deserializedDataset = TestUtils.serialised(originalDataset);
        
        // Then: Deserialized dataset should equal the original
        assertEquals(originalDataset, deserializedDataset, 
            "Deserialized dataset should equal the original");
    }
}