package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.util.MockDate;
import org.jfree.chart.date.SerialDate;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.runner.RunWith;

public class SymbolicXYItemLabelGenerator_ESTestTest2 extends SymbolicXYItemLabelGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test1() throws Throwable {
        SymbolicXYItemLabelGenerator symbolicXYItemLabelGenerator0 = new SymbolicXYItemLabelGenerator();
        MockDate mockDate0 = new MockDate(102L);
        SerialDate serialDate0 = SerialDate.createInstance((Date) mockDate0);
        Date[] dateArray0 = new Date[3];
        dateArray0[0] = (Date) mockDate0;
        double[] doubleArray0 = new double[1];
        DefaultHighLowDataset defaultHighLowDataset0 = new DefaultHighLowDataset(serialDate0, dateArray0, doubleArray0, doubleArray0, doubleArray0, doubleArray0, doubleArray0);
        String string0 = symbolicXYItemLabelGenerator0.generateToolTip(defaultHighLowDataset0, 0, 0);
        assertEquals("X: 102.0, Y: 0.0", string0);
    }
}
