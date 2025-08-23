package org.jfree.data.xy;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class XYIntervalTestTest1 {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        XYInterval i1 = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);
        XYInterval i2 = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);
        assertEquals(i1, i2);
        i1 = new XYInterval(1.1, 2.0, 3.0, 2.5, 3.5);
        assertNotEquals(i1, i2);
        i2 = new XYInterval(1.1, 2.0, 3.0, 2.5, 3.5);
        assertEquals(i1, i2);
        i1 = new XYInterval(1.1, 2.2, 3.0, 2.5, 3.5);
        assertNotEquals(i1, i2);
        i2 = new XYInterval(1.1, 2.2, 3.0, 2.5, 3.5);
        assertEquals(i1, i2);
        i1 = new XYInterval(1.1, 2.2, 3.3, 2.5, 3.5);
        assertNotEquals(i1, i2);
        i2 = new XYInterval(1.1, 2.2, 3.3, 2.5, 3.5);
        assertEquals(i1, i2);
        i1 = new XYInterval(1.1, 2.2, 3.3, 2.6, 3.5);
        assertNotEquals(i1, i2);
        i2 = new XYInterval(1.1, 2.2, 3.3, 2.6, 3.5);
        assertEquals(i1, i2);
        i1 = new XYInterval(1.1, 2.2, 3.3, 2.6, 3.6);
        assertNotEquals(i1, i2);
        i2 = new XYInterval(1.1, 2.2, 3.3, 2.6, 3.6);
        assertEquals(i1, i2);
    }
}
