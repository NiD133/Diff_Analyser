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

public class GridArrangement_ESTestTest52 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test51() throws Throwable {
        BlockContainer blockContainer0 = new BlockContainer();
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertNotNull(blockContainer0);
        CenterArrangement centerArrangement0 = new CenterArrangement();
        assertNotNull(centerArrangement0);
        BlockContainer blockContainer1 = new BlockContainer(centerArrangement0);
        assertEquals(0.0, blockContainer1.getHeight(), 0.01);
        assertEquals(0.0, blockContainer1.getContentXOffset(), 0.01);
        assertTrue(blockContainer1.isEmpty());
        assertNull(blockContainer1.getID());
        assertEquals(0.0, blockContainer1.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer1.getWidth(), 0.01);
        assertNotNull(blockContainer1);
        assertFalse(blockContainer1.equals((Object) blockContainer0));
        blockContainer0.add((Block) blockContainer1);
        assertNotSame(blockContainer0, blockContainer1);
        assertNotSame(blockContainer1, blockContainer0);
        assertFalse(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer1.getHeight(), 0.01);
        assertEquals(0.0, blockContainer1.getContentXOffset(), 0.01);
        assertTrue(blockContainer1.isEmpty());
        assertNull(blockContainer1.getID());
        assertEquals(0.0, blockContainer1.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer1.getWidth(), 0.01);
        assertFalse(blockContainer0.equals((Object) blockContainer1));
        assertFalse(blockContainer1.equals((Object) blockContainer0));
        GridArrangement gridArrangement0 = new GridArrangement((-973), (-973));
        assertNotNull(gridArrangement0);
        // Undeclared exception!
        try {
            gridArrangement0.arrangeNN(blockContainer0, (Graphics2D) null);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
    }
}
