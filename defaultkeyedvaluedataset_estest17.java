package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigInteger;
import javax.swing.JLayeredPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.util.MockDate;
import org.jfree.chart.date.SerialDate;
import org.jfree.chart.date.SpreadsheetDate;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.xy.OHLCDataItem;
import org.junit.runner.RunWith;

public class DefaultKeyedValueDataset_ESTestTest17 extends DefaultKeyedValueDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        DefaultKeyedValueDataset defaultKeyedValueDataset0 = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset defaultKeyedValueDataset1 = new DefaultKeyedValueDataset(defaultKeyedValueDataset0);
        Integer integer0 = JLayeredPane.DRAG_LAYER;
        // Undeclared exception!
        try {
            defaultKeyedValueDataset1.updateValue(integer0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null 'key' argument.
            //
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }
}
