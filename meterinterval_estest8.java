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

public class MeterInterval_ESTestTest8 extends MeterInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        DefaultBoxAndWhiskerCategoryDataset<HistogramType, HistogramType> defaultBoxAndWhiskerCategoryDataset0 = new DefaultBoxAndWhiskerCategoryDataset<HistogramType, HistogramType>();
        Range range0 = defaultBoxAndWhiskerCategoryDataset0.getRangeBounds(true);
        Color color0 = Color.darkGray;
        BasicStroke basicStroke0 = new BasicStroke(0.0F);
        MeterInterval meterInterval0 = new MeterInterval("L5-", range0, color0, basicStroke0, color0);
        Color color1 = (Color) meterInterval0.getBackgroundPaint();
        assertEquals(64, color1.getBlue());
    }
}
