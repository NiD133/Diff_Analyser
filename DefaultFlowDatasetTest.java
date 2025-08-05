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
 * ---------------------------
 * DefaultFlowDatasetTest.java
 * ---------------------------
 * (C) Copyright 2021-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DefaultFlowDataset} class, focusing on its core
 * functionality, state management, and contract compliance (equals, cloneable,
 * serializable).
 */
@DisplayName("DefaultFlowDataset")
class DefaultFlowDatasetTest {

    private DefaultFlowDataset<String> dataset;

    @BeforeEach
    void setUp() {
        dataset = new DefaultFlowDataset<>();
    }

    @Test
    @DisplayName("should retrieve a flow value after it has been set")
    void getFlow_shouldReturnCorrectValue_whenFlowIsSet() {
        // Arrange
        final int stage = 0;
        final String source = "A";
        final String destination = "Z";
        final double flowValue = 1.5;
        dataset.setFlow(stage, source, destination, flowValue);

        // Act
        double result = dataset.getFlow(stage, source, destination).doubleValue();

        // Assert
        assertEquals(flowValue, result);
    }

    @Nested
    @DisplayName("getStageCount")
    class GetStageCountTests {
        @Test
        @DisplayName("should return 1 for a new, empty dataset")
        void shouldReturnOneForNewDataset() {
            // Arrange (dataset is new from setUp)

            // Act
            int stageCount = dataset.getStageCount();

            // Assert
            assertEquals(1, stageCount, "An empty dataset should have one default stage.");
        }

        @Test
        @DisplayName("should not increase when adding a flow to an existing stage")
        void shouldNotIncreaseWhenAddingFlowToExistingStage() {
            // Arrange
            dataset.setFlow(0, "A", "Z", 11.1);

            // Act
            int stageCount = dataset.getStageCount();

            // Assert
            assertEquals(1, stageCount);
        }

        @Test
        @DisplayName("should increase when adding a flow to a new stage")
        void shouldIncreaseWhenAddingFlowToNewStage() {
            // Arrange
            dataset.setFlow(1, "Z", "P", 5.0);

            // Act
            int stageCount = dataset.getStageCount();

            // Assert
            assertEquals(2, stageCount);
        }
    }

    @Test
    @DisplayName("equals() should correctly compare dataset content")
    void equals_shouldCorrectlyCompareDatasetContent() {
        // Arrange
        DefaultFlowDataset<String> dataset1 = new DefaultFlowDataset<>();
        DefaultFlowDataset<String> dataset2 = new DefaultFlowDataset<>();

        // Assert: Two newly created datasets should be equal
        assertEquals(dataset1, dataset2);

        // Act: Modify the first dataset
        dataset1.setFlow(0, "A", "Z", 1.0);

        // Assert: Datasets should no longer be equal
        assertNotEquals(dataset1, dataset2);

        // Act: Apply the same modification to the second dataset
        dataset2.setFlow(0, "A", "Z", 1.0);

        // Assert: Datasets should be equal again
        assertEquals(dataset1, dataset2);
    }

    @Test
    @DisplayName("should remain equal after serialization and deserialization")
    void serialization_shouldPreserveDatasetEquality() {
        // Arrange
        dataset.setFlow(0, "A", "Z", 1.0);

        // Act
        DefaultFlowDataset<String> deserializedDataset = TestUtils.serialised(dataset);

        // Assert
        assertEquals(dataset, deserializedDataset);
    }

    @Test
    @DisplayName("clone() should produce an independent and equal copy")
    void clone_shouldProduceIndependentAndEqualCopy() throws CloneNotSupportedException {
        // Arrange
        dataset.setFlow(0, "A", "Z", 1.0);

        // Act
        DefaultFlowDataset<String> clonedDataset = (DefaultFlowDataset<String>) dataset.clone();

        // Assert: Basic clone contract
        assertNotSame(dataset, clonedDataset, "Clone should be a different object instance.");
        assertEquals(dataset, clonedDataset, "Clone should be equal to the original.");

        // Assert: Deep copy - modifying the original should not affect the clone
        dataset.setFlow(0, "A", "Y", 8.0);
        assertNotEquals(dataset, clonedDataset, "Modifying original should make it unequal to the clone.");
    }

    @Test
    @DisplayName("should implement PublicCloneable")
    void class_shouldImplementPublicCloneable() {
        // Assert
        assertTrue(dataset instanceof PublicCloneable);
    }
}