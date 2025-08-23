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

public class ModuloAxis_ESTestTest9 extends ModuloAxis_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Range range0 = new Range(0.043157868280104594, 946.7277932617);
        ModuloAxis moduloAxis0 = new ModuloAxis("", range0);
        moduloAxis0.setInverted(true);
        Rectangle2D.Double rectangle2D_Double0 = new Rectangle2D.Double();
        RectangleEdge rectangleEdge0 = RectangleEdge.TOP;
        double double0 = moduloAxis0.valueToJava2D(2.0, rectangle2D_Double0, rectangleEdge0);
        assertEquals(0.0, double0, 0.01);
        assertEquals(270.0, moduloAxis0.getDisplayStart(), 0.01);
    }
}
