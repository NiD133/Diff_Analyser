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

public class DefaultKeyedValueDataset_ESTestTest22 extends DefaultKeyedValueDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        MockDate mockDate0 = new MockDate((-2363), (-1655), 1600);
        OHLCDataItem oHLCDataItem0 = new OHLCDataItem(mockDate0, 1600, (-1655), 1.0, 1600, (-1655));
        BigInteger bigInteger0 = BigInteger.TEN;
        DefaultKeyedValueDataset defaultKeyedValueDataset0 = new DefaultKeyedValueDataset(oHLCDataItem0, bigInteger0);
        defaultKeyedValueDataset0.setValue(oHLCDataItem0, bigInteger0);
        assertTrue(defaultKeyedValueDataset0.getNotify());
    }
}
