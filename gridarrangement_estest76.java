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

public class GridArrangement_ESTestTest76 extends GridArrangement_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test75() throws Throwable {
        GridArrangement gridArrangement0 = new GridArrangement(3306, 3306);
        assertNotNull(gridArrangement0);
        LabelBlock labelBlock0 = new LabelBlock("org.jfree.chart.block.FlowArrangement");
        assertEquals(TextBlockAnchor.CENTER, labelBlock0.getContentAlignmentPoint());
        assertEquals(0.0, labelBlock0.getContentXOffset(), 0.01);
        assertNull(labelBlock0.getToolTipText());
        assertEquals(RectangleAnchor.CENTER, labelBlock0.getTextAnchor());
        assertEquals(0.0, labelBlock0.getWidth(), 0.01);
        assertEquals(0.0, labelBlock0.getHeight(), 0.01);
        assertNull(labelBlock0.getID());
        assertEquals(0.0, labelBlock0.getContentYOffset(), 0.01);
        assertNull(labelBlock0.getURLText());
        assertNotNull(labelBlock0);
        gridArrangement0.add(labelBlock0, "org.jfree.chart.block.FlowArrangement");
        assertEquals(TextBlockAnchor.CENTER, labelBlock0.getContentAlignmentPoint());
        assertEquals(0.0, labelBlock0.getContentXOffset(), 0.01);
        assertNull(labelBlock0.getToolTipText());
        assertEquals(RectangleAnchor.CENTER, labelBlock0.getTextAnchor());
        assertEquals(0.0, labelBlock0.getWidth(), 0.01);
        assertEquals(0.0, labelBlock0.getHeight(), 0.01);
        assertNull(labelBlock0.getID());
        assertEquals(0.0, labelBlock0.getContentYOffset(), 0.01);
        assertNull(labelBlock0.getURLText());
    }
}
