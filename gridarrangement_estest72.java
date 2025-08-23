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

public class GridArrangement_ESTestTest72 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test71() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(0, (-827));
        assertNotNull(gridArrangement0);
        BlockContainer blockContainer0 = new BlockContainer(gridArrangement0);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertNotNull(blockContainer0);
        LengthConstraintType lengthConstraintType0 = LengthConstraintType.NONE;
        LengthConstraintType lengthConstraintType1 = LengthConstraintType.FIXED;
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint((-827), (Range) null, lengthConstraintType1, 0, (Range) null, lengthConstraintType0);
        assertEquals(LengthConstraintType.NONE, rectangleConstraint0.getHeightConstraintType());
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getWidthConstraintType());
        assertEquals((-827.0), rectangleConstraint0.getWidth(), 0.01);
        assertNotNull(rectangleConstraint0);
        assertFalse(lengthConstraintType0.equals((Object) lengthConstraintType1));
        assertFalse(lengthConstraintType1.equals((Object) lengthConstraintType0));
        Size2D size2D0 = gridArrangement0.arrange(blockContainer0, (Graphics2D) null, rectangleConstraint0);
        assertNotSame(lengthConstraintType0, lengthConstraintType1);
        assertNotSame(lengthConstraintType1, lengthConstraintType0);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(LengthConstraintType.NONE, rectangleConstraint0.getHeightConstraintType());
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getWidthConstraintType());
        assertEquals((-827.0), rectangleConstraint0.getWidth(), 0.01);
        assertEquals((-827.0), size2D0.getWidth(), 0.01);
        assertEquals("Size2D[width=-827.0, height=NaN]", size2D0.toString());
        assertEquals(Double.NaN, size2D0.getHeight(), 0.01);
        assertNotNull(size2D0);
        assertFalse(lengthConstraintType0.equals((Object) lengthConstraintType1));
        assertFalse(lengthConstraintType1.equals((Object) lengthConstraintType0));
        assertEquals(Double.NaN, size2D0.height, 0.01);
        assertEquals((-827.0), size2D0.width, 0.01);
    }
}
