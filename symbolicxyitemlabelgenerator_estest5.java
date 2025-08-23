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

public class SymbolicXYItemLabelGenerator_ESTestTest5 extends SymbolicXYItemLabelGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test4() throws Throwable {
        SymbolicXYItemLabelGenerator symbolicXYItemLabelGenerator0 = new SymbolicXYItemLabelGenerator();
        DynamicTimeSeriesCollection dynamicTimeSeriesCollection0 = new DynamicTimeSeriesCollection(1970, 1970);
        // Undeclared exception!
        try {
            symbolicXYItemLabelGenerator0.generateToolTip(dynamicTimeSeriesCollection0, 1970, 2);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // Index 1970 out of bounds for length 1970
            //
            verifyException("org.jfree.data.time.DynamicTimeSeriesCollection", e);
        }
    }
}
