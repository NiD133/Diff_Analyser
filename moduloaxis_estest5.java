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

public class ModuloAxis_ESTestTest5 extends ModuloAxis_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        DefaultValueDataset defaultValueDataset0 = new DefaultValueDataset();
        ThermometerPlot thermometerPlot0 = new ThermometerPlot(defaultValueDataset0);
        Range range0 = thermometerPlot0.getDataRange((ValueAxis) null);
        ModuloAxis moduloAxis0 = new ModuloAxis("", range0);
        moduloAxis0.setDisplayRange(0.0, 10);
        moduloAxis0.resizeRange(1462.0995585, (-2273.979600655645));
        assertEquals(36.5181918443559, moduloAxis0.getDisplayEnd(), 0.01);
    }
}