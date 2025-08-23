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

public class DefaultFlowDataset_ESTestTest24 extends DefaultFlowDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        DefaultFlowDataset<Integer> defaultFlowDataset0 = new DefaultFlowDataset<Integer>();
        Integer integer0 = JLayeredPane.MODAL_LAYER;
        defaultFlowDataset0.setFlow(0, integer0, integer0, 0);
        DefaultFlowDataset defaultFlowDataset1 = (DefaultFlowDataset) defaultFlowDataset0.clone();
        Integer integer1 = JLayeredPane.PALETTE_LAYER;
        defaultFlowDataset0.setFlow(0, integer0, integer1, (-744.1398505791022));
        boolean boolean0 = defaultFlowDataset0.equals(defaultFlowDataset1);
        assertFalse(boolean0);
        assertEquals(1, defaultFlowDataset0.getStageCount());
        assertEquals(1, defaultFlowDataset1.getStageCount());
    }
}
