package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.text.TextAnchor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CategoryLabelPosition} class, focusing on equality,
 * hashing, and serialization.
 */
@DisplayName("CategoryLabelPosition")
class CategoryLabelPositionTest {

    private static final RectangleAnchor ANCHOR = RectangleAnchor.TOP;
    private static final TextBlockAnchor LABEL_ANCHOR = TextBlockAnchor.CENTER;
    private static final TextAnchor ROTATION_ANCHOR = TextAnchor.CENTER;
    private static final double ANGLE = Math.PI / 6.0;
    private static final CategoryLabelWidthType WIDTH_TYPE = CategoryLabelWidthType.CATEGORY;
    private static final float WIDTH_RATIO = 0.55f;

    private CategoryLabelPosition basePosition;

    @BeforeEach
    void setUp() {
        basePosition = new CategoryLabelPosition(
            ANCHOR, LABEL_ANCHOR, ROTATION_ANCHOR, ANGLE, WIDTH_TYPE, WIDTH_RATIO
        );
    }

    @Nested
    @DisplayName("equals() and hashCode() contract")
    class EqualsAndHashCode {

        @Test
        @DisplayName("should be true for two identical instances")
        void equals_withIdenticalInstances_shouldBeTrue() {
            CategoryLabelPosition identicalPosition = new CategoryLabelPosition(
                ANCHOR, LABEL_ANCHOR, ROTATION_ANCHOR, ANGLE, WIDTH_TYPE, WIDTH_RATIO
            );
            assertEquals(basePosition, identicalPosition);
            assertEquals(basePosition.hashCode(), identicalPosition.hashCode(), "Hash codes should be equal for equal objects");
        }

        @Test
        @DisplayName("should be true for the same instance (reflexive)")
        void equals_withSameInstance_shouldBeTrue() {
            assertEquals(basePosition, basePosition);
        }

        @Test
        @DisplayName("should be false when compared to null")
        void equals_withNull_shouldBeFalse() {
            assertNotEquals(null, basePosition);
        }

        @Test
        @DisplayName("should be false when compared to a different type")
        void equals_withDifferentType_shouldBeFalse() {
            assertNotEquals("A String", basePosition);
        }

        @Test
        @DisplayName("should be false when categoryAnchor differs")
        void equals_whenCategoryAnchorDiffers_shouldBeFalse() {
            CategoryLabelPosition differentPosition = new CategoryLabelPosition(
                RectangleAnchor.BOTTOM_LEFT, LABEL_ANCHOR, ROTATION_ANCHOR, ANGLE, WIDTH_TYPE, WIDTH_RATIO
            );
            assertNotEquals(basePosition, differentPosition);
        }

        @Test
        @DisplayName("should be false when labelAnchor differs")
        void equals_whenLabelAnchorDiffers_shouldBeFalse() {
            CategoryLabelPosition differentPosition = new CategoryLabelPosition(
                ANCHOR, TextBlockAnchor.CENTER_RIGHT, ROTATION_ANCHOR, ANGLE, WIDTH_TYPE, WIDTH_RATIO
            );
            assertNotEquals(basePosition, differentPosition);
        }

        @Test
        @DisplayName("should be false when rotationAnchor differs")
        void equals_whenRotationAnchorDiffers_shouldBeFalse() {
            CategoryLabelPosition differentPosition = new CategoryLabelPosition(
                ANCHOR, LABEL_ANCHOR, TextAnchor.BASELINE_LEFT, ANGLE, WIDTH_TYPE, WIDTH_RATIO
            );
            assertNotEquals(basePosition, differentPosition);
        }

        @Test
        @DisplayName("should be false when angle differs")
        void equals_whenAngleDiffers_shouldBeFalse() {
            CategoryLabelPosition differentPosition = new CategoryLabelPosition(
                ANCHOR, LABEL_ANCHOR, ROTATION_ANCHOR, Math.PI / 4.0, WIDTH_TYPE, WIDTH_RATIO
            );
            assertNotEquals(basePosition, differentPosition);
        }

        @Test
        @DisplayName("should be false when widthType differs")
        void equals_whenWidthTypeDiffers_shouldBeFalse() {
            CategoryLabelPosition differentPosition = new CategoryLabelPosition(
                ANCHOR, LABEL_ANCHOR, ROTATION_ANCHOR, ANGLE, CategoryLabelWidthType.RANGE, WIDTH_RATIO
            );
            assertNotEquals(basePosition, differentPosition);
        }

        @Test
        @DisplayName("should be false when widthRatio differs")
        void equals_whenWidthRatioDiffers_shouldBeFalse() {
            CategoryLabelPosition differentPosition = new CategoryLabelPosition(
                ANCHOR, LABEL_ANCHOR, ROTATION_ANCHOR, ANGLE, WIDTH_TYPE, 0.44f
            );
            assertNotEquals(basePosition, differentPosition);
        }
    }

    @Nested
    @DisplayName("Serialization")
    class Serialization {

        @Test
        @DisplayName("should be preserved after serialization and deserialization")
        void serialization_shouldPreserveObjectState() {
            CategoryLabelPosition serializedAndDeserialized = TestUtils.serialises(basePosition);
            assertEquals(basePosition, serializedAndDeserialized);
        }
    }
}