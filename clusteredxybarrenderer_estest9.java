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

public class ClusteredXYBarRenderer_ESTestTest9 extends ClusteredXYBarRenderer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        ClusteredXYBarRenderer clusteredXYBarRenderer0 = new ClusteredXYBarRenderer();
        OHLCDataItem[] oHLCDataItemArray0 = new OHLCDataItem[5];
        Locale locale0 = new Locale("}y&~*+ |^", " q~^A)mz{I%kS}vYl\"h");
        DateFormatSymbols dateFormatSymbols0 = DateFormatSymbols.getInstance(locale0);
        MockSimpleDateFormat mockSimpleDateFormat0 = new MockSimpleDateFormat("", dateFormatSymbols0);
        Date date0 = mockSimpleDateFormat0.get2DigitYearStart();
        OHLCDataItem oHLCDataItem0 = new OHLCDataItem(date0, 1641.62203, 1376.0317, 1376.0317, 1641.62203, 597.5355594430192);
        oHLCDataItemArray0[0] = oHLCDataItem0;
        oHLCDataItemArray0[1] = oHLCDataItemArray0[0];
        oHLCDataItemArray0[2] = oHLCDataItemArray0[1];
        oHLCDataItemArray0[3] = oHLCDataItemArray0[1];
        oHLCDataItemArray0[4] = oHLCDataItemArray0[0];
        DefaultOHLCDataset defaultOHLCDataset0 = new DefaultOHLCDataset(clusteredXYBarRenderer0.ZERO, oHLCDataItemArray0);
        Range range0 = clusteredXYBarRenderer0.findDomainBounds(defaultOHLCDataset0);
        assertEquals(1.39240928132E12, range0.getLowerBound(), 0.01);
    }
}
