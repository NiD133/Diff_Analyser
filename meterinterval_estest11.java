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

public class MeterInterval_ESTestTest11 extends MeterInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Range range0 = new Range((-808.678), (-808.678));
        SystemColor systemColor0 = SystemColor.infoText;
        GradientPaint gradientPaint0 = new GradientPaint(657.688F, 657.688F, systemColor0, (-89.9F), (-89.9F), systemColor0, false);
        BasicStroke basicStroke0 = new BasicStroke();
        MeterInterval meterInterval0 = new MeterInterval("k", range0, gradientPaint0, basicStroke0, systemColor0);
        MeterInterval meterInterval1 = new MeterInterval("k", range0, gradientPaint0, basicStroke0, gradientPaint0);
        boolean boolean0 = meterInterval0.equals(meterInterval1);
        assertFalse(boolean0);
    }
}
