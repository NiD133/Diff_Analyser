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

public class MeterInterval_ESTestTest15 extends MeterInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Range range0 = new Range((-229.41607020985958), (-229.41607020985958));
        MeterInterval meterInterval0 = new MeterInterval("d|V&g", range0);
        MeterInterval meterInterval1 = new MeterInterval("U`/UA", range0);
        boolean boolean0 = meterInterval1.equals(meterInterval0);
        assertFalse(boolean0);
    }
}
