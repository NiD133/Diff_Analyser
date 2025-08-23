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

public class MeterInterval_ESTestTest3 extends MeterInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Range range0 = new Range(0.0, 0.0);
        MeterInterval meterInterval0 = new MeterInterval("|U<d(gc1", range0);
        Range range1 = meterInterval0.getRange();
        assertEquals(0.0, range1.getLowerBound(), 0.01);
    }
}
