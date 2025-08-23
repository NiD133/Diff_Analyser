package org.jfree.chart.block;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Font;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * A collection of tests for the equals() and hashCode() methods of the {@link LabelBlock} class.
 */
@DisplayName("LabelBlock Equality")
class LabelBlockTestTest1 {

    // Common attributes for creating LabelBlock instances
    private static final String DEFAULT_TEXT = "ABC";
    private static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 12);
    private static final Color DEFAULT_PAINT = Color.RED;

    private LabelBlock block1;
    private LabelBlock block2;

    @BeforeEach
    void setUp() {
        // Create two identical LabelBlock instances before each test
        block1 = new LabelBlock(DEFAULT_TEXT, DEFAULT_FONT, DEFAULT_PAINT);
        block2 = new LabelBlock(DEFAULT_TEXT, DEFAULT_FONT, DEFAULT_PAINT);
    }

    @Test
    @DisplayName("should be equal to an identical instance")
    void testEquals_withIdenticalInstance() {
        assertEquals(block1, block2, "Blocks with identical attributes should be equal");
    }

    @Test
    @DisplayName("should have a consistent hash code for identical instances")
    void testHashCode_isConsistentForEqualObjects() {
        assertEquals(block1.hashCode(), block2.hashCode(), "Hash codes should be the same for equal objects");
    }

    @Test
    @DisplayName("should not be equal to null")
    void testEquals_withNull() {
        assertNotEquals(null, block1, "A block should not be equal to null");
    }

    @Test
    @DisplayName("should not be equal to an object of a different type")
    void testEquals_withDifferentObjectType() {
        assertNotEquals(block1, new Object(), "A block should not be equal to an object of a different class");
    }

    @Nested
    @DisplayName("should differentiate based on")
    class DifferentiatesBy {

        @Test
        @DisplayName("text content")
        void testEquals_differentiatesByText() {
            block1 = new LabelBlock("XYZ", DEFAULT_FONT, DEFAULT_PAINT);
            assertNotEquals(block1, block2);

            block2 = new LabelBlock("XYZ", DEFAULT_FONT, DEFAULT_PAINT);
            assertEquals(block1, block2);
        }

        @Test
        @DisplayName("font")
        void testEquals_differentiatesByFont() {
            block1 = new LabelBlock(DEFAULT_TEXT, new Font("Dialog", Font.BOLD, 12), DEFAULT_PAINT);
            assertNotEquals(block1, block2);

            block2 = new LabelBlock(DEFAULT_TEXT, new Font("Dialog", Font.BOLD, 12), DEFAULT_PAINT);
            assertEquals(block1, block2);
        }

        @Test
        @DisplayName("paint color")
        void testEquals_differentiatesByPaint() {
            block1 = new LabelBlock(DEFAULT_TEXT, DEFAULT_FONT, Color.BLUE);
            assertNotEquals(block1, block2);

            block2 = new LabelBlock(DEFAULT_TEXT, DEFAULT_FONT, Color.BLUE);
            assertEquals(block1, block2);
        }

        @Test
        @DisplayName("tool tip text")
        void testEquals_differentiatesByToolTipText() {
            block1.setToolTipText("Tooltip");
            assertNotEquals(block1, block2);

            block2.setToolTipText("Tooltip");
            assertEquals(block1, block2);
        }

        @Test
        @DisplayName("URL text")
        void testEquals_differentiatesByUrlText() {
            block1.setURLText("URL");
            assertNotEquals(block1, block2);

            block2.setURLText("URL");
            assertEquals(block1, block2);
        }

        @Test
        @DisplayName("content alignment point")
        void testEquals_differentiatesByContentAlignmentPoint() {
            block1.setContentAlignmentPoint(TextBlockAnchor.CENTER_RIGHT);
            assertNotEquals(block1, block2);

            block2.setContentAlignmentPoint(TextBlockAnchor.CENTER_RIGHT);
            assertEquals(block1, block2);
        }

        @Test
        @DisplayName("text anchor")
        void testEquals_differentiatesByTextAnchor() {
            block1.setTextAnchor(RectangleAnchor.BOTTOM_RIGHT);
            assertNotEquals(block1, block2);

            block2.setTextAnchor(RectangleAnchor.BOTTOM_RIGHT);
            assertEquals(block1, block2);
        }
    }
}