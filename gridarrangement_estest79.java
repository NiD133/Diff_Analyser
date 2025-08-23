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

public class GridArrangement_ESTestTest79 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test78() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(0, 0);
        assertNotNull(gridArrangement0);
        GridArrangement gridArrangement1 = new GridArrangement(0, 0);
        assertNotNull(gridArrangement1);
        assertTrue(gridArrangement1.equals((Object) gridArrangement0));
        boolean boolean0 = gridArrangement0.equals(gridArrangement1);
        assertNotSame(gridArrangement0, gridArrangement1);
        assertNotSame(gridArrangement1, gridArrangement0);
        assertTrue(boolean0);
        assertTrue(gridArrangement0.equals((Object) gridArrangement1));
        assertTrue(gridArrangement1.equals((Object) gridArrangement0));
    }
}
