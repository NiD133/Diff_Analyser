package org.jfree.chart.block;

import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * This is an improved version of an auto-generated test for the LabelBlock class.
 * The original class name is preserved for context.
 */
public class LabelBlock_ESTestTest37 extends LabelBlock_ESTest_scaffolding {

    /**
     * Verifies that the draw() method executes without errors when the label text is empty.
     * An empty label should be handled gracefully, and the call should not have any
     * unexpected side effects, such as altering the block's default properties.
     */
    @Test(timeout = 4000)
    public void drawWithEmptyLabelShouldCompleteSuccessfully() {
        // Arrange: Create a LabelBlock with an empty string, a mock Graphics2D context,
        // and a drawing area.
        LabelBlock labelBlock = new LabelBlock("");
        Graphics2D mockGraphics = mock(Graphics2D.class);
        Rectangle2D drawArea = new Rectangle2D.Float();

        // Act: Call the draw method. The primary goal is to ensure this operation
        // completes without throwing an exception.
        labelBlock.draw(mockGraphics, drawArea);

        // Assert: Confirm that no unexpected side effects occurred. The ID, which is
        // null by default, should remain null.
        assertNull("The block ID should remain null after drawing.", labelBlock.getID());
    }
}