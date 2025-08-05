package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A set of tests for the {@link CategoryLabelPosition} class, focusing on constructor behavior,
 * property initialization, and the equals/hashCode contract.
 */
public class CategoryLabelPositionTest {

    // --- Constructor Tests ---

    @Test
    public void defaultConstructor_shouldSetDefaultValues() {
        // Arrange & Act
        CategoryLabelPosition position = new CategoryLabelPosition();

        // Assert
        assertEquals(RectangleAnchor.CENTER, position.getCategoryAnchor());
        assertEquals(TextBlockAnchor.BOTTOM_CENTER, position.getLabelAnchor());
        assertEquals(TextAnchor.CENTER, position.getRotationAnchor());
        assertEquals(0.0, position.getAngle(), 0.0);
        assertEquals(CategoryLabelWidthType.CATEGORY, position.getWidthType());
        assertEquals(0.95f, position.getWidthRatio(), 0.0f);
    }

    @Test
    public void constructorWithAnchors_shouldSetDefaultsForOtherProperties() {
        // Arrange
        RectangleAnchor categoryAnchor = RectangleAnchor.TOP_RIGHT;
        TextBlockAnchor labelAnchor = TextBlockAnchor.CENTER_LEFT;

        // Act
        CategoryLabelPosition position = new CategoryLabelPosition(categoryAnchor, labelAnchor);

        // Assert
        assertEquals(categoryAnchor, position.getCategoryAnchor());
        assertEquals(labelAnchor, position.getLabelAnchor());
        // Check that other properties are set to their default values
        assertEquals(TextAnchor.CENTER, position.getRotationAnchor());
        assertEquals(0.0, position.getAngle(), 0.0);
        assertEquals(CategoryLabelWidthType.CATEGORY, position.getWidthType());
        assertEquals(0.95f, position.getWidthRatio(), 0.0f);
    }

    @Test
    public void constructorWithWidthProperties_shouldSetDefaultsForAngleAndRotation() {
        // Arrange
        RectangleAnchor categoryAnchor = RectangleAnchor.BOTTOM_LEFT;
        TextBlockAnchor labelAnchor = TextBlockAnchor.TOP_RIGHT;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.RANGE;
        float widthRatio = 0.75f;

        // Act
        CategoryLabelPosition position = new CategoryLabelPosition(categoryAnchor, labelAnchor, widthType, widthRatio);

        // Assert
        assertEquals(categoryAnchor, position.getCategoryAnchor());
        assertEquals(labelAnchor, position.getLabelAnchor());
        assertEquals(widthType, position.getWidthType());
        assertEquals(widthRatio, position.getWidthRatio(), 0.0f);
        // Check that other properties are set to their default values
        assertEquals(TextAnchor.CENTER, position.getRotationAnchor());
        assertEquals(0.0, position.getAngle(), 0.0);
    }

    @Test
    public void fullConstructor_shouldSetAllPropertiesCorrectly() {
        // Arrange
        RectangleAnchor categoryAnchor = RectangleAnchor.TOP_LEFT;
        TextBlockAnchor labelAnchor = TextBlockAnchor.TOP_RIGHT;
        TextAnchor rotationAnchor = TextAnchor.TOP_CENTER;
        double angle = Math.PI / 4.0;
        CategoryLabelWidthType widthType = CategoryLabelWidthType.RANGE;
        float widthRatio = 0.5f;

        // Act
        CategoryLabelPosition position = new CategoryLabelPosition(categoryAnchor, labelAnchor, rotationAnchor, angle, widthType, widthRatio);

        // Assert
        assertEquals(categoryAnchor, position.getCategoryAnchor());
        assertEquals(labelAnchor, position.getLabelAnchor());
        assertEquals(rotationAnchor, position.getRotationAnchor());
        assertEquals(angle, position.getAngle(), 0.0);
        assertEquals(widthType, position.getWidthType());
        assertEquals(widthRatio, position.getWidthRatio(), 0.0f);
    }

    // --- Constructor Validation Tests ---

