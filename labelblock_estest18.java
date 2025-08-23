package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Tests for the draw(Graphics2D, Rectangle2D, Object) method in the LabelBlock class.
 *
 * Note: The original class name 'LabelBlock_ESTestTest18' and its scaffolding are preserved.
 * In a real-world scenario, these would be refactored for better clarity
 * (e.g., a class named 'LabelBlockDrawTest').
 */
public class LabelBlock_ESTestTest18 extends LabelBlock_ESTest_scaffolding {

    /**
     * Verifies that the draw() method with a 'params' object always returns null,
     * as specified by its Javadoc contract.
     */
    @Test
    public void drawWithParamsShouldAlwaysReturnNull() {
        // Arrange: Create a LabelBlock instance and a dummy graphics context for drawing.
        LabelBlock labelBlock = new LabelBlock("Test Label");
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        Rectangle2D drawArea = new Rectangle2D.Double(0, 0, 100, 50);

        // The third parameter ('params') is documented as being ignored by the method.
        Object params = null;

        // Act: Call the draw method whose return value is under test.
        Object result = labelBlock.draw(g2, drawArea, params);

        // Assert: The method's contract states it must always return null.
        assertNull("The draw(g2, area, params) method should always return null.", result);
    }
}