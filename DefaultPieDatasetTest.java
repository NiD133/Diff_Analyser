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
 * DefaultPieDatasetTest.java
 * -------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest implements DatasetChangeListener {

    private DatasetChangeEvent lastChangeEvent;
    private DefaultPieDataset<String> dataset;

    @BeforeEach
    public void setUp() {
        this.lastChangeEvent = null;
        this.dataset = new DefaultPieDataset<>();
    }

    /**
     * Records the last dataset change event for testing purposes.
     *
     * @param event the dataset change event
     */
    @Override
    public void datasetChanged(DatasetChangeEvent event) {
        this.lastChangeEvent = event;
    }

    // ========== CLEAR METHOD TESTS ==========

    /**
     * Tests that clear() method works correctly and fires appropriate change events.
     */
    @Test
    public void testClear_EmptyDataset_NoEventFired() {
        // Given: empty dataset with change listener
        dataset.addChangeListener(this);
        
        // When: clearing empty dataset
        dataset.clear();
        
        // Then: no change event should be fired
        assertNull(lastChangeEvent, "No event should be fired when clearing empty dataset");
    }

    @Test
    public void testClear_NonEmptyDataset_EventFiredAndDatasetEmpty() {
        // Given: dataset with data and change listener
        dataset.addChangeListener(this);
        dataset.setValue("Category A", 1.0);
        assertEquals(1, dataset.getItemCount(), "Dataset should have 1 item initially");
        lastChangeEvent = null; // Reset after setValue
        
        // When: clearing non-empty dataset
        dataset.clear();
        
        // Then: change event should be fired and dataset should be empty
        assertNotNull(lastChangeEvent, "Change event should be fired when clearing non-empty dataset");
        assertEquals(0, dataset.getItemCount(), "Dataset should be empty after clear");
    }

    // ========== GET KEY METHOD TESTS ==========

    /**
     * Tests that getKey() method returns correct keys for valid indices.
     */
    @Test
    public void testGetKey_ValidIndices_ReturnsCorrectKeys() {
        // Given: dataset with two items
        dataset.setValue("Category A", 1.0);
        dataset.setValue("Category B", 2.0);
        
        // When & Then: getting keys by valid indices
        assertEquals("Category A", dataset.getKey(0), "First key should be 'Category A'");
        assertEquals("Category B", dataset.getKey(1), "Second key should be 'Category B'");
    }

    @Test
    public void testGetKey_NegativeIndex_ThrowsIndexOutOfBoundsException() {
        // Given: dataset with data
        dataset.setValue("Category A", 1.0);
        dataset.setValue("Category B", 2.0);
        
        // When & Then: accessing negative index should throw exception
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getKey(-1),
                "Negative index should throw IndexOutOfBoundsException");
    }

    @Test
    public void testGetKey_IndexTooLarge_ThrowsIndexOutOfBoundsException() {
        // Given: dataset with 2 items
        dataset.setValue("Category A", 1.0);
        dataset.setValue("Category B", 2.0);
        
        // When & Then: accessing index beyond dataset size should throw exception
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getKey(2),
                "Index beyond dataset size should throw IndexOutOfBoundsException");
    }

    // ========== GET INDEX METHOD TESTS ==========

    /**
     * Tests that getIndex() method returns correct indices for existing keys.
     */
    @Test
    public void testGetIndex_ExistingKeys_ReturnsCorrectIndices() {
        // Given: dataset with two items
        dataset.setValue("Category A", 1.0);
        dataset.setValue("Category B", 2.0);
        
        // When & Then: getting indices for existing keys
        assertEquals(0, dataset.getIndex("Category A"), "Index of 'Category A' should be 0");
        assertEquals(1, dataset.getIndex("Category B"), "Index of 'Category B' should be 1");
    }

    @Test
    public void testGetIndex_NonExistentKey_ReturnsMinusOne() {
        // Given: dataset with data
        dataset.setValue("Category A", 1.0);
        dataset.setValue("Category B", 2.0);
        
        // When & Then: getting index for non-existent key should return -1
        assertEquals(-1, dataset.getIndex("NonExistent"), 
                "Index of non-existent key should be -1");
    }

    @Test
    public void testGetIndex_NullKey_ThrowsIllegalArgumentException() {
        // Given: dataset with data
        dataset.setValue("Category A", 1.0);
        
        // When & Then: null key should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> dataset.getIndex(null),
                "Null key should throw IllegalArgumentException");
    }

    // ========== CLONING TESTS ==========

    /**
     * Tests that cloning creates a proper deep copy of the dataset.
     */
    @Test
    public void testCloning_CreatesProperDeepCopy() throws CloneNotSupportedException {
        // Given: dataset with various data types including null
        DefaultPieDataset<String> originalDataset = new DefaultPieDataset<>();
        originalDataset.setValue("Value1", 1);
        originalDataset.setValue("Value2", null);
        originalDataset.setValue("Value3", 3);
        
        // When: cloning the dataset
        DefaultPieDataset<String> clonedDataset = CloneUtils.clone(originalDataset);
        
        // Then: clone should be proper deep copy
        assertNotSame(originalDataset, clonedDataset, "Clone should be different object instance");
        assertSame(originalDataset.getClass(), clonedDataset.getClass(), "Clone should have same class");
        assertEquals(originalDataset, clonedDataset, "Clone should be equal to original");
    }

    // ========== SERIALIZATION TESTS ==========

    /**
     * Tests that serialization and deserialization preserves dataset equality.
     */
    @Test
    public void testSerialization_PreservesDatasetEquality() {
        // Given: dataset with various data including null values
        DefaultPieDataset<String> originalDataset = new DefaultPieDataset<>();
        originalDataset.setValue("Category1", 234.2);
        originalDataset.setValue("Category2", null);
        originalDataset.setValue("Category3", 345.9);
        originalDataset.setValue("Category4", 452.7);
        
        // When: serializing and deserializing
        DefaultPieDataset<String> deserializedDataset = TestUtils.serialised(originalDataset);
        
        // Then: deserialized dataset should equal original
        assertEquals(originalDataset, deserializedDataset, 
                "Deserialized dataset should equal original dataset");
    }

    // ========== BUG REGRESSION TESTS ==========

    /**
     * Regression test for bug #212: getValue() with invalid indices should throw IndexOutOfBoundsException.
     * See: https://github.com/jfree/jfreechart/issues/212
     */
    @Test
    public void testGetValue_InvalidIndices_ThrowsIndexOutOfBoundsException() {
        // Given: empty dataset
        DefaultPieDataset<String> emptyDataset = new DefaultPieDataset<>();
        
        // When & Then: invalid indices on empty dataset should throw exceptions
        assertThrows(IndexOutOfBoundsException.class, () -> emptyDataset.getValue(-1),
                "Negative index should throw IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> emptyDataset.getValue(0),
                "Index 0 on empty dataset should throw IndexOutOfBoundsException");
        
        // Given: dataset with one item
        emptyDataset.setValue("Category A", 1.0);
        
        // When & Then: valid index should work, invalid index should throw exception
        assertEquals(1.0, emptyDataset.getValue(0), "Valid index should return correct value");
        assertThrows(IndexOutOfBoundsException.class, () -> emptyDataset.getValue(1),
                "Index beyond dataset size should throw IndexOutOfBoundsException");
    }
}