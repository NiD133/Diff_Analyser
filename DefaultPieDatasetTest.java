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
 * PieDatasetTest.java
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DefaultPieDataset} class.
 * This test class implements {@link DatasetChangeListener} to verify that
 * {@link DatasetChangeEvent}s are fired correctly.
 */
@DisplayName("DefaultPieDataset")
public class DefaultPieDatasetTest implements DatasetChangeListener {

    private DatasetChangeEvent lastEvent;

    /**
     * Records the last received dataset change event.
     *
     * @param event the event.
     */
    @Override
    public void datasetChanged(DatasetChangeEvent event) {
        this.lastEvent = event;
    }

    /**
     * Resets the listener state before each test.
     */
    @BeforeEach
    void setUp() {
        this.lastEvent = null;
    }

    @Test
    @DisplayName("Clearing an already empty dataset should not fire a change event")
    void clear_whenDatasetIsEmpty_shouldNotFireChangeEvent() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.addChangeListener(this);

        // Act
        dataset.clear();

        // Assert
        assertNull(this.lastEvent, "A change event should not be fired for an empty dataset.");
        assertEquals(0, dataset.getItemCount());
    }

    @Test
    @DisplayName("Clearing a non-empty dataset should fire a change event and remove all items")
    void clear_whenDatasetIsNotEmpty_shouldFireChangeEventAndRemoveAllItems() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);
        dataset.addChangeListener(this);

        // Act
        dataset.clear();

        // Assert
        assertNotNull(this.lastEvent, "A change event should be fired when clearing a non-empty dataset.");
        assertEquals(0, dataset.getItemCount(), "The dataset should be empty after clearing.");
    }

    @Test
    @DisplayName("getKey should return the correct key for a valid index")
    void getKey_withValidIndex_shouldReturnCorrectKey() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);
        dataset.setValue("B", 2.0);

        // Act & Assert
        assertEquals("A", dataset.getKey(0));
        assertEquals("B", dataset.getKey(1));
    }

    @Test
    @DisplayName("getKey should throw IndexOutOfBoundsException for an out-of-bounds index")
    void getKey_withInvalidIndex_shouldThrowException() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getKey(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getKey(1));
    }

    @Test
    @DisplayName("getIndex should return the correct index for an existing key")
    void getIndex_withExistingKey_shouldReturnCorrectIndex() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);
        dataset.setValue("B", 2.0);

        // Act & Assert
        assertEquals(0, dataset.getIndex("A"));
        assertEquals(1, dataset.getIndex("B"));
    }

    @Test
    @DisplayName("getIndex should return -1 for a non-existent key")
    void getIndex_withNonExistentKey_shouldReturnNegativeOne() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);

        // Act & Assert
        assertEquals(-1, dataset.getIndex("Non-existent Key"));
    }

    @Test
    @DisplayName("getIndex should throw IllegalArgumentException for a null key")
    void getIndex_withNullKey_shouldThrowException() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> dataset.getIndex(null));
    }

    @Test
    @DisplayName("A cloned dataset should be an independent object with equal content")
    void clone_shouldProduceIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        DefaultPieDataset<String> original = new DefaultPieDataset<>();
        original.setValue("V1", 1);
        original.setValue("V2", null);
        original.setValue("V3", 3);

        // Act
        DefaultPieDataset<String> clone = CloneUtils.clone(original);

        // Assert
        assertNotSame(original, clone, "The clone should be a different instance.");
        assertEquals(original, clone, "The clone should be equal to the original in content.");

        // Verify independence by modifying the clone
        clone.setValue("V1", 99);
        assertNotEquals(original.getValue("V1"), clone.getValue("V1"), "Modifying the clone should not affect the original.");
    }

    @Test
    @DisplayName("A deserialized dataset should be equal to the original")
    void serialization_shouldPreserveDatasetState() {
        // Arrange
        DefaultPieDataset<String> original = new DefaultPieDataset<>();
        original.setValue("C1", 234.2);
        original.setValue("C2", null);
        original.setValue("C3", 345.9);
        original.setValue("C4", 452.7);

        // Act
        DefaultPieDataset<String> deserialized = TestUtils.serialised(original);

        // Assert
        assertEquals(original, deserialized, "The deserialized dataset should be equal to the original.");
    }

    @Test
    @DisplayName("getValue should return the correct value for a valid index")
    void getValue_withValidIndex_shouldReturnValue() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);

        // Act & Assert
        assertEquals(1.0, dataset.getValue(0));
    }

    @Test
    @DisplayName("getValue should throw IndexOutOfBoundsException for any index on an empty dataset")
    void getValue_onEmptyDataset_shouldThrowException() {
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(-1), "Negative index should throw.");
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(0), "Index 0 on empty dataset should throw.");
    }

    @Test
    @DisplayName("getValue should throw IndexOutOfBoundsException for an out-of-bounds index on a non-empty dataset")
    void getValue_withInvalidIndexOnNonEmptyDataset_shouldThrowException() {
        // This test case is relevant to bug #212
        // Arrange
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("A", 1.0);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(-1), "Negative index should throw.");
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(1), "Index greater than size-1 should throw.");
    }
}