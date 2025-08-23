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

public class DefaultKeyedValueDataset_ESTestTest4 extends DefaultKeyedValueDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        SimpleHistogramBin simpleHistogramBin0 = new SimpleHistogramBin((-1201.1136675752093), 1.0, false, false);
        byte[] byteArray0 = new byte[3];
        byteArray0[2] = (byte) (-93);
        BigInteger bigInteger0 = new BigInteger(byteArray0);
        DefaultKeyedValueDataset defaultKeyedValueDataset0 = new DefaultKeyedValueDataset(simpleHistogramBin0, bigInteger0);
        Number number0 = defaultKeyedValueDataset0.getValue();
        assertEquals((byte) (-93), number0.byteValue());
    }
}
