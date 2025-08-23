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

public class DefaultKeyedValueDataset_ESTestTest14 extends DefaultKeyedValueDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        DefaultKeyedValueDataset defaultKeyedValueDataset0 = new DefaultKeyedValueDataset();
        byte[] byteArray0 = new byte[3];
        BigInteger bigInteger0 = new BigInteger(byteArray0);
        boolean boolean0 = defaultKeyedValueDataset0.equals(bigInteger0);
        assertFalse(boolean0);
    }
}