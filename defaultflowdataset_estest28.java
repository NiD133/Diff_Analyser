package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.chrono.HijrahEra;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JLayeredPane;
import javax.swing.JRadioButtonMenuItem;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DefaultFlowDataset_ESTestTest28 extends DefaultFlowDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        DefaultFlowDataset<Integer> defaultFlowDataset0 = new DefaultFlowDataset<Integer>();
        Integer integer0 = JLayeredPane.DEFAULT_LAYER;
        Integer integer1 = JLayeredPane.PALETTE_LAYER;
        defaultFlowDataset0.setFlow(0, integer1, integer1, 0);
        NodeKey<Integer> nodeKey0 = new NodeKey<Integer>(0, integer0);
        List<FlowKey> list0 = (List<FlowKey>) defaultFlowDataset0.getOutFlows(nodeKey0);
        assertEquals(1, defaultFlowDataset0.getStageCount());
        assertTrue(list0.isEmpty());
    }
}
