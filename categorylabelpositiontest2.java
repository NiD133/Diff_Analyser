package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryLabelPositionTestTest2 {

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashCode() {
        CategoryLabelPosition a1 = new CategoryLabelPosition();
        CategoryLabelPosition a2 = new CategoryLabelPosition();
        assertEquals(a1, a2);
        int h1 = a1.hashCode();
        int h2 = a2.hashCode();
        assertEquals(h1, h2);
    }
}
