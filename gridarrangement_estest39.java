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

public class GridArrangement_ESTestTest39 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(2236, 2236);
        assertNotNull(gridArrangement0);
        BlockContainer blockContainer0 = new BlockContainer();
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertNotNull(blockContainer0);
        blockContainer0.add((Block) blockContainer0);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertFalse(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint(1717.132829611028, 1717.132829611028);
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getWidthConstraintType());
        assertEquals(1717.132829611028, rectangleConstraint0.getWidth(), 0.01);
        assertEquals(1717.132829611028, rectangleConstraint0.getHeight(), 0.01);
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getHeightConstraintType());
        assertNotNull(rectangleConstraint0);
        // Undeclared exception!
        try {
            gridArrangement0.arrangeRN(blockContainer0, (Graphics2D) null, rectangleConstraint0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Not implemented.
            //
            verifyException("org.jfree.chart.block.BorderArrangement", e);
        }
    }
}
