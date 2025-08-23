package org.jfree.data;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.fail;

public class ComparableObjectItemTestTest5 {

    /**
     * Some checks for the compareTo() method.
     */
    @Test
    public void testCompareTo() {
        ComparableObjectItem item1 = new ComparableObjectItem(1, "XYZ");
        ComparableObjectItem item2 = new ComparableObjectItem(2, "XYZ");
        ComparableObjectItem item3 = new ComparableObjectItem(3, "XYZ");
        ComparableObjectItem item4 = new ComparableObjectItem(1, "XYZ");
        assertTrue(item2.compareTo(item1) > 0);
        assertTrue(item3.compareTo(item1) > 0);
        assertEquals(0, item4.compareTo(item1));
        assertTrue(item1.compareTo(item2) < 0);
    }
}
