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

public class ComparableObjectItemTestTest2 {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        ComparableObjectItem item1 = new ComparableObjectItem(1, "XYZ");
        ComparableObjectItem item2 = new ComparableObjectItem(1, "XYZ");
        assertEquals(item1, item2);
        item1 = new ComparableObjectItem(2, "XYZ");
        assertNotEquals(item1, item2);
        item2 = new ComparableObjectItem(2, "XYZ");
        assertEquals(item1, item2);
        item1 = new ComparableObjectItem(2, null);
        assertNotEquals(item1, item2);
        item2 = new ComparableObjectItem(2, null);
        assertEquals(item1, item2);
    }
}
