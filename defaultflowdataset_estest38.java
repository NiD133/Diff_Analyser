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

public class DefaultFlowDataset_ESTestTest38 extends DefaultFlowDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        DefaultFlowDataset<Integer> defaultFlowDataset0 = new DefaultFlowDataset<Integer>();
        Integer integer0 = JLayeredPane.DRAG_LAYER;
        NodeKey<Integer> nodeKey0 = new NodeKey<Integer>((-1567), integer0);
        defaultFlowDataset0.setNodeProperty(nodeKey0, "", defaultFlowDataset0);
        defaultFlowDataset0.setNodeProperty(nodeKey0, "", nodeKey0);
        assertEquals((-1567), nodeKey0.getStage());
    }
}
