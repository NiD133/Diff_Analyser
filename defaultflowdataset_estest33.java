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

public class DefaultFlowDataset_ESTestTest33 extends DefaultFlowDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        DefaultFlowDataset<Integer> defaultFlowDataset0 = new DefaultFlowDataset<Integer>();
        Integer integer0 = JLayeredPane.FRAME_CONTENT_LAYER;
        defaultFlowDataset0.setFlow(0, integer0, integer0, 0);
        NodeKey<Integer> nodeKey0 = new NodeKey<Integer>(1, integer0);
        List<FlowKey<Integer>> list0 = defaultFlowDataset0.getInFlows(nodeKey0);
        assertFalse(list0.isEmpty());
        assertEquals(1, defaultFlowDataset0.getStageCount());
    }
}
