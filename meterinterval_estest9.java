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

public class MeterInterval_ESTestTest9 extends MeterInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        SystemColor systemColor0 = SystemColor.controlHighlight;
        BasicStroke basicStroke0 = new BasicStroke();
        MeterInterval meterInterval0 = null;
        try {
            meterInterval0 = new MeterInterval("org.jfree.chart.plot.MeterInterval", (Range) null, systemColor0, basicStroke0, systemColor0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null 'range' argument.
            //
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }
}
