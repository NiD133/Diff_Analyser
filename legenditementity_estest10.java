package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.time.chrono.HijrahEra;
import javax.swing.JLayeredPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XIntervalSeriesCollection;
import org.junit.runner.RunWith;

public class LegendItemEntity_ESTestTest10 extends LegendItemEntity_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Rectangle2D.Double rectangle2D_Double0 = new Rectangle2D.Double();
        LegendItemEntity<Integer> legendItemEntity0 = new LegendItemEntity<Integer>(rectangle2D_Double0);
        Object object0 = legendItemEntity0.clone();
        boolean boolean0 = legendItemEntity0.equals(object0);
        assertTrue(boolean0);
    }
}
