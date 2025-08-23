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

public class GridArrangement_ESTestTest50 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(11, 11);
        assertNotNull(gridArrangement0);
        BlockContainer blockContainer0 = new BlockContainer();
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertNotNull(blockContainer0);
        blockContainer0.add((Block) blockContainer0);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertFalse(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        // Undeclared exception!
        try {
            gridArrangement0.arrangeNN(blockContainer0, (Graphics2D) null);
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
