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

public class ModuloAxis_ESTestTest7 extends ModuloAxis_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        DropMode dropMode0 = DropMode.ON_OR_INSERT;
        TimeSeries<DropMode> timeSeries0 = new TimeSeries<DropMode>(dropMode0);
        TimePeriodAnchor timePeriodAnchor0 = TimePeriodAnchor.MIDDLE;
        TimeZone timeZone0 = TimeZone.getDefault();
        MockGregorianCalendar mockGregorianCalendar0 = new MockGregorianCalendar(timeZone0);
        Range range0 = timeSeries0.findValueRange((Range) null, timePeriodAnchor0, (Calendar) mockGregorianCalendar0);
        ModuloAxis moduloAxis0 = new ModuloAxis("", range0);
        Rectangle2D.Double rectangle2D_Double0 = new Rectangle2D.Double();
        RectangleEdge rectangleEdge0 = RectangleEdge.LEFT;
        double double0 = moduloAxis0.valueToJava2D(19, rectangle2D_Double0, rectangleEdge0);
        assertEquals(Double.NaN, double0, 0.01);
        assertEquals(270.0, moduloAxis0.getDisplayStart(), 0.01);
        assertEquals(90.0, moduloAxis0.getDisplayEnd(), 0.01);
    }
}
