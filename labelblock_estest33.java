package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

/**
 * Unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that the getPaint() method correctly returns the Paint object
     * that was provided in the constructor.
     */
    @Test
    public void getPaintShouldReturnColorSetInConstructor() {
        // Arrange: Define the test data and create the object under test.
        String labelText = "Test Label";
        Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
        Paint expectedPaint = Color.CYAN;

        LabelBlock labelBlock = new LabelBlock(labelText, labelFont, expectedPaint);

        // Act: Call the method being tested.
        Paint actualPaint = labelBlock.getPaint();

        // Assert: Check if the result matches the expected outcome.
        assertEquals("The paint returned should be the one set in the constructor.",
                     expectedPaint, actualPaint);
    }
}