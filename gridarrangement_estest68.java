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

public class GridArrangement_ESTestTest68 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test67() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(305, 305);
        assertNotNull(gridArrangement0);
        BlockContainer blockContainer0 = new BlockContainer();
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertNotNull(blockContainer0);
        blockContainer0.add((Block) blockContainer0);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertFalse(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        Range range0 = new Range(305, 305);
        assertEquals(0.0, range0.getLength(), 0.01);
        assertEquals(305.0, range0.getLowerBound(), 0.01);
        assertEquals(305.0, range0.getCentralValue(), 0.01);
        assertEquals(305.0, range0.getUpperBound(), 0.01);
        assertFalse(range0.isNaNRange());
        assertEquals("Range[305.0,305.0]", range0.toString());
        assertNotNull(range0);
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint(range0, 305);
        assertEquals(0.0, range0.getLength(), 0.01);
        assertEquals(305.0, range0.getLowerBound(), 0.01);
        assertEquals(305.0, range0.getCentralValue(), 0.01);
        assertEquals(305.0, range0.getUpperBound(), 0.01);
        assertFalse(range0.isNaNRange());
        assertEquals("Range[305.0,305.0]", range0.toString());
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getHeightConstraintType());
        assertEquals(0.0, rectangleConstraint0.getWidth(), 0.01);
        assertEquals(LengthConstraintType.RANGE, rectangleConstraint0.getWidthConstraintType());
        assertEquals(305.0, rectangleConstraint0.getHeight(), 0.01);
        assertNotNull(rectangleConstraint0);
        // Undeclared exception!
        try {
            gridArrangement0.arrange(blockContainer0, (Graphics2D) null, rectangleConstraint0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Not implemented.
            //
            verifyException("org.jfree.chart.block.BorderArrangement", e);
        }
    }
}
