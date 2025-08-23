package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest {

    private static final double FLOATING_POINT_DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes all properties and that the
     * corresponding getters return the expected values.
     */
    @Test
    public void gettersShouldReturnValuesSetInConstructor() {
        // Arrange: Define the configuration for the CategoryLabelPosition.
        RectangleAnchor expectedCategoryAnchor = RectangleAnchor.CENTER;
        TextBlockAnchor expectedLabelAnchor = TextBlockAnchor.CENTER;
        TextAnchor expectedRotationAnchor = TextAnchor.TOP_CENTER;
        double expectedAngle = 1.0;
        CategoryLabelWidthType expectedWidthType = CategoryLabelWidthType.RANGE;
        float expectedWidthRatio = 0.0F;

        // Act: Create an instance using the constructor that sets all properties.
        CategoryLabelPosition position = new CategoryLabelPosition(
                expectedCategoryAnchor,
                expectedLabelAnchor,
                expectedRotationAnchor,
                expectedAngle,
                expectedWidthType,
                expectedWidthRatio
        );

        // Assert: Confirm that each getter returns the value provided to the constructor.
        assertEquals("Category anchor should match the constructor argument.",
                expectedCategoryAnchor, position.getCategoryAnchor());

        assertEquals("Label anchor should match the constructor argument.",
                expectedLabelAnchor, position.getLabelAnchor());

        assertEquals("Rotation anchor should match the constructor argument.",
                expectedRotationAnchor, position.getRotationAnchor());

        assertEquals("Angle should match the constructor argument.",
                expectedAngle, position.getAngle(), FLOATING_POINT_DELTA);

        assertEquals("Width type should match the constructor argument.",
                expectedWidthType, position.getWidthType());

        assertEquals("Width ratio should match the constructor argument.",
                expectedWidthRatio, position.getWidthRatio(), FLOATING_POINT_DELTA);
    }
}