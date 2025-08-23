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

public class DefaultFlowDataset_ESTestTest12 extends DefaultFlowDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        DefaultFlowDataset<Integer> defaultFlowDataset0 = new DefaultFlowDataset<Integer>();
        Integer integer0 = JLayeredPane.FRAME_CONTENT_LAYER;
        defaultFlowDataset0.setFlow(0, integer0, integer0, 0);
        List<Integer> list0 = defaultFlowDataset0.getDestinations(0);
        assertTrue(list0.contains(integer0));
        assertEquals(1, defaultFlowDataset0.getStageCount());
    }
}
