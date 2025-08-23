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

public class ModuloAxis_ESTestTest39 extends ModuloAxis_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        Range range0 = ValueAxis.DEFAULT_RANGE;
        ModuloAxis moduloAxis0 = new ModuloAxis("", range0);
        JScrollPane jScrollPane0 = new JScrollPane();
        Rectangle rectangle0 = jScrollPane0.getViewportBorderBounds();
        RectangleEdge rectangleEdge0 = RectangleEdge.RIGHT;
        double double0 = moduloAxis0.lengthToJava2D(1.0E-8, rectangle0, rectangleEdge0);
        assertEquals(1.675977653631285E-10, double0, 0.01);
    }
}
