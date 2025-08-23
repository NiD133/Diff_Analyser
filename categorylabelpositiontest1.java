package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryLabelPositionTestTest1 {

    /**
     * Check that the equals() method can distinguish all fields.
     */
    @Test
    public void testEquals() {
        CategoryLabelPosition p1 = new CategoryLabelPosition(RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        CategoryLabelPosition p2 = new CategoryLabelPosition(RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);
        assertEquals(p2, p1);
        p1 = new CategoryLabelPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(p1, p2);
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);
        p1 = new CategoryLabelPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(p1, p2);
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);
        p1 = new CategoryLabelPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(p1, p2);
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);
        p1 = new CategoryLabelPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertNotEquals(p1, p2);
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);
        p1 = new CategoryLabelPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, CategoryLabelWidthType.CATEGORY, 0.44f);
        assertNotEquals(p1, p2);
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, CategoryLabelWidthType.CATEGORY, 0.44f);
        assertEquals(p1, p2);
        p1 = new CategoryLabelPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, CategoryLabelWidthType.CATEGORY, 0.55f);
        assertNotEquals(p1, p2);
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, CategoryLabelWidthType.CATEGORY, 0.55f);
        assertEquals(p1, p2);
    }
}