    @Test(expected = IllegalArgumentException.class)
    public void constructor_whenCategoryAnchorIsNull_shouldThrowException() {
        new CategoryLabelPosition(null, TextBlockAnchor.CENTER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_whenLabelAnchorIsNull_shouldThrowException() {
        new CategoryLabelPosition(RectangleAnchor.CENTER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fullConstructor_whenRotationAnchorIsNull_shouldThrowException() {
        new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, null, 0.0, CategoryLabelWidthType.CATEGORY, 0.95f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fullConstructor_whenWidthTypeIsNull_shouldThrowException() {
        new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.CENTER, 0.0, null, 0.95f);
    }

    // --- Equals and HashCode Contract Tests ---

    @Test
    public void equals_whenObjectIsSame_shouldReturnTrue() {
        CategoryLabelPosition position = new CategoryLabelPosition();
        assertTrue(position.equals(position));
    }

    @Test
    public void equals_whenObjectIsNull_shouldReturnFalse() {
        CategoryLabelPosition position = new CategoryLabelPosition();
        assertFalse(position.equals(null));
    }

    @Test
    public void equals_whenObjectIsDifferentClass_shouldReturnFalse() {
        CategoryLabelPosition position = new CategoryLabelPosition();
        assertFalse(position.equals("a string object"));
    }

    @Test
    public void equals_whenObjectsHaveSameValues_shouldReturnTrue() {
        // Arrange
        CategoryLabelPosition position1 = new CategoryLabelPosition(RectangleAnchor.TOP_LEFT, TextBlockAnchor.TOP_RIGHT, TextAnchor.TOP_CENTER, 0.5, CategoryLabelWidthType.RANGE, 0.5f);
        CategoryLabelPosition position2 = new CategoryLabelPosition(RectangleAnchor.TOP_LEFT, TextBlockAnchor.TOP_RIGHT, TextAnchor.TOP_CENTER, 0.5, CategoryLabelWidthType.RANGE, 0.5f);

        // Act & Assert
        assertTrue(position1.equals(position2));
    }

    @Test
    public void hashCode_whenObjectsAreEqual_shouldBeEqual() {
        // Arrange
        CategoryLabelPosition position1 = new CategoryLabelPosition(RectangleAnchor.TOP_LEFT, TextBlockAnchor.TOP_RIGHT, TextAnchor.TOP_CENTER, 0.5, CategoryLabelWidthType.RANGE, 0.5f);
        CategoryLabelPosition position2 = new CategoryLabelPosition(RectangleAnchor.TOP_LEFT, TextBlockAnchor.TOP_RIGHT, TextAnchor.TOP_CENTER, 0.5, CategoryLabelWidthType.RANGE, 0.5f);

        // Act & Assert
        assertEquals(position1.hashCode(), position2.hashCode());
    }

    @Test
    public void equals_whenCategoryAnchorsDiffer_shouldReturnFalse() {
        CategoryLabelPosition position1 = new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER);
        CategoryLabelPosition position2 = new CategoryLabelPosition(RectangleAnchor.LEFT, TextBlockAnchor.CENTER);
        assertFalse(position1.equals(position2));
    }

    @Test
    public void equals_whenLabelAnchorsDiffer_shouldReturnFalse() {
        CategoryLabelPosition position1 = new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER);
        CategoryLabelPosition position2 = new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.TOP_CENTER);
        assertFalse(position1.equals(position2));
    }

    @Test
    public void equals_whenRotationAnchorsDiffer_shouldReturnFalse() {
        CategoryLabelPosition position1 = new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.CENTER, 0.0, CategoryLabelWidthType.CATEGORY, 0.95f);
        CategoryLabelPosition position2 = new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.TOP_LEFT, 0.0, CategoryLabelWidthType.CATEGORY, 0.95f);
        assertFalse(position1.equals(position2));
    }

    @Test
    public void equals_whenAnglesDiffer_shouldReturnFalse() {
        CategoryLabelPosition position1 = new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.CENTER, 0.1, CategoryLabelWidthType.CATEGORY, 0.95f);
        CategoryLabelPosition position2 = new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.CENTER, 0.2, CategoryLabelWidthType.CATEGORY, 0.95f);
        assertFalse(position1.equals(position2));
    }

    @Test
    public void equals_whenWidthTypesDiffer_shouldReturnFalse() {
        CategoryLabelPosition position1 = new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.CENTER, 0.0, CategoryLabelWidthType.CATEGORY, 0.95f);
        CategoryLabelPosition position2 = new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.CENTER, 0.0, CategoryLabelWidthType.RANGE, 0.95f);
        assertFalse(position1.equals(position2));
    }

    @Test
    public void equals_whenWidthRatiosDiffer_shouldReturnFalse() {
        CategoryLabelPosition position1 = new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.CENTER, 0.0, CategoryLabelWidthType.CATEGORY, 0.95f);
        CategoryLabelPosition position2 = new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.CENTER, 0.0, CategoryLabelWidthType.CATEGORY, 0.50f);
        assertFalse(position1.equals(position2));
    }
}