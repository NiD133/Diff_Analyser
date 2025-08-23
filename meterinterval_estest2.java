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

public class MeterInterval_ESTestTest2 extends MeterInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Range range0 = new Range((-3041.91489), (-3041.91489));
        Range range1 = Range.shift(range0, 3388.0, true);
        MeterInterval meterInterval0 = new MeterInterval("", range1);
        Range range2 = meterInterval0.getRange();
        assertNotSame(range2, range0);
    }
}
