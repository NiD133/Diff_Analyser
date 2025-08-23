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

public class GridArrangement_ESTestTest87 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test86() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(2878, 2878);
        Range range0 = new Range(2878, 2878);
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint(range0, range0);
        BlockContainer blockContainer0 = new BlockContainer();
        // Undeclared exception!
        gridArrangement0.arrangeFR(blockContainer0, (Graphics2D) null, rectangleConstraint0);
    }
}
