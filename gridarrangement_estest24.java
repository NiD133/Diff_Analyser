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

public class GridArrangement_ESTestTest24 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(65, 65);
        assertNotNull(gridArrangement0);
        Range range0 = new Range(65, 65);
        assertEquals(65.0, range0.getUpperBound(), 0.01);
        assertEquals(65.0, range0.getCentralValue(), 0.01);
        assertEquals(65.0, range0.getLowerBound(), 0.01);
        assertEquals("Range[65.0,65.0]", range0.toString());
        assertFalse(range0.isNaNRange());
        assertEquals(0.0, range0.getLength(), 0.01);
        assertNotNull(range0);
        BlockContainer blockContainer0 = new BlockContainer();
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertNotNull(blockContainer0);
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint((-555.02928076), range0);
        assertEquals(65.0, range0.getUpperBound(), 0.01);
        assertEquals(65.0, range0.getCentralValue(), 0.01);
        assertEquals(65.0, range0.getLowerBound(), 0.01);
        assertEquals("Range[65.0,65.0]", range0.toString());
        assertFalse(range0.isNaNRange());
        assertEquals(0.0, range0.getLength(), 0.01);
        assertEquals((-555.02928076), rectangleConstraint0.getWidth(), 0.01);
        assertEquals(LengthConstraintType.RANGE, rectangleConstraint0.getHeightConstraintType());
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getWidthConstraintType());
        assertNotNull(rectangleConstraint0);
        Size2D size2D0 = gridArrangement0.arrangeFR(blockContainer0, (Graphics2D) null, rectangleConstraint0);
        assertEquals(65.0, range0.getUpperBound(), 0.01);
        assertEquals(65.0, range0.getCentralValue(), 0.01);
        assertEquals(65.0, range0.getLowerBound(), 0.01);
        assertEquals("Range[65.0,65.0]", range0.toString());
        assertFalse(range0.isNaNRange());
        assertEquals(0.0, range0.getLength(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertEquals((-555.02928076), rectangleConstraint0.getWidth(), 0.01);
        assertEquals(LengthConstraintType.RANGE, rectangleConstraint0.getHeightConstraintType());
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getWidthConstraintType());
        assertEquals((-555.02928076), size2D0.getWidth(), 0.01);
        assertEquals("Size2D[width=-555.02928076, height=65.0]", size2D0.toString());
        assertEquals(65.0, size2D0.getHeight(), 0.01);
        assertNotNull(size2D0);
        assertEquals(65.0, size2D0.height, 0.01);
        assertEquals((-555.02928076), size2D0.width, 0.01);
    }
}
