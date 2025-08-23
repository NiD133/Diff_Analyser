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

public class ModuloAxis_ESTestTest25 extends ModuloAxis_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Range range0 = new Range((-3022.7454), (-2241.3964950581735));
        ModuloAxis moduloAxis0 = new ModuloAxis("lFFFf+.F<AAFi", range0);
        moduloAxis0.resizeRange(3737.437, 2.0);
        double double0 = moduloAxis0.getDisplayEnd();
        assertEquals((-2169.9484016396004), moduloAxis0.getUpperBound(), 0.01);
        assertEquals((-2951.2973065814267), double0, 0.01);
    }
}
