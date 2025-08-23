package org.jfree.chart.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MeterIntervalTestTest1 {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        MeterInterval m1 = new MeterInterval("Label 1", new Range(1.2, 3.4), Color.RED, new BasicStroke(1.0f), Color.BLUE);
        MeterInterval m2 = new MeterInterval("Label 1", new Range(1.2, 3.4), Color.RED, new BasicStroke(1.0f), Color.BLUE);
        assertEquals(m1, m2);
        assertEquals(m2, m1);
        m1 = new MeterInterval("Label 2", new Range(1.2, 3.4), Color.RED, new BasicStroke(1.0f), Color.BLUE);
        assertNotEquals(m1, m2);
        m2 = new MeterInterval("Label 2", new Range(1.2, 3.4), Color.RED, new BasicStroke(1.0f), Color.BLUE);
        assertEquals(m1, m2);
    }
}
