package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.util.MockGregorianCalendar;
import org.jfree.chart.api.HorizontalAlignment;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.api.VerticalAlignment;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.data.Range;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.junit.runner.RunWith;

public class GridArrangement_ESTestTest89 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test88() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(15, 15);
        Range range0 = new Range((-2250.587925944), 15);
        BlockContainer blockContainer0 = new BlockContainer();
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint(range0, range0);
        Size2D size2D0 = gridArrangement0.arrangeNF(blockContainer0, (Graphics2D) null, rectangleConstraint0);
        assertEquals("Size2D[width=0.0, height=0.0]", size2D0.toString());
    }
}
