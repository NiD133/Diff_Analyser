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

public class GridArrangement_ESTestTest4 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(4, (-649));
        assertNotNull(gridArrangement0);
        CenterArrangement centerArrangement0 = new CenterArrangement();
        assertNotNull(centerArrangement0);
        BlockContainer blockContainer0 = new BlockContainer(centerArrangement0);
        assertTrue(blockContainer0.isEmpty());
        assertEquals(0.0, blockContainer0.getContentXOffset(), 0.01);
        assertNull(blockContainer0.getID());
        assertEquals(0.0, blockContainer0.getContentYOffset(), 0.01);
        assertEquals(0.0, blockContainer0.getWidth(), 0.01);
        assertEquals(0.0, blockContainer0.getHeight(), 0.01);
        assertNotNull(blockContainer0);
        ChronoUnit chronoUnit0 = ChronoUnit.MONTHS;
        TimeSeries<ChronoUnit> timeSeries0 = new TimeSeries<ChronoUnit>(chronoUnit0);
        assertEquals(Integer.MAX_VALUE, timeSeries0.getMaximumItemCount());
        assertEquals(9223372036854775807L, timeSeries0.getMaximumItemAge());
        assertEquals(Double.NaN, timeSeries0.getMaxY(), 0.01);
        assertEquals(0, timeSeries0.getItemCount());
        assertTrue(timeSeries0.getNotify());
        assertEquals(Double.NaN, timeSeries0.getMinY(), 0.01);
        assertNotNull(timeSeries0);
        TimePeriodAnchor timePeriodAnchor0 = TimePeriodAnchor.START;
        MockGregorianCalendar mockGregorianCalendar0 = new MockGregorianCalendar((-827), (-649), (-649), 1, 39, (-320));
        assertNotNull(mockGregorianCalendar0);
        Range range0 = timeSeries0.findValueRange((Range) null, timePeriodAnchor0, (Calendar) mockGregorianCalendar0);
        assertEquals(Integer.MAX_VALUE, timeSeries0.getMaximumItemCount());
        assertEquals(9223372036854775807L, timeSeries0.getMaximumItemAge());
        assertEquals(Double.NaN, timeSeries0.getMaxY(), 0.01);
        assertEquals(0, timeSeries0.getItemCount());
        assertTrue(timeSeries0.getNotify());
        assertEquals(Double.NaN, timeSeries0.getMinY(), 0.01);
        assertTrue(mockGregorianCalendar0.isLenient());
        assertEquals(1, mockGregorianCalendar0.getFirstDayOfWeek());
        assertEquals(1, mockGregorianCalendar0.getMinimalDaysInFirstWeek());
        assertEquals("org.evosuite.runtime.mock.java.util.MockGregorianCalendar[time=?,areFieldsSet=false,areAllFieldsSet=false,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"GMT\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=?,YEAR=-827,MONTH=-649,WEEK_OF_YEAR=?,WEEK_OF_MONTH=?,DAY_OF_MONTH=-649,DAY_OF_YEAR=?,DAY_OF_WEEK=?,DAY_OF_WEEK_IN_MONTH=?,AM_PM=0,HOUR=1,HOUR_OF_DAY=1,MINUTE=39,SECOND=-320,MILLISECOND=?,ZONE_OFFSET=?,DST_OFFSET=?]", mockGregorianCalendar0.toString());
        assertEquals(Double.NaN, range0.getCentralValue(), 0.01);
        assertEquals(Double.NaN, range0.getUpperBound(), 0.01);
        assertEquals(Double.NaN, range0.getLowerBound(), 0.01);
        assertEquals(Double.NaN, range0.getLength(), 0.01);
        assertTrue(range0.isNaNRange());
        assertEquals("Range[NaN,NaN]", range0.toString());
        assertNotNull(range0);
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint(0.0, range0);
        assertEquals(Integer.MAX_VALUE, timeSeries0.getMaximumItemCount());
        assertEquals(9223372036854775807L, timeSeries0.getMaximumItemAge());
        assertEquals(Double.NaN, timeSeries0.getMaxY(), 0.01);
        assertEquals(0, timeSeries0.getItemCount());
        assertTrue(timeSeries0.getNotify());
        assertEquals(Double.NaN, timeSeries0.getMinY(), 0.01);
        assertTrue(mockGregorianCalendar0.isLenient());
        assertEquals(1, mockGregorianCalendar0.getFirstDayOfWeek());
        assertEquals(1, mockGregorianCalendar0.getMinimalDaysInFirstWeek());
        assertEquals("org.evosuite.runtime.mock.java.util.MockGregorianCalendar[time=?,areFieldsSet=false,areAllFieldsSet=false,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"GMT\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=?,YEAR=-827,MONTH=-649,WEEK_OF_YEAR=?,WEEK_OF_MONTH=?,DAY_OF_MONTH=-649,DAY_OF_YEAR=?,DAY_OF_WEEK=?,DAY_OF_WEEK_IN_MONTH=?,AM_PM=0,HOUR=1,HOUR_OF_DAY=1,MINUTE=39,SECOND=-320,MILLISECOND=?,ZONE_OFFSET=?,DST_OFFSET=?]", mockGregorianCalendar0.toString());
        assertEquals(Double.NaN, range0.getCentralValue(), 0.01);
        assertEquals(Double.NaN, range0.getUpperBound(), 0.01);
        assertEquals(Double.NaN, range0.getLowerBound(), 0.01);
        assertEquals(Double.NaN, range0.getLength(), 0.01);
        assertTrue(range0.isNaNRange());
        assertEquals("Range[NaN,NaN]", range0.toString());
        assertEquals(LengthConstraintType.FIXED, rectangleConstraint0.getWidthConstraintType());
        assertEquals(0.0, rectangleConstraint0.getHeight(), 0.01);
        assertEquals(0.0, rectangleConstraint0.getWidth(), 0.01);
        assertEquals(LengthConstraintType.RANGE, rectangleConstraint0.getHeightConstraintType());
        assertNotNull(rectangleConstraint0);
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
