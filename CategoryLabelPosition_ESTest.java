package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelWidthType;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;

/**
 * Test suite for CategoryLabelPosition class.
 * Tests constructor behavior, property getters, equals/hashCode methods,
 * and null argument validation.
 */
public class CategoryLabelPositionTest {

    // Test data constants for better readability
    private static final RectangleAnchor CATEGORY_ANCHOR_CENTER = RectangleAnchor.CENTER;
    private static final RectangleAnchor CATEGORY_ANCHOR_TOP_LEFT = RectangleAnchor.TOP_LEFT;
    private static final TextBlockAnchor LABEL_ANCHOR_BOTTOM_LEFT = TextBlockAnchor.BOTTOM_LEFT;
    private static final TextBlockAnchor LABEL_ANCHOR_CENTER = TextBlockAnchor.CENTER;
    private static final TextAnchor ROTATION_ANCHOR_CENTER = TextAnchor.CENTER;
    private static final CategoryLabelWidthType WIDTH_TYPE_CATEGORY = CategoryLabelWidthType.CATEGORY;
    private static final CategoryLabelWidthType WIDTH_TYPE_RANGE = CategoryLabelWidthType.RANGE;
    
    private static final double ZERO_ANGLE = 0.0;
    private static final double POSITIVE_ANGLE = 1.0;
    private static final double NEGATIVE_ANGLE = -1.0;
    private static final float DEFAULT_WIDTH_RATIO = 0.95f;
    private static final float CUSTOM_WIDTH_RATIO = 0.5f;

    // Constructor Tests
    
    @Test
    public void testDefaultConstructor_ShouldCreatePositionWithDefaultValues() {
        CategoryLabelPosition position = new CategoryLabelPosition();
        
        assertEquals("Default angle should be 0.0", ZERO_ANGLE, position.getAngle(), 0.01);
        assertEquals("Default width ratio should be 0.95", DEFAULT_WIDTH_RATIO, position.getWidthRatio(), 0.01f);
        assertNotNull("Category anchor should not be null", position.getCategoryAnchor());
        assertNotNull("Label anchor should not be null", position.getLabelAnchor());
        assertNotNull("Rotation anchor should not be null", position.getRotationAnchor());
        assertNotNull("Width type should not be null", position.getWidthType());
    }

    @Test
    public void testTwoParameterConstructor_ShouldCreatePositionWithSpecifiedAnchors() {
        CategoryLabelPosition position = new CategoryLabelPosition(CATEGORY_ANCHOR_TOP_LEFT, LABEL_ANCHOR_BOTTOM_LEFT);
        
        assertEquals("Angle should be default 0.0", ZERO_ANGLE, position.getAngle(), 0.01);
        assertEquals("Width ratio should be default 0.95", DEFAULT_WIDTH_RATIO, position.getWidthRatio(), 0.01f);
        assertEquals("Category anchor should match constructor parameter", CATEGORY_ANCHOR_TOP_LEFT, position.getCategoryAnchor());
        assertEquals("Label anchor should match constructor parameter", LABEL_ANCHOR_BOTTOM_LEFT, position.getLabelAnchor());
    }

    @Test
    public void testFourParameterConstructor_ShouldCreatePositionWithCustomWidthSettings() {
        CategoryLabelPosition position = new CategoryLabelPosition(
            CATEGORY_ANCHOR_CENTER, LABEL_ANCHOR_CENTER, WIDTH_TYPE_RANGE, CUSTOM_WIDTH_RATIO);
        
        assertEquals("Angle should be default 0.0", ZERO_ANGLE, position.getAngle(), 0.01);
        assertEquals("Width ratio should match constructor parameter", CUSTOM_WIDTH_RATIO, position.getWidthRatio(), 0.01f);
        assertEquals("Width type should match constructor parameter", WIDTH_TYPE_RANGE, position.getWidthType());
    }

    @Test
    public void testFullConstructor_ShouldCreatePositionWithAllCustomValues() {
        CategoryLabelPosition position = new CategoryLabelPosition(
            CATEGORY_ANCHOR_CENTER, LABEL_ANCHOR_CENTER, ROTATION_ANCHOR_CENTER, 
            POSITIVE_ANGLE, WIDTH_TYPE_RANGE, CUSTOM_WIDTH_RATIO);
        
        assertEquals("Angle should match constructor parameter", POSITIVE_ANGLE, position.getAngle(), 0.01);
        assertEquals("Width ratio should match constructor parameter", CUSTOM_WIDTH_RATIO, position.getWidthRatio(), 0.01f);
        assertEquals("Category anchor should match constructor parameter", CATEGORY_ANCHOR_CENTER, position.getCategoryAnchor());
        assertEquals("Label anchor should match constructor parameter", LABEL_ANCHOR_CENTER, position.getLabelAnchor());
        assertEquals("Rotation anchor should match constructor parameter", ROTATION_ANCHOR_CENTER, position.getRotationAnchor());
        assertEquals("Width type should match constructor parameter", WIDTH_TYPE_RANGE, position.getWidthType());
    }

