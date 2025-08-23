package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.DropMode;
import javax.swing.JScrollPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.util.MockGregorianCalendar;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.legend.PaintScaleLegend;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.jfree.data.time.DateRange;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.junit.runner.RunWith;

public class ModuloAxis_ESTestTest45 extends ModuloAxis_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        DateRange dateRange0 = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis moduloAxis0 = new ModuloAxis("", dateRange0);
        moduloAxis0.resizeRange(1005.89236, 0.13377999998920131);
        Line2D.Float line2D_Float0 = new Line2D.Float();
        Rectangle2D rectangle2D0 = line2D_Float0.getBounds2D();
        RectangleEdge rectangleEdge0 = RectangleEdge.RIGHT;
        double double0 = moduloAxis0.java2DToValue(0.8662200000107987, rectangle2D0, rectangleEdge0);
        assertEquals(0.7675599999784026, moduloAxis0.getUpperBound(), 0.01);
        assertEquals(Double.POSITIVE_INFINITY, double0, 0.01);
    }
}
