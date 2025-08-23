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

public class LegendItemEntity_ESTestTest11 extends LegendItemEntity_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Rectangle2D.Double rectangle2D_Double0 = new Rectangle2D.Double();
        LegendItemEntity<Integer> legendItemEntity0 = new LegendItemEntity<Integer>(rectangle2D_Double0);
        Dataset dataset0 = legendItemEntity0.getDataset();
        assertNull(dataset0);
    }
}
