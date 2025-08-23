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

public class MeterInterval_ESTestTest1 extends MeterInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        DefaultBoxAndWhiskerCategoryDataset<HistogramType, HistogramType> defaultBoxAndWhiskerCategoryDataset0 = new DefaultBoxAndWhiskerCategoryDataset<HistogramType, HistogramType>();
        Range range0 = defaultBoxAndWhiskerCategoryDataset0.getRangeBounds(false);
        MeterInterval meterInterval0 = new MeterInterval("Px$=FZ)/l);", range0);
        Range range1 = meterInterval0.getRange();
        assertEquals(Double.NaN, range1.getUpperBound(), 0.01);
    }
}
