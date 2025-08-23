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

public class GridArrangement_ESTestTest33 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(81, 81);
        assertNotNull(gridArrangement0);
        BlockContainer blockContainer0 = new BlockContainer();
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertNotNull(blockContainer0);
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint(81, (-2677.7593688945217));
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getWidthConstraintType());
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getHeightConstraintType());
        assertEquals(81.0, rectangleConstraint0.getWidth(), 0.01);
        assertEquals((-2677.7593688945217), rectangleConstraint0.getHeight(), 0.01);
        assertNotNull(rectangleConstraint0);
        Size2D size2D0 = gridArrangement0.arrangeFF(blockContainer0, (Graphics2D) null, rectangleConstraint0);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getWidthConstraintType());
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getHeightConstraintType());
        assertEquals(81.0, rectangleConstraint0.getWidth(), 0.01);
        assertEquals((-2677.7593688945217), rectangleConstraint0.getHeight(), 0.01);
        assertEquals((-2677.7593688945217), size2D0.getHeight(), 0.01);
        assertEquals("Size2D[width=81.0, height=-2677.7593688945217]", size2D0.toString());
        assertEquals(81.0, size2D0.getWidth(), 0.01);
        assertNotNull(size2D0);
        assertEquals((-2677.7593688945217), size2D0.height, 0.01);
        assertEquals(81.0, size2D0.width, 0.01);
    }
}
