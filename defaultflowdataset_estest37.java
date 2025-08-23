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

public class DefaultFlowDataset_ESTestTest37 extends DefaultFlowDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        DefaultFlowDataset<Integer> defaultFlowDataset0 = new DefaultFlowDataset<Integer>();
        Integer integer0 = JLayeredPane.DEFAULT_LAYER;
        FlowKey<Integer> flowKey0 = new FlowKey<Integer>(0, integer0, integer0);
        Object object0 = defaultFlowDataset0.getFlowProperty(flowKey0, "selected");
        assertNull(object0);
    }
}