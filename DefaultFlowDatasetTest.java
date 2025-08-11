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

    @Test
    void getFlow_AfterSettingFlow_ReturnsCorrectValue() {
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        dataset.setFlow(0, "A", "Z", 1.5);
        assertEquals(1.5, dataset.getFlow(0, "A", "Z"), 
            "Flow value should match the set value");
    }

    @Test
    void getStageCount_InitialState_ReturnsOneStage() {
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        assertEquals(1, dataset.getStageCount(), 
            "New dataset should have 1 stage");
    }

    @Test
    void getStageCount_AfterSettingFlowInFirstStage_ReturnsOneStage() {
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        dataset.setFlow(0, "A", "Z", 11.1);
        assertEquals(1, dataset.getStageCount(), 
            "Setting flow in stage 0 should keep stage count at 1");
    }

    @Test
    void getStageCount_AfterSettingFlowInNewStage_ReturnsTwoStages() {
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        dataset.setFlow(1, "Z", "P", 5.0);
        assertEquals(2, dataset.getStageCount(), 
            "Setting flow in stage 1 should increase stage count to 2");
    }

    @Test
    void equals_SameDefaultInstances_AreEqual() {
        DefaultFlowDataset<String> dataset1 = new DefaultFlowDataset<>();
        DefaultFlowDataset<String> dataset2 = new DefaultFlowDataset<>();
        assertEquals(dataset1, dataset2, 
            "Two default datasets should be equal");
    }

    @Test
    void equals_AfterSettingFlowInOneDataset_AreNotEqual() {
        DefaultFlowDataset<String> dataset1 = new DefaultFlowDataset<>();
        DefaultFlowDataset<String> dataset2 = new DefaultFlowDataset<>();
        dataset1.setFlow(0, "A", "Z", 1.0);
        assertNotEquals(dataset1, dataset2, 
            "Datasets should differ after adding flow to one");
    }

    @Test
    void equals_AfterSettingSameFlowInBoth_AreEqual() {
        DefaultFlowDataset<String> dataset1 = new DefaultFlowDataset<>();
        DefaultFlowDataset<String> dataset2 = new DefaultFlowDataset<>();
        dataset1.setFlow(0, "A", "Z", 1.0);
        dataset2.setFlow(0, "A", "Z", 1.0);
        assertEquals(dataset1, dataset2, 
            "Datasets should be equal after same flow added to both");
    }

    @Test
    void serialization_RoundTrip_PreservesDatasetEquality() {
        DefaultFlowDataset<String> original = new DefaultFlowDataset<>();
        original.setFlow(0, "A", "Z", 1.0);
        DefaultFlowDataset<String> deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, 
            "Deserialized dataset should equal original");
    }

    @Test
    void cloning_CreatesEqualButDistinctInstance() throws CloneNotSupportedException {
        DefaultFlowDataset<String> original = new DefaultFlowDataset<>();
        original.setFlow(0, "A", "Z", 1.0);
        DefaultFlowDataset<String> clone = (DefaultFlowDataset<String>) original.clone();

        assertNotSame(original, clone, 
            "Clone should be a different object instance");
        assertEquals(original, clone, 
            "Clone should be equal to the original dataset");

        // Verify independence
        original.setFlow(0, "A", "Y", 8.0);
        assertNotEquals(original, clone, 
            "Clone should not reflect changes to original");
        
        clone.setFlow(0, "A", "Y", 8.0);
        assertEquals(original, clone, 
            "Clone should match after same modification");
    }

    @Test
    void publicCloneable_ImplementsInterface() {
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        assertTrue(dataset instanceof PublicCloneable, 
            "Dataset should implement PublicCloneable");
    }
}