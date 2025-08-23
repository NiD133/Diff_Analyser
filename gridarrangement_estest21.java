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

public class GridArrangement_ESTestTest21 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(0, 0);
        assertNotNull(gridArrangement0);
        ColumnArrangement columnArrangement0 = new ColumnArrangement();
        assertNotNull(columnArrangement0);
        BlockContainer blockContainer0 = new BlockContainer(columnArrangement0);
        assertTrue(blockContainer0.isEmpty());
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertNotNull(blockContainer0);
        RectangleConstraint rectangleConstraint0 = RectangleConstraint.NONE;
        assertEquals(LengthConstraintType.NONE, rectangleConstraint0.getWidthConstraintType());
        assertEquals(0.0, rectangleConstraint0.getWidth(), 0.01);
        assertEquals(LengthConstraintType.NONE, rectangleConstraint0.getHeightConstraintType());
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertNotNull(rectangleConstraint0);
        Size2D size2D0 = gridArrangement0.arrangeNF(blockContainer0, (Graphics2D) null, rectangleConstraint0);
        assertTrue(blockContainer0.isEmpty());
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertEquals(LengthConstraintType.NONE, rectangleConstraint0.getWidthConstraintType());
        assertEquals(0.0, rectangleConstraint0.getWidth(), 0.01);
        assertEquals(LengthConstraintType.NONE, rectangleConstraint0.getHeightConstraintType());
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertEquals("Size2D[width=NaN, height=NaN]", size2D0.toString());
        assertEquals(Double.NaN, size2D0.getWidth(), 0.01);
        assertEquals(Double.NaN, size2D0.getHeight(), 0.01);
        assertNotNull(size2D0);
        assertEquals(Double.NaN, size2D0.width, 0.01);
        assertEquals(Double.NaN, size2D0.height, 0.01);
    }
}
