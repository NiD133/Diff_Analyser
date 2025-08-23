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

public class GridArrangement_ESTestTest16 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        ColumnArrangement columnArrangement0 = new ColumnArrangement();
        assertNotNull(columnArrangement0);
        BlockContainer blockContainer0 = new BlockContainer(columnArrangement0);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertNotNull(blockContainer0);
        Range range0 = new Range(0.0, 45.53941763);
        assertEquals("Range[0.0,45.53941763]", range0.toString());
        assertFalse(range0.isNaNRange());
        assertEquals(22.769708815, range0.getCentralValue(), 0.01);
        assertEquals(45.53941763, range0.getUpperBound(), 0.01);
        assertEquals(45.53941763, range0.getLength(), 0.01);
        assertEquals(0.0, range0.getLowerBound(), 0.01);
        assertNotNull(range0);
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint(3506.7306618706652, range0);
        assertEquals("Range[0.0,45.53941763]", range0.toString());
        assertFalse(range0.isNaNRange());
        assertEquals(22.769708815, range0.getCentralValue(), 0.01);
        assertEquals(45.53941763, range0.getUpperBound(), 0.01);
        assertEquals(45.53941763, range0.getLength(), 0.01);
        assertEquals(0.0, range0.getLowerBound(), 0.01);
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertEquals(LengthConstraintType.RANGE, rectangleConstraint0.getHeightConstraintType());
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getWidthConstraintType());
        assertEquals(3506.7306618706652, rectangleConstraint0.getWidth(), 0.01);
        assertNotNull(rectangleConstraint0);
        GridArrangement gridArrangement0 = new GridArrangement(30, 1328);
        assertNotNull(gridArrangement0);
        Size2D size2D0 = gridArrangement0.arrangeNR(blockContainer0, (Graphics2D) null, rectangleConstraint0);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertTrue(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertEquals("Range[0.0,45.53941763]", range0.toString());
        assertFalse(range0.isNaNRange());
        assertEquals(22.769708815, range0.getCentralValue(), 0.01);
        assertEquals(45.53941763, range0.getUpperBound(), 0.01);
        assertEquals(45.53941763, range0.getLength(), 0.01);
        assertEquals(0.0, range0.getLowerBound(), 0.01);
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertEquals(LengthConstraintType.RANGE, rectangleConstraint0.getHeightConstraintType());
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getWidthConstraintType());
        assertEquals(3506.7306618706652, rectangleConstraint0.getWidth(), 0.01);
        assertEquals("Size2D[width=3506.730661870665, height=0.0]", size2D0.toString());
        assertEquals(3506.730661870665, size2D0.getWidth(), 0.01);
        assertEquals(0.0, size2D0.getHeight(), 0.01);
        assertNotNull(size2D0);
        assertEquals(3506.730661870665, size2D0.width, 0.01);
        assertEquals(0.0, size2D0.height, 0.01);
    }
}
