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

public class ClusteredXYBarRenderer_ESTestTest16 extends ClusteredXYBarRenderer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        CombinedRangeXYPlot<Short> combinedRangeXYPlot0 = new CombinedRangeXYPlot<Short>();
        Rectangle2D.Double rectangle2D_Double0 = new Rectangle2D.Double();
        PlotRenderingInfo plotRenderingInfo0 = new PlotRenderingInfo((ChartRenderingInfo) null);
        CategoryCrosshairState<Short, Short> categoryCrosshairState0 = new CategoryCrosshairState<Short, Short>();
        ClusteredXYBarRenderer clusteredXYBarRenderer0 = new ClusteredXYBarRenderer(2, false);
        CyclicNumberAxis cyclicNumberAxis0 = new CyclicNumberAxis(0.0, 0.0);
        DefaultXYZDataset<Short> defaultXYZDataset0 = new DefaultXYZDataset<Short>();
        // Undeclared exception!
        try {
            clusteredXYBarRenderer0.drawItem((Graphics2D) null, (XYItemRendererState) null, rectangle2D_Double0, plotRenderingInfo0, combinedRangeXYPlot0, cyclicNumberAxis0, cyclicNumberAxis0, defaultXYZDataset0, 2, (-3493), categoryCrosshairState0, 2);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // class org.jfree.data.xy.DefaultXYZDataset cannot be cast to class org.jfree.data.xy.IntervalXYDataset (org.jfree.data.xy.DefaultXYZDataset and org.jfree.data.xy.IntervalXYDataset are in unnamed module of loader org.evosuite.instrumentation.InstrumentingClassLoader @9a99bfa)
            //
            verifyException("org.jfree.chart.renderer.xy.ClusteredXYBarRenderer", e);
        }
    }
}