    // Property Getter Tests
    
    @Test
    public void testGetAngle_ShouldReturnPositiveAngle() {
        CategoryLabelPosition position = new CategoryLabelPosition(
            CATEGORY_ANCHOR_CENTER, LABEL_ANCHOR_CENTER, ROTATION_ANCHOR_CENTER, 
            POSITIVE_ANGLE, WIDTH_TYPE_RANGE, CUSTOM_WIDTH_RATIO);
        
        assertEquals("Should return the positive angle set in constructor", POSITIVE_ANGLE, position.getAngle(), 0.01);
    }

    @Test
    public void testGetAngle_ShouldReturnNegativeAngle() {
        CategoryLabelPosition position = new CategoryLabelPosition(
            RectangleAnchor.TOP_RIGHT, TextBlockAnchor.CENTER_RIGHT, TextAnchor.TOP_RIGHT, 
            NEGATIVE_ANGLE, WIDTH_TYPE_RANGE, 3650.8f);
        
        assertEquals("Should return the negative angle set in constructor", NEGATIVE_ANGLE, position.getAngle(), 0.01);
    }

    @Test
    public void testGetWidthRatio_ShouldReturnCustomRatio() {
        float customRatio = -1989.5195f;
        CategoryLabelPosition position = new CategoryLabelPosition(
            RectangleAnchor.BOTTOM, TextBlockAnchor.TOP_LEFT, TextAnchor.BASELINE_RIGHT, 
            NEGATIVE_ANGLE, WIDTH_TYPE_CATEGORY, customRatio);
        
        assertEquals("Should return the custom width ratio", customRatio, position.getWidthRatio(), 0.01f);
    }

    @Test
    public void testGetRotationAnchor_ShouldReturnCorrectAnchor() {
        CategoryLabelPosition position = new CategoryLabelPosition(
            RectangleAnchor.TOP_RIGHT, TextBlockAnchor.CENTER_RIGHT, TextAnchor.HALF_ASCENT_RIGHT, 
            986.267245817, WIDTH_TYPE_CATEGORY, DEFAULT_WIDTH_RATIO);
        
        TextAnchor rotationAnchor = position.getRotationAnchor();
        assertEquals("Should return the rotation anchor set in constructor", TextAnchor.HALF_ASCENT_RIGHT, rotationAnchor);
    }

