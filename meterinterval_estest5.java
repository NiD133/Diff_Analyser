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

public class MeterInterval_ESTestTest5 extends MeterInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Range range0 = new Range((-229.41607020985958), (-229.41607020985958));
        Color color0 = Color.gray;
        MeterInterval meterInterval0 = new MeterInterval(",", range0, color0, (Stroke) null, color0);
        Stroke stroke0 = meterInterval0.getOutlineStroke();
        assertNull(stroke0);
    }
}
