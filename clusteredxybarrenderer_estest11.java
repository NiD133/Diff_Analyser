package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.Locale;
import javax.swing.JLayeredPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.text.MockSimpleDateFormat;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.CyclicNumberAxis;
import org.jfree.chart.plot.CategoryCrosshairState;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.util.DirectionalGradientPaintTransformer;
import org.jfree.data.Range;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.DefaultWindDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.runner.RunWith;

public class ClusteredXYBarRenderer_ESTestTest11 extends ClusteredXYBarRenderer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        ClusteredXYBarRenderer clusteredXYBarRenderer0 = new ClusteredXYBarRenderer();
        Integer integer0 = JLayeredPane.DEFAULT_LAYER;
        SimpleHistogramDataset<Integer> simpleHistogramDataset0 = new SimpleHistogramDataset<Integer>(integer0);
        simpleHistogramDataset0.addBin((SimpleHistogramBin) null);
        // Undeclared exception!
        try {
            clusteredXYBarRenderer0.findDomainBoundsWithOffset(simpleHistogramDataset0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.data.statistics.SimpleHistogramDataset", e);
        }
    }
}
