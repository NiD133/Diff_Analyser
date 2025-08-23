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

public class DefaultKeyedValueDataset_ESTestTest2 extends DefaultKeyedValueDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        MockDate mockDate0 = new MockDate(0, 1, 0);
        OHLCDataItem oHLCDataItem0 = new OHLCDataItem(mockDate0, (-1.0), (-1.0), 0.0, 0.0, 0.0);
        byte[] byteArray0 = new byte[5];
        byteArray0[3] = (byte) (-53);
        BigInteger bigInteger0 = new BigInteger(byteArray0);
        DefaultKeyedValueDataset defaultKeyedValueDataset0 = new DefaultKeyedValueDataset(oHLCDataItem0, bigInteger0);
        Number number0 = defaultKeyedValueDataset0.getValue();
        assertSame(bigInteger0, number0);
    }
}
