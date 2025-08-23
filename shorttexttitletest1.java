package org.jfree.chart.title;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShortTextTitleTestTest1 {

    /**
     * Check that the equals() method distinguishes all fields.
     */
    @Test
    public void testEquals() {
        ShortTextTitle t1 = new ShortTextTitle("ABC");
        ShortTextTitle t2 = new ShortTextTitle("ABC");
        assertEquals(t1, t2);
        t1.setText("Test 1");
        assertNotEquals(t1, t2);
        t2.setText("Test 1");
        assertEquals(t1, t2);
    }
}
