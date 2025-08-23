package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    /**
     * Verifies that the equals() method returns false when comparing two
     * CategoryLabelPosition instances that differ only by their widthRatio.
     */
    @Test
    public void equals_shouldReturnFalse_whenWidthRatiosDiffer() {
        // Arrange: Create two CategoryLabelPosition instances that are identical
        // except for their widthRatio.
        RectangleAnchor commonAnchor = RectangleAnchor.TOP_RIGHT;
        TextBlockAnchor commonLabelAnchor = TextBlockAnchor.CENTER_RIGHT;

        // This instance uses a constructor that sets a default widthRatio (0.95f).
        CategoryLabelPosition positionWithDefaultRatio = new CategoryLabelPosition(commonAnchor, commonLabelAnchor);

        // This instance is created with a different, non-default widthRatio.
        // The widthType is the same as the default, so only the ratio differs.
        float customWidthRatio = -2714.5823F;
        CategoryLabelPosition positionWithCustomRatio = new CategoryLabelPosition(
                commonAnchor,
                commonLabelAnchor,
                CategoryLabelWidthType.CATEGORY,
                customWidthRatio);

        // Sanity check to confirm the custom width ratio was set correctly.
        assertEquals(customWidthRatio, positionWithCustomRatio.getWidthRatio(), 0.01F);

        // Act & Assert: The two objects should not be considered equal.
        // assertNotEquals is used for a clear and direct expression of this intent.
        assertNotEquals(positionWithDefaultRatio, positionWithCustomRatio);
    }
}