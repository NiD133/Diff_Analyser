package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.chrono.HijrahEra;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JLayeredPane;
import javax.swing.JRadioButtonMenuItem;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.data.flow.DefaultFlowDataset;
import org.jfree.data.flow.FlowKey;
import org.jfree.data.flow.NodeKey;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class DefaultFlowDatasetTest extends DefaultFlowDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSetFlowAndHashCode() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer layer = JLayeredPane.DEFAULT_LAYER;
        dataset.setFlow(1, layer, layer, 0);
        dataset.hashCode();
    }

    @Test(timeout = 4000)
    public void testCloneAndEquality() {
        DefaultFlowDataset<Integer> original = new DefaultFlowDataset<>();
        Integer modalLayer = JLayeredPane.MODAL_LAYER;
        Integer popupLayer = JLayeredPane.POPUP_LAYER;
        original.setFlow(0, modalLayer, popupLayer, 0);
        DefaultFlowDataset<Integer> clone = (DefaultFlowDataset<Integer>) original.clone();
        assertTrue(original.equals(clone));
        assertEquals(1, clone.getStageCount());
    }

    @Test(timeout = 4000)
    public void testEqualityAfterModification() {
        DefaultFlowDataset<Integer> dataset1 = new DefaultFlowDataset<>();
        Integer layer = JLayeredPane.DEFAULT_LAYER;
        DefaultFlowDataset<Integer> dataset2 = new DefaultFlowDataset<>();
        assertTrue(dataset2.equals(dataset1));

        dataset2.setFlow(1, layer, layer, 1);
        Object clonedObject = dataset2.clone();
        boolean isEqual = dataset1.equals(clonedObject);
        assertFalse(dataset2.equals(dataset1));
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testGetOutFlowsWithNoFlows() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer paletteLayer = JLayeredPane.PALETTE_LAYER;
        NodeKey<Integer> nodeKey = new NodeKey<>(-352, paletteLayer);
        dataset.setFlow(1, paletteLayer, paletteLayer, -478.3883577094);
        List<FlowKey> outFlows = dataset.getOutFlows(nodeKey);
        assertEquals(0, outFlows.size());
    }

    @Test(timeout = 4000)
    public void testGetInFlowsWithNoFlows() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer layer = JLayeredPane.DEFAULT_LAYER;
        dataset.setFlow(0, layer, layer, 0);
        NodeKey<Integer> nodeKey = new NodeKey<>(213, layer);
        List<FlowKey<Integer>> inFlows = dataset.getInFlows(nodeKey);
        assertEquals(0, inFlows.size());
        assertEquals(1, dataset.getStageCount());
    }

    @Test(timeout = 4000)
    public void testStageCountAfterMultipleFlows() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer paletteLayer = JLayeredPane.PALETTE_LAYER;
        dataset.setFlow(1, paletteLayer, paletteLayer, -1670.283700956413);
        dataset.setFlow(0, paletteLayer, paletteLayer, -2177.79472);
        assertEquals(2, dataset.getStageCount());
    }

    @Test(timeout = 4000)
    public void testGetFlowWithNoFlowSet() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer popupLayer = JLayeredPane.POPUP_LAYER;
        Integer modalLayer = JLayeredPane.MODAL_LAYER;
        Number flow = dataset.getFlow(87, modalLayer, popupLayer);
        assertNull(flow);
    }

    @Test(timeout = 4000)
    public void testGetSourcesAfterSettingFlow() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer layer = JLayeredPane.DEFAULT_LAYER;
        dataset.setFlow(0, layer, layer, 0);
        List<Integer> sources = dataset.getSources(1);
        assertTrue(sources.contains(layer));
        assertEquals(1, dataset.getStageCount());
    }

    @Test(timeout = 4000)
    public void testSetAndGetNodeProperty() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer popupLayer = JLayeredPane.POPUP_LAYER;
        NodeKey<Integer> nodeKey = new NodeKey<>(0, popupLayer);
        JRadioButtonMenuItem radioButton = new JRadioButtonMenuItem((Icon) null);
        dataset.setNodeProperty(nodeKey, "selected", radioButton);
        JRadioButtonMenuItem retrievedRadioButton = (JRadioButtonMenuItem) dataset.getNodeProperty(nodeKey, "selected");
        assertFalse(retrievedRadioButton.isFocusTraversalPolicySet());
    }

    @Test(timeout = 4000)
    public void testSetAndGetFlowProperty() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer modalLayer = JLayeredPane.MODAL_LAYER;
        FlowKey<Integer> flowKey = new FlowKey<>(0, modalLayer, modalLayer);
        dataset.setFlowProperty(flowKey, "selected", flowKey);
        FlowKey retrievedFlowKey = (FlowKey) dataset.getFlowProperty(flowKey, "selected");
        assertEquals(0, retrievedFlowKey.getStage());
    }

    @Test(timeout = 4000)
    public void testGetDestinationsAfterSettingFlow() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer frameContentLayer = JLayeredPane.FRAME_CONTENT_LAYER;
        dataset.setFlow(0, frameContentLayer, frameContentLayer, 0);
        List<Integer> destinations = dataset.getDestinations(0);
        assertTrue(destinations.contains(frameContentLayer));
        assertEquals(1, dataset.getStageCount());
    }

    @Test(timeout = 4000)
    public void testGetAllFlowsAfterSettingFlow() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer frameContentLayer = JLayeredPane.FRAME_CONTENT_LAYER;
        dataset.setFlow(0, frameContentLayer, frameContentLayer, 0);
        dataset.getAllFlows();
        assertEquals(1, dataset.getStageCount());
    }

    @Test(timeout = 4000)
    public void testSetFlowWithInvalidStage() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer modalLayer = JLayeredPane.MODAL_LAYER;
        try {
            dataset.setFlow(-882, modalLayer, modalLayer, -882);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception: Require 'stage' (-882) to be in the range 0 to 1
        }
    }

    @Test(timeout = 4000)
    public void testGetSourcesWithInvalidStage() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        try {
            dataset.getSources(71);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetOutFlowsWithNullNodeKey() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        try {
            dataset.getOutFlows(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception: Null 'nodeKey' argument.
        }
    }

    @Test(timeout = 4000)
    public void testGetInFlowsWithNullNodeKey() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        try {
            dataset.getInFlows(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception: Null 'nodeKey' argument.
        }
    }

    @Test(timeout = 4000)
    public void testGetFlowWithNullSourceAndDestination() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        try {
            dataset.getFlow(-1067, null, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception: Null 'source' argument.
        }
    }

    @Test(timeout = 4000)
    public void testGetDestinationsWithInvalidStage() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        try {
            dataset.getDestinations(1);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetSourcesWithNoFlows() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        List<Integer> sources = dataset.getSources(0);
        assertFalse(sources.contains(0));
    }

    @Test(timeout = 4000)
    public void testGetStageCount() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        int stageCount = dataset.getStageCount();
        assertEquals(1, stageCount);
    }

    @Test(timeout = 4000)
    public void testGetDestinationsWithNoFlows() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        List<Integer> destinations = dataset.getDestinations(0);
        assertTrue(destinations.isEmpty());
    }

    @Test(timeout = 4000)
    public void testCloneAndModifyFlow() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer frameContentLayer = JLayeredPane.FRAME_CONTENT_LAYER;
        dataset.setFlow(0, frameContentLayer, frameContentLayer, 0);
        DefaultFlowDataset<Integer> clone = (DefaultFlowDataset<Integer>) dataset.clone();
        dataset.setFlow(0, frameContentLayer, frameContentLayer, -3612.716603546956);
        assertFalse(dataset.equals(clone));
        assertEquals(1, clone.getStageCount());
    }

    @Test(timeout = 4000)
    public void testCloneAndModifyFlowWithDifferentDestination() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer modalLayer = JLayeredPane.MODAL_LAYER;
        dataset.setFlow(0, modalLayer, modalLayer, 0);
        DefaultFlowDataset<Integer> clone = (DefaultFlowDataset<Integer>) dataset.clone();
        Integer paletteLayer = JLayeredPane.PALETTE_LAYER;
        dataset.setFlow(0, modalLayer, paletteLayer, -744.1398505791022);
        assertFalse(dataset.equals(clone));
        assertEquals(1, dataset.getStageCount());
        assertEquals(1, clone.getStageCount());
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentDatasets() {
        DefaultFlowDataset<Integer> dataset1 = new DefaultFlowDataset<>();
        Integer popupLayer = JLayeredPane.POPUP_LAYER;
        dataset1.setFlow(0, popupLayer, popupLayer, 0);
        DefaultFlowDataset<Integer> dataset2 = new DefaultFlowDataset<>();
        assertFalse(dataset1.equals(dataset2));
        assertEquals(1, dataset1.getStageCount());
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentFlows() {
        DefaultFlowDataset<Integer> dataset1 = new DefaultFlowDataset<>();
        Integer popupLayer = JLayeredPane.POPUP_LAYER;
        dataset1.setFlow(1, popupLayer, popupLayer, 4286.22);
        DefaultFlowDataset<Integer> dataset2 = new DefaultFlowDataset<>();
        assertFalse(dataset1.equals(dataset2));
    }

    @Test(timeout = 4000)
    public void testSelfEquality() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        assertTrue(dataset.equals(dataset));
    }

    @Test(timeout = 4000)
    public void testGetOutFlowsWithEmptyFlows() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer defaultLayer = JLayeredPane.DEFAULT_LAYER;
        Integer paletteLayer = JLayeredPane.PALETTE_LAYER;
        dataset.setFlow(0, paletteLayer, paletteLayer, 0);
        NodeKey<Integer> nodeKey = new NodeKey<>(0, defaultLayer);
        List<FlowKey> outFlows = dataset.getOutFlows(nodeKey);
        assertEquals(1, dataset.getStageCount());
        assertTrue(outFlows.isEmpty());
    }

    @Test(timeout = 4000)
    public void testGetOutFlowsWithNonEmptyFlows() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer defaultLayer = JLayeredPane.DEFAULT_LAYER;
        dataset.setFlow(0, defaultLayer, defaultLayer, 0);
        NodeKey<Integer> nodeKey = new NodeKey<>(0, defaultLayer);
        List<FlowKey> outFlows = dataset.getOutFlows(nodeKey);
        assertEquals(1, dataset.getStageCount());
        assertFalse(outFlows.isEmpty());
    }

    @Test(timeout = 4000)
    public void testGetOutFlowsWithDifferentNodeKey() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer defaultLayer = JLayeredPane.DEFAULT_LAYER;
        dataset.setFlow(0, defaultLayer, defaultLayer, 0);
        NodeKey<Integer> nodeKey = new NodeKey<>(213, defaultLayer);
        List<FlowKey> outFlows = dataset.getOutFlows(nodeKey);
        assertTrue(outFlows.isEmpty());
        assertEquals(1, dataset.getStageCount());
    }

    @Test(timeout = 4000)
    public void testGetOutFlowsWithDifferentStage() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer popupLayer = JLayeredPane.POPUP_LAYER;
        NodeKey<Integer> nodeKey = new NodeKey<>(1, popupLayer);
        List<FlowKey> outFlows = dataset.getOutFlows(nodeKey);
        assertTrue(outFlows.isEmpty());
    }

    @Test(timeout = 4000)
    public void testGetInFlowsWithEmptyFlows() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer modalLayer = JLayeredPane.MODAL_LAYER;
        dataset.setFlow(0, modalLayer, modalLayer, 0);
        Integer paletteLayer = JLayeredPane.PALETTE_LAYER;
        NodeKey<Integer> nodeKey = new NodeKey<>(1, paletteLayer);
        List<FlowKey<Integer>> inFlows = dataset.getInFlows(nodeKey);
        assertEquals(1, dataset.getStageCount());
        assertTrue(inFlows.isEmpty());
    }

    @Test(timeout = 4000)
    public void testGetInFlowsWithNonEmptyFlows() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer frameContentLayer = JLayeredPane.FRAME_CONTENT_LAYER;
        dataset.setFlow(0, frameContentLayer, frameContentLayer, 0);
        NodeKey<Integer> nodeKey = new NodeKey<>(1, frameContentLayer);
        List<FlowKey<Integer>> inFlows = dataset.getInFlows(nodeKey);
        assertFalse(inFlows.isEmpty());
        assertEquals(1, dataset.getStageCount());
    }

    @Test(timeout = 4000)
    public void testGetInFlowsWithEmptyDataset() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer defaultLayer = JLayeredPane.DEFAULT_LAYER;
        NodeKey<Integer> nodeKey = new NodeKey<>(0, defaultLayer);
        List<FlowKey<Integer>> inFlows = dataset.getInFlows(nodeKey);
        assertTrue(inFlows.isEmpty());
    }

    @Test(timeout = 4000)
    public void testSetAndGetFlowPropertyWithDifferentValues() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer defaultLayer = JLayeredPane.DEFAULT_LAYER;
        FlowKey<Integer> flowKey = new FlowKey<>(0, defaultLayer, defaultLayer);
        dataset.setFlowProperty(flowKey, "[FlowKey: 0, 0 -> 0]", defaultLayer);
        HijrahEra hijrahEra = HijrahEra.AH;
        dataset.setFlowProperty(flowKey, "", hijrahEra);
        assertEquals(0, flowKey.getStage());
    }

    @Test(timeout = 4000)
    public void testGetFlowPropertyWithDifferentKeys() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer defaultLayer = JLayeredPane.DEFAULT_LAYER;
        FlowKey<Integer> flowKey = new FlowKey<>(0, defaultLayer, defaultLayer);
        dataset.setFlowProperty(flowKey, "[FlowKey: 0, 0 -> 0]", defaultLayer);
        Object property = dataset.getFlowProperty(flowKey, "");
        assertNull(property);
    }

    @Test(timeout = 4000)
    public void testGetFlowPropertyWithNoPropertySet() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer defaultLayer = JLayeredPane.DEFAULT_LAYER;
        FlowKey<Integer> flowKey = new FlowKey<>(0, defaultLayer, defaultLayer);
        Object property = dataset.getFlowProperty(flowKey, "selected");
        assertNull(property);
    }

    @Test(timeout = 4000)
    public void testSetAndGetNodePropertyWithDifferentValues() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer dragLayer = JLayeredPane.DRAG_LAYER;
        NodeKey<Integer> nodeKey = new NodeKey<>(-1567, dragLayer);
        dataset.setNodeProperty(nodeKey, "", dataset);
        dataset.setNodeProperty(nodeKey, "", nodeKey);
        assertEquals(-1567, nodeKey.getStage());
    }

    @Test(timeout = 4000)
    public void testSetAndGetNodePropertyWithNullValues() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        dataset.setNodeProperty(null, "", null);
        Object property = dataset.getNodeProperty(null, "");
        assertNull(property);
    }

    @Test(timeout = 4000)
    public void testGetNodePropertyWithNoPropertySet() {
        Integer modalLayer = JLayeredPane.MODAL_LAYER;
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        NodeKey<Integer> nodeKey = new NodeKey<>(0, modalLayer);
        Object property = dataset.getNodeProperty(nodeKey, "selected");
        assertNull(property);
    }

    @Test(timeout = 4000)
    public void testGetAllNodesAfterSettingFlow() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer popupLayer = JLayeredPane.POPUP_LAYER;
        dataset.setFlow(1, popupLayer, popupLayer, 4286.22);
        dataset.getAllNodes();
        assertEquals(2, dataset.getStageCount());
    }

    @Test(timeout = 4000)
    public void testGetAllNodesWithEmptyDataset() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        dataset.getAllNodes();
        assertEquals(1, dataset.getStageCount());
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentType() {
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Set<FlowKey<Integer>> allFlows = dataset.getAllFlows();
        assertFalse(dataset.equals(allFlows));
    }
}