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

public class GridArrangement_ESTestTest10 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(15, 15);
        assertNotNull(gridArrangement0);
        BlockContainer blockContainer0 = new BlockContainer();
        assertTrue(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertNotNull(blockContainer0);
        Range range0 = new Range((-3367.341901), 219.4320566834);
        assertEquals(3586.7739576833997, range0.getLength(), 0.01);
        assertEquals(219.4320566834, range0.getUpperBound(), 0.01);
        assertEquals((-3367.341901), range0.getLowerBound(), 0.01);
        assertFalse(range0.isNaNRange());
        assertEquals("Range[-3367.341901,219.4320566834]", range0.toString());
        assertEquals((-1573.9549221583), range0.getCentralValue(), 0.01);
        assertNotNull(range0);
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint(range0, range0);
        assertEquals(3586.7739576833997, range0.getLength(), 0.01);
        assertEquals(219.4320566834, range0.getUpperBound(), 0.01);
        assertEquals((-3367.341901), range0.getLowerBound(), 0.01);
        assertFalse(range0.isNaNRange());
        assertEquals("Range[-3367.341901,219.4320566834]", range0.toString());
        assertEquals((-1573.9549221583), range0.getCentralValue(), 0.01);
        assertEquals(LengthConstraintType.RANGE, rectangleConstraint0.getHeightConstraintType());
        assertEquals(0.0, rectangleConstraint0.getWidth(), 0.01);
        assertEquals(LengthConstraintType.RANGE, rectangleConstraint0.getWidthConstraintType());
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertNotNull(rectangleConstraint0);
        Size2D size2D0 = gridArrangement0.arrangeRR(blockContainer0, (Graphics2D) null, rectangleConstraint0);
        assertTrue(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertEquals(3586.7739576833997, range0.getLength(), 0.01);
        assertEquals(219.4320566834, range0.getUpperBound(), 0.01);
        assertEquals((-3367.341901), range0.getLowerBound(), 0.01);
        assertFalse(range0.isNaNRange());
        assertEquals("Range[-3367.341901,219.4320566834]", range0.toString());
        assertEquals((-1573.9549221583), range0.getCentralValue(), 0.01);
        assertEquals(LengthConstraintType.RANGE, rectangleConstraint0.getHeightConstraintType());
        assertEquals(0.0, rectangleConstraint0.getWidth(), 0.01);
        assertEquals(LengthConstraintType.RANGE, rectangleConstraint0.getWidthConstraintType());
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertEquals(0.0, size2D0.getHeight(), 0.01);
        assertEquals("Size2D[width=0.0, height=0.0]", size2D0.toString());
        assertEquals(0.0, size2D0.getWidth(), 0.01);
        assertNotNull(size2D0);
        assertEquals(0.0, size2D0.width, 0.01);
        assertEquals(0.0, size2D0.height, 0.01);
    }
}
