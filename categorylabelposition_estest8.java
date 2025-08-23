package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link CategoryLabelPosition} class, focusing on its constructor and getters.
 */
public class CategoryLabelPositionTest {

    private static final double DELTA = 0.001;

    /**
     * Verifies that the constructor correctly initializes the object's properties
     * and that the corresponding getters return the expected values.
     */
    @Test
    public void gettersShouldReturnValuesSetInConstructor() {
        // Arrange: Define the parameters for creating a CategoryLabelPosition instance.
        RectangleAnchor categoryAnchor = RectangleAnchor.CENTER;
        TextBlockAnchor labelAnchor = TextBlockAnchor.CENTER;
        TextAnchor rotationAnchor = TextAnchor.TOP_CENTER;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.RANGE;
        double expectedAngle = 1.0;
        float expectedWidthRatio = 0.0F;

        // Act: Create the CategoryLabelPosition instance using the full constructor.
        CategoryLabelPosition labelPosition = new CategoryLabelPosition(
                categoryAnchor,
                labelAnchor,
                rotationAnchor,
                expectedAngle,
                widthType,
                expectedWidthRatio
        );

        // Assert: Check that the getters return the values passed during construction.
        assertEquals("The angle should match the value provided in the constructor.",
                expectedAngle, labelPosition.getAngle(), DELTA);

        assertEquals("The width ratio should match the value provided in the constructor.",
                expectedWidthRatio, labelPosition.getWidthRatio(), DELTA);
    }
}