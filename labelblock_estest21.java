package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.awt.Color;
import java.awt.Font;

/**
 * Contains tests for the equals() method of the {@link LabelBlock} class.
 * 
 * Note: The class name and inheritance are preserved from the original 
 * EvoSuite-generated test. In a typical project, this class would be named 
 * LabelBlockTest.
 */
public class LabelBlock_ESTestTest21 extends LabelBlock_ESTest_scaffolding {

    /**
     * Verifies that two LabelBlock instances with different paint colors are not
     * considered equal. The equals() method should correctly compare the paint
     * attribute.
     */
    @Test
    public void labelBlocksWithDifferentPaintShouldNotBeEqual() {
        // Arrange: Create two LabelBlock instances that are identical except for their paint color.
        String commonText = "SansSerif";
        Font commonFont = new Font("SansSerif", Font.PLAIN, 10);

        LabelBlock blockWithCyanPaint = new LabelBlock(commonText, commonFont, Color.CYAN);
        LabelBlock blockWithPinkPaint = new LabelBlock(commonText, commonFont, Color.PINK);

        // Act: Compare the two blocks for equality.
        boolean areEqual = blockWithCyanPaint.equals(blockWithPinkPaint);

        // Assert: The blocks should not be equal.
        assertFalse("LabelBlocks with different paint colors should not be equal.", areEqual);
    }
}