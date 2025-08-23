package org.jfree.chart.block;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import org.jfree.chart.TestUtils;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LabelBlockTestTest1 {

    /**
     * Confirm that the equals() method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        LabelBlock b1 = new LabelBlock("ABC", new Font("Dialog", Font.PLAIN, 12), Color.RED);
        LabelBlock b2 = new LabelBlock("ABC", new Font("Dialog", Font.PLAIN, 12), Color.RED);
        assertEquals(b1, b2);
        assertEquals(b2, b2);
        b1 = new LabelBlock("XYZ", new Font("Dialog", Font.PLAIN, 12), Color.RED);
        assertNotEquals(b1, b2);
        b2 = new LabelBlock("XYZ", new Font("Dialog", Font.PLAIN, 12), Color.RED);
        assertEquals(b1, b2);
        b1 = new LabelBlock("XYZ", new Font("Dialog", Font.BOLD, 12), Color.RED);
        assertNotEquals(b1, b2);
        b2 = new LabelBlock("XYZ", new Font("Dialog", Font.BOLD, 12), Color.RED);
        assertEquals(b1, b2);
        b1 = new LabelBlock("XYZ", new Font("Dialog", Font.BOLD, 12), Color.BLUE);
        assertNotEquals(b1, b2);
        b2 = new LabelBlock("XYZ", new Font("Dialog", Font.BOLD, 12), Color.BLUE);
        assertEquals(b1, b2);
        b1.setToolTipText("Tooltip");
        assertNotEquals(b1, b2);
        b2.setToolTipText("Tooltip");
        assertEquals(b1, b2);
        b1.setURLText("URL");
        assertNotEquals(b1, b2);
        b2.setURLText("URL");
        assertEquals(b1, b2);
        b1.setContentAlignmentPoint(TextBlockAnchor.CENTER_RIGHT);
        assertNotEquals(b1, b2);
        b2.setContentAlignmentPoint(TextBlockAnchor.CENTER_RIGHT);
        assertEquals(b1, b2);
        b1.setTextAnchor(RectangleAnchor.BOTTOM_RIGHT);
        assertNotEquals(b1, b2);
        b2.setTextAnchor(RectangleAnchor.BOTTOM_RIGHT);
        assertEquals(b1, b2);
    }
}
