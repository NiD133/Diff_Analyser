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

public class GridArrangement_ESTestTest90 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test89() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(105, 105);
        BlockContainer blockContainer0 = new BlockContainer();
        blockContainer0.add((Block) null);
        RectangleConstraint rectangleConstraint0 = RectangleConstraint.NONE;
        // Undeclared exception!
        try {
            gridArrangement0.arrangeRF(blockContainer0, (Graphics2D) null, rectangleConstraint0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.block.GridArrangement", e);
        }
    }
}
