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

public class GridArrangement_ESTestTest47 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(105, 105);
        assertNotNull(gridArrangement0);
        Range range0 = new Range(105, 105);
        assertFalse(range0.isNaNRange());
        assertEquals(0.0, range0.getLength(), 0.01);
        assertEquals(105.0, range0.getUpperBound(), 0.01);
        assertEquals(105.0, range0.getLowerBound(), 0.01);
        assertEquals("Range[105.0,105.0]", range0.toString());
        assertEquals(105.0, range0.getCentralValue(), 0.01);
        assertNotNull(range0);
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint(range0, range0);
        assertFalse(range0.isNaNRange());
        assertEquals(0.0, range0.getLength(), 0.01);
        assertEquals(105.0, range0.getUpperBound(), 0.01);
        assertEquals(105.0, range0.getLowerBound(), 0.01);
        assertEquals("Range[105.0,105.0]", range0.toString());
        assertEquals(105.0, range0.getCentralValue(), 0.01);
        assertEquals(0.0, rectangleConstraint0.getWidth(), 0.01);
        assertEquals(LengthConstraintType.RANGE, rectangleConstraint0.getWidthConstraintType());
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertEquals(LengthConstraintType.RANGE, rectangleConstraint0.getHeightConstraintType());
        assertNotNull(rectangleConstraint0);
        BlockContainer blockContainer0 = new BlockContainer();
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertNotNull(blockContainer0);
        BlockContainer blockContainer1 = new BlockContainer();
        assertEquals(0.0, blockContainer1.getHeight(), 0.01);
        assertNull(blockContainer1.getID());
        assertEquals(0.0, blockContainer1.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer1.getContentXOffset(), 0.01);
        assertEquals(0.0, blockContainer1.getWidth(), 0.01);
        assertTrue(blockContainer1.isEmpty());
        assertNotNull(blockContainer1);
        assertTrue(blockContainer1.equals((Object) blockContainer0));
        blockContainer0.add((Block) blockContainer1);
        assertNotSame(blockContainer0, blockContainer1);
        assertNotSame(blockContainer1, blockContainer0);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertFalse(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertEquals(0.0, blockContainer1.getHeight(), 0.01);
        assertNull(blockContainer1.getID());
        assertEquals(0.0, blockContainer1.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer1.getContentXOffset(), 0.01);
        assertEquals(0.0, blockContainer1.getWidth(), 0.01);
        assertTrue(blockContainer1.isEmpty());
        assertFalse(blockContainer0.equals((Object) blockContainer1));
        assertFalse(blockContainer1.equals((Object) blockContainer0));
        // Undeclared exception!
        try {
            gridArrangement0.arrangeNR(blockContainer0, (Graphics2D) null, rectangleConstraint0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Not implemented.
            //
            verifyException("org.jfree.chart.block.BorderArrangement", e);
        }
    }
}