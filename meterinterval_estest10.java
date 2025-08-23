package org.jfree.chart.plot;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.SystemColor;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.data.Range;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.HistogramType;
import org.junit.runner.RunWith;

public class MeterInterval_ESTestTest10 extends MeterInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        MeterInterval meterInterval0 = null;
        try {
            meterInterval0 = new MeterInterval("9+XJTk=~|", (Range) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null 'range' argument.
            //
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }
}
