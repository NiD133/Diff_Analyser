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

public class DefaultFlowDataset_ESTestTest43 extends DefaultFlowDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        DefaultFlowDataset<Integer> defaultFlowDataset0 = new DefaultFlowDataset<Integer>();
        Set<FlowKey<Integer>> set0 = defaultFlowDataset0.getAllFlows();
        boolean boolean0 = defaultFlowDataset0.equals(set0);
        assertFalse(boolean0);
    }
}
