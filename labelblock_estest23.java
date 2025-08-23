package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

/**
 * Contains tests for the equals() method in the {@link LabelBlock} class.
 */
public class LabelBlockEqualsTest {

    /**
     * Verifies that two LabelBlock instances are not considered equal if they
     * have different text, font, and paint attributes.
     */
    @Test
    public void equals_whenAttributesDiffer_returnsFalse() {
        // Arrange: Create two distinct LabelBlock instances.

        // The first block uses a simple text string. The constructor will assign
        // a default font (SansSerif, 10pt) and paint (Color.BLACK).
        LabelBlock blockA = new LabelBlock("First Label");

        // The second block is created with explicitly different text, font, and paint.
        Font customFont = new Font("Dialog", Font.BOLD, 12);
        Paint customPaint = Color.RED;
        LabelBlock blockB = new LabelBlock("Second Label", customFont, customPaint);

        // Act & Assert: Verify that the two blocks are not equal.
        // The equals() method should return false because the text, font, and paint
        // properties all differ.
        assertNotEquals("Blocks with different attributes should not be equal", blockA, blockB);
    }
}