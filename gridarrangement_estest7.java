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

public class GridArrangement_ESTestTest7 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(15, 15);
        assertNotNull(gridArrangement0);
        ColumnArrangement columnArrangement0 = new ColumnArrangement();
        assertNotNull(columnArrangement0);
        RectangleConstraint rectangleConstraint0 = RectangleConstraint.NONE;
        assertEquals(LengthConstraintType.NONE, rectangleConstraint0.getHeightConstraintType());
        assertEquals(LengthConstraintType.NONE, rectangleConstraint0.getWidthConstraintType());
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertEquals(0.0, rectangleConstraint0.getWidth(), 0.01);
        assertNotNull(rectangleConstraint0);
        BlockContainer blockContainer0 = new BlockContainer(columnArrangement0);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertNotNull(blockContainer0);
        EmptyBlock emptyBlock0 = new EmptyBlock(15, 1.7976931348623157E308);
        assertNull(emptyBlock0.getID());
        assertEquals(0.0, emptyBlock0.getContentYOffset(), 0.01);
        assertEquals(1.7976931348623157E308, emptyBlock0.getHeight(), 0.01);
        assertEquals(15.0, emptyBlock0.getWidth(), 0.01);
        assertEquals(0.0, emptyBlock0.getContentXOffset(), 0.01);
        assertNotNull(emptyBlock0);
        blockContainer0.add((Block) emptyBlock0, (Object) gridArrangement0);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertFalse(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertNull(emptyBlock0.getID());
        assertEquals(0.0, emptyBlock0.getContentYOffset(), 0.01);
        assertEquals(1.7976931348623157E308, emptyBlock0.getHeight(), 0.01);
        assertEquals(15.0, emptyBlock0.getWidth(), 0.01);
        assertEquals(0.0, emptyBlock0.getContentXOffset(), 0.01);
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
