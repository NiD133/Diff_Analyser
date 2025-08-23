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

public class GridArrangement_ESTestTest2 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(0, 0);
        assertNotNull(gridArrangement0);
        GridArrangement gridArrangement1 = new GridArrangement(0, (-1901));
        assertNotNull(gridArrangement1);
        assertFalse(gridArrangement1.equals((Object) gridArrangement0));
        boolean boolean0 = gridArrangement1.equals(gridArrangement0);
        assertNotSame(gridArrangement0, gridArrangement1);
        assertNotSame(gridArrangement1, gridArrangement0);
        assertFalse(boolean0);
        assertFalse(gridArrangement0.equals((Object) gridArrangement1));
        assertFalse(gridArrangement1.equals((Object) gridArrangement0));
    }
}
