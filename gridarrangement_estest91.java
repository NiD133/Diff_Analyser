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

public class GridArrangement_ESTestTest91 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test90() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(105, 105);
        BlockContainer blockContainer0 = new BlockContainer();
        Range range0 = new Range(105, 105);
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint(range0, range0);
        SystemColor systemColor0 = SystemColor.activeCaption;
        ColorBlock colorBlock0 = new ColorBlock(systemColor0, 0.0, Double.NEGATIVE_INFINITY);
        blockContainer0.add((Block) colorBlock0);
        Size2D size2D0 = gridArrangement0.arrangeRF(blockContainer0, (Graphics2D) null, rectangleConstraint0);
        assertEquals("Size2D[width=105.0, height=105.0]", size2D0.toString());
    }
}
