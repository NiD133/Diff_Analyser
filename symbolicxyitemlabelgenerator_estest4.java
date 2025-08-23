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

public class SymbolicXYItemLabelGenerator_ESTestTest4 extends SymbolicXYItemLabelGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test3() throws Throwable {
        SymbolicXYItemLabelGenerator symbolicXYItemLabelGenerator0 = new SymbolicXYItemLabelGenerator();
        // Undeclared exception!
        try {
            symbolicXYItemLabelGenerator0.generateToolTip((XYDataset) null, 44, 44);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null 'dataset' argument.
            //
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }
}
