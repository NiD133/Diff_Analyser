package org.jfree.chart.title;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShortTextTitleTestTest2 {

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashcode() {
        ShortTextTitle t1 = new ShortTextTitle("ABC");
        ShortTextTitle t2 = new ShortTextTitle("ABC");
        assertEquals(t1, t2);
        int h1 = t1.hashCode();
        int h2 = t2.hashCode();
        assertEquals(h1, h2);
    }
}
