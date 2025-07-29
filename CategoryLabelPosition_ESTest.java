package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelWidthType;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CategoryLabelPosition_ESTest extends CategoryLabelPosition_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testEqualsWithDifferentWidthRatio() {
        RectangleAnchor anchor = RectangleAnchor.TOP_LEFT;
        TextBlockAnchor textBlockAnchor = TextBlockAnchor.BOTTOM_LEFT;
        CategoryLabelPosition position1 = new CategoryLabelPosition(anchor, textBlockAnchor);
        CategoryLabelWidthType widthType = CategoryLabelWidthType.CATEGORY;
        CategoryLabelPosition position2 = new CategoryLabelPosition(anchor, textBlockAnchor, widthType, 0.0F);

        assertFalse("Positions with different width ratios should not be equal", position1.equals(position2));
        assertEquals("Width ratio should be 0.0F", 0.0F, position2.getWidthRatio(), 0.01F);
        assertEquals("Angle should be 0.0", 0.0, position2.getAngle(), 0.01);
    }

    @Test(timeout = 4000)
    public void testWidthRatioAndAngleDefaults() {
        RectangleAnchor anchor = RectangleAnchor.CENTER;
        TextBlockAnchor textBlockAnchor = TextBlockAnchor.TOP_CENTER;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.RANGE;
        CategoryLabelPosition position = new CategoryLabelPosition(anchor, textBlockAnchor, widthType, 0.0F);

        assertEquals("Width ratio should be 0.0F", 0.0F, position.getWidthRatio(), 0.01F);
        assertEquals("Angle should be 0.0", 0.0, position.getAngle(), 0.01);
    }

    @Test(timeout = 4000)
    public void testNegativeWidthRatioAndAngle() {
        RectangleAnchor anchor = RectangleAnchor.BOTTOM;
        TextBlockAnchor textBlockAnchor = TextBlockAnchor.TOP_LEFT;
        TextAnchor textAnchor = TextAnchor.BASELINE_RIGHT;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.CATEGORY;
        CategoryLabelPosition position = new CategoryLabelPosition(anchor, textBlockAnchor, textAnchor, -1.0, widthType, -1989.5195F);

        assertEquals("Width ratio should be -1989.5195F", -1989.5195F, position.getWidthRatio(), 0.01F);
        assertEquals("Angle should be -1.0", -1.0, position.getAngle(), 0.01);
    }

    @Test(timeout = 4000)
    public void testRotationAnchor() {
        RectangleAnchor anchor = RectangleAnchor.CENTER;
        TextBlockAnchor textBlockAnchor = TextBlockAnchor.CENTER;
        TextAnchor textAnchor = TextAnchor.TOP_CENTER;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.RANGE;
        CategoryLabelPosition position = new CategoryLabelPosition(anchor, textBlockAnchor, textAnchor, 1.0, widthType, 0.0F);

        assertEquals("Width ratio should be 0.0F", 0.0F, position.getWidthRatio(), 0.01F);
        assertEquals("Angle should be 1.0", 1.0, position.getAngle(), 0.01);
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionForNullLabelAnchor() {
        RectangleAnchor anchor = RectangleAnchor.TOP_RIGHT;
        TextAnchor textAnchor = TextAnchor.CENTER;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.CATEGORY;

        try {
            new CategoryLabelPosition(anchor, null, textAnchor, 1000.4695532, widthType, -1517.6609F);
            fail("Expected IllegalArgumentException for null labelAnchor");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'labelAnchor' argument.", e.getMessage());
        }
    }

    // Additional test methods with descriptive names and clear assertions...

}