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

public class LegendItemEntity_ESTestTest2 extends LegendItemEntity_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Rectangle2D.Float rectangle2D_Float0 = new Rectangle2D.Float();
        Rectangle rectangle0 = rectangle2D_Float0.getBounds();
        LegendItemEntity<Integer> legendItemEntity0 = new LegendItemEntity<Integer>(rectangle0);
        DefaultTableXYDataset<Integer> defaultTableXYDataset0 = new DefaultTableXYDataset<Integer>(false);
        legendItemEntity0.setDataset(defaultTableXYDataset0);
        DefaultTableXYDataset defaultTableXYDataset1 = (DefaultTableXYDataset) legendItemEntity0.getDataset();
        assertTrue(defaultTableXYDataset1.getNotify());
    }
}
