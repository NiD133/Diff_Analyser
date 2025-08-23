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

public class ModuloAxis_ESTestTest16 extends ModuloAxis_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        DefaultValueDataset defaultValueDataset0 = new DefaultValueDataset();
        ThermometerPlot thermometerPlot0 = new ThermometerPlot(defaultValueDataset0);
        Range range0 = thermometerPlot0.getDataRange((ValueAxis) null);
        ModuloAxis moduloAxis0 = new ModuloAxis("", range0);
        Polygon polygon0 = new Polygon();
        Rectangle rectangle0 = (Rectangle) polygon0.getBounds2D();
        moduloAxis0.setDisplayRange(0.0, 10);
        rectangle0.setBounds(2, 97, 10, 2);
        RectangleEdge rectangleEdge0 = RectangleEdge.BOTTOM;
        double double0 = moduloAxis0.valueToJava2D(2239.4471173664856, rectangle0, rectangleEdge0);
        assertEquals(10.0, moduloAxis0.getDisplayEnd(), 0.01);
        assertEquals(41.4471173664856, double0, 0.01);
    }
}