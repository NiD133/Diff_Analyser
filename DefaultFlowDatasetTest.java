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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    // Test data constants for better readability and maintainability
    private static final String SOURCE_NODE_A = "A";
    private static final String SOURCE_NODE_Z = "Z";
    private static final String DESTINATION_NODE_Y = "Y";
    private static final String DESTINATION_NODE_P = "P";
    private static final int STAGE_0 = 0;
    private static final int STAGE_1 = 1;
    private static final double FLOW_VALUE_1_5 = 1.5;
    private static final double FLOW_VALUE_1_0 = 1.0;
    private static final double FLOW_VALUE_5_0 = 5.0;
    private static final double FLOW_VALUE_8_0 = 8.0;
    private static final double FLOW_VALUE_11_1 = 11.1;

    /**
     * Creates a basic flow dataset for testing purposes.
     */
    private DefaultFlowDataset<String> createBasicFlowDataset() {
        return new DefaultFlowDataset<>();
    }

    /**
     * Creates a flow dataset with a single flow for testing purposes.
     */
    private DefaultFlowDataset<String> createDatasetWithSingleFlow() {
        DefaultFlowDataset<String> dataset = createBasicFlowDataset();
        dataset.setFlow(STAGE_0, SOURCE_NODE_A, SOURCE_NODE_Z, FLOW_VALUE_1_0);
        return dataset;
    }

    /**
     * Tests that getFlow() correctly retrieves a flow value that was previously set.
     */
    @Test
    public void testGetFlow_ReturnsCorrectFlowValue() {
        // Given: A dataset with a flow from A to Z with value 1.5
        DefaultFlowDataset<String> dataset = createBasicFlowDataset();
        dataset.setFlow(STAGE_0, SOURCE_NODE_A, SOURCE_NODE_Z, FLOW_VALUE_1_5);
        
        // When: Getting the flow value
        double actualFlow = dataset.getFlow(STAGE_0, SOURCE_NODE_A, SOURCE_NODE_Z);
        
        // Then: The returned value should match what was set
        assertEquals(FLOW_VALUE_1_5, actualFlow, 
            "Flow value should match the value that was set");
    }

    /**
     * Tests that getStageCount() returns correct values for various dataset states.
     */
    @Test
    public void testGetStageCount_ReturnsCorrectCount() {
        DefaultFlowDataset<String> dataset = createBasicFlowDataset();
        
        // Empty dataset should have 1 stage by default
        assertEquals(1, dataset.getStageCount(), 
            "Empty dataset should have exactly 1 stage");

        // Adding a flow to stage 0 should not increase stage count
        dataset.setFlow(STAGE_0, SOURCE_NODE_A, SOURCE_NODE_Z, FLOW_VALUE_11_1);
        assertEquals(1, dataset.getStageCount(), 
            "Dataset with flow in stage 0 should still have 1 stage");

        // Adding a flow to stage 1 should increase stage count to 2
        dataset.setFlow(STAGE_1, SOURCE_NODE_Z, DESTINATION_NODE_P, FLOW_VALUE_5_0);
        assertEquals(2, dataset.getStageCount(), 
            "Dataset with flows in stages 0 and 1 should have 2 stages");
    }

    /**
     * Tests that equals() method correctly identifies equal and unequal datasets.
     */
    @Test
    public void testEquals_CorrectlyComparesDatasets() {
        DefaultFlowDataset<String> dataset1 = createBasicFlowDataset();
        DefaultFlowDataset<String> dataset2 = createBasicFlowDataset();
        
        // Two empty datasets should be equal
        assertEquals(dataset1, dataset2, 
            "Two empty datasets should be equal");
        
        // Datasets with different flows should not be equal
        dataset1.setFlow(STAGE_0, SOURCE_NODE_A, SOURCE_NODE_Z, FLOW_VALUE_1_0);
        assertNotEquals(dataset1, dataset2, 
            "Datasets with different flows should not be equal");
        
        // Datasets with the same flows should be equal
        dataset2.setFlow(STAGE_0, SOURCE_NODE_A, SOURCE_NODE_Z, FLOW_VALUE_1_0);
        assertEquals(dataset1, dataset2, 
            "Datasets with identical flows should be equal");
    }

    /**
     * Tests that serialization preserves dataset equality.
     */
    @Test
    public void testSerialization_PreservesDatasetEquality() {
        // Given: A dataset with flow data
        DefaultFlowDataset<String> originalDataset = createDatasetWithSingleFlow();
        
        // When: Serializing and deserializing the dataset
        DefaultFlowDataset<String> deserializedDataset = TestUtils.serialised(originalDataset);
        
        // Then: The deserialized dataset should equal the original
        assertEquals(originalDataset, deserializedDataset, 
            "Deserialized dataset should equal the original dataset");
    }

    /**
     * Tests that cloning creates an independent copy of the dataset.
     */
    @Test
    public void testCloning_CreatesIndependentCopy() throws CloneNotSupportedException {
        // Given: A dataset with flow data
        DefaultFlowDataset<String> originalDataset = createDatasetWithSingleFlow();
        
        // When: Cloning the dataset
        DefaultFlowDataset<String> clonedDataset = 
            (DefaultFlowDataset<String>) originalDataset.clone();
        
        // Then: The clone should be a separate instance but equal in content
        assertNotSame(originalDataset, clonedDataset, 
            "Cloned dataset should be a different instance");
        assertSame(originalDataset.getClass(), clonedDataset.getClass(), 
            "Cloned dataset should have the same class");
        assertEquals(originalDataset, clonedDataset, 
            "Cloned dataset should equal the original dataset");
    }

    /**
     * Tests that cloned datasets are truly independent (changes to one don't affect the other).
     */
    @Test
    public void testCloning_EnsuresDataIndependence() throws CloneNotSupportedException {
        // Given: A dataset and its clone
        DefaultFlowDataset<String> originalDataset = createDatasetWithSingleFlow();
        DefaultFlowDataset<String> clonedDataset = 
            (DefaultFlowDataset<String>) originalDataset.clone();
        
        // When: Modifying the original dataset
        originalDataset.setFlow(STAGE_0, SOURCE_NODE_A, DESTINATION_NODE_Y, FLOW_VALUE_8_0);
        
        // Then: The datasets should no longer be equal
        assertNotEquals(originalDataset, clonedDataset, 
            "Modifying original should not affect the clone");
        
        // When: Making the same modification to the clone
        clonedDataset.setFlow(STAGE_0, SOURCE_NODE_A, DESTINATION_NODE_Y, FLOW_VALUE_8_0);
        
        // Then: The datasets should be equal again
        assertEquals(originalDataset, clonedDataset, 
            "Datasets should be equal after making identical modifications");
    }

    /**
     * Tests that DefaultFlowDataset implements the PublicCloneable interface.
     */
    @Test
    public void testPublicCloneable_InterfaceImplementation() {
        DefaultFlowDataset<String> dataset = createBasicFlowDataset();
        
        assertTrue(dataset instanceof PublicCloneable, 
            "DefaultFlowDataset should implement PublicCloneable interface");
    }
}