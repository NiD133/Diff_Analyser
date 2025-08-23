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

public class DefaultFlowDataset_ESTestTest4 extends DefaultFlowDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        DefaultFlowDataset<Integer> defaultFlowDataset0 = new DefaultFlowDataset<Integer>();
        Integer integer0 = JLayeredPane.PALETTE_LAYER;
        NodeKey<Integer> nodeKey0 = new NodeKey<Integer>((-352), integer0);
        defaultFlowDataset0.setFlow(1, integer0, integer0, (-478.3883577094));
        List<FlowKey> list0 = (List<FlowKey>) defaultFlowDataset0.getOutFlows(nodeKey0);
        assertEquals(0, list0.size());
    }
}