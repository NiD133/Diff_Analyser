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

public class DefaultFlowDataset_ESTestTest14 extends DefaultFlowDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        DefaultFlowDataset<Integer> defaultFlowDataset0 = new DefaultFlowDataset<Integer>();
        Integer integer0 = JLayeredPane.MODAL_LAYER;
        // Undeclared exception!
        try {
            defaultFlowDataset0.setFlow((-882), integer0, integer0, (-882));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Require 'stage' (-882) to be in the range 0 to 1
            //
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }
}