    // Null Argument Validation Tests
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithNullLabelAnchor_ShouldThrowException() {
        new CategoryLabelPosition(CATEGORY_ANCHOR_TOP_LEFT, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFourParameterConstructor_WithNullLabelAnchor_ShouldThrowException() {
        new CategoryLabelPosition(RectangleAnchor.BOTTOM_LEFT, null, WIDTH_TYPE_RANGE, 2093.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFullConstructor_WithNullLabelAnchor_ShouldThrowException() {
        new CategoryLabelPosition(RectangleAnchor.TOP_RIGHT, null, TextAnchor.CENTER, 
            1000.4695532, WIDTH_TYPE_CATEGORY, -1517.6609f);
    }

    // Equals Method Tests
    
    @Test
    public void testEquals_WithSameInstance_ShouldReturnTrue() {
        CategoryLabelPosition position = new CategoryLabelPosition();
        
        assertTrue("Position should equal itself", position.equals(position));
    }

    @Test
    public void testEquals_WithIdenticalPositions_ShouldReturnTrue() {
        CategoryLabelPosition position1 = new CategoryLabelPosition();
        CategoryLabelPosition position2 = new CategoryLabelPosition();
        
        assertTrue("Identical positions should be equal", position1.equals(position2));
        assertTrue("Equality should be symmetric", position2.equals(position1));
    }

    @Test
    public void testEquals_WithDifferentWidthTypes_ShouldReturnFalse() {
        CategoryLabelPosition position1 = new CategoryLabelPosition(CATEGORY_ANCHOR_TOP_LEFT, LABEL_ANCHOR_BOTTOM_LEFT);
        CategoryLabelPosition position2 = new CategoryLabelPosition(CATEGORY_ANCHOR_TOP_LEFT, LABEL_ANCHOR_BOTTOM_LEFT, WIDTH_TYPE_CATEGORY, 0.0f);
        
        assertFalse("Positions with different width configurations should not be equal", position1.equals(position2));
        assertFalse("Inequality should be symmetric", position2.equals(position1));
    }

    @Test
    public void testEquals_WithDifferentAngles_ShouldReturnFalse() {
        CategoryLabelPosition position1 = new CategoryLabelPosition(
            RectangleAnchor.RIGHT, TextBlockAnchor.BOTTOM_CENTER, TextAnchor.BASELINE_CENTER, 
            ZERO_ANGLE, WIDTH_TYPE_RANGE, -985.5677f);
        CategoryLabelPosition position2 = new CategoryLabelPosition(
            RectangleAnchor.RIGHT, TextBlockAnchor.BOTTOM_CENTER, TextAnchor.BASELINE_CENTER, 
            986.267245817, WIDTH_TYPE_RANGE, -985.5677f);
        
        assertFalse("Positions with different angles should not be equal", position1.equals(position2));
        assertFalse("Inequality should be symmetric", position2.equals(position1));
    }

    @Test
    public void testEquals_WithDifferentCategoryAnchors_ShouldReturnFalse() {
        CategoryLabelPosition position1 = new CategoryLabelPosition();
        CategoryLabelPosition position2 = new CategoryLabelPosition(RectangleAnchor.TOP_RIGHT, TextBlockAnchor.CENTER_RIGHT);
        
        assertFalse("Positions with different category anchors should not be equal", position1.equals(position2));
        assertFalse("Inequality should be symmetric", position2.equals(position1));
    }

    @Test
    public void testEquals_WithDifferentRotationAnchors_ShouldReturnFalse() {
        CategoryLabelPosition position1 = new CategoryLabelPosition(
            RectangleAnchor.BOTTOM_RIGHT, TextBlockAnchor.CENTER_LEFT, WIDTH_TYPE_RANGE, -641.25f);
        CategoryLabelPosition position2 = new CategoryLabelPosition(
            RectangleAnchor.BOTTOM_RIGHT, TextBlockAnchor.CENTER_LEFT, TextAnchor.CENTER_RIGHT, 
            -641.25f, WIDTH_TYPE_RANGE, -641.25f);
        
        assertFalse("Positions with different rotation anchors should not be equal", position1.equals(position2));
    }

    @Test
    public void testEquals_WithDifferentWidthRatios_ShouldReturnFalse() {
        CategoryLabelPosition position1 = new CategoryLabelPosition(RectangleAnchor.TOP_RIGHT, TextBlockAnchor.CENTER_RIGHT);
        CategoryLabelPosition position2 = new CategoryLabelPosition(RectangleAnchor.TOP_RIGHT, TextBlockAnchor.CENTER_RIGHT, WIDTH_TYPE_CATEGORY, -2714.5823f);
        
        assertFalse("Positions with different width ratios should not be equal", position1.equals(position2));
        assertFalse("Inequality should be symmetric", position2.equals(position1));
    }

    @Test
    public void testEquals_WithNonCategoryLabelPositionObject_ShouldReturnFalse() {
        CategoryLabelPosition position = new CategoryLabelPosition();
        Object otherObject = new Object();
        
        assertFalse("Position should not equal non-CategoryLabelPosition object", position.equals(otherObject));
    }

    @Test
    public void testEquals_WithDefaultVsCustomPosition_ShouldReturnFalse() {
        CategoryLabelPosition defaultPosition = new CategoryLabelPosition();
        CategoryLabelPosition customPosition = new CategoryLabelPosition(
            CATEGORY_ANCHOR_CENTER, TextBlockAnchor.CENTER, TextAnchor.TOP_CENTER, 
            POSITIVE_ANGLE, WIDTH_TYPE_RANGE, 0.0f);
        
        assertFalse("Default and custom positions should not be equal", defaultPosition.equals(customPosition));
    }

    // Hash Code Test
    
    @Test
    public void testHashCode_ShouldExecuteWithoutException() {
        CategoryLabelPosition position = new CategoryLabelPosition();
        
        // Just verify hashCode can be called without throwing an exception
        int hashCode = position.hashCode();
        // Hash code value itself is implementation-dependent, so we don't assert a specific value
    }
}