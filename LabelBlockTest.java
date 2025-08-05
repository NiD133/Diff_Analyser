/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * -------------------
 * LabelBlockTest.java
 * -------------------
 * (C) Copyright 2005-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.block;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link LabelBlock} class, focusing on correctness and contract adherence.
 */
@DisplayName("LabelBlock")
class LabelBlockTest {

    private static final Font TEST_FONT = new Font("Dialog", Font.PLAIN, 12);
    private static final Paint TEST_PAINT = Color.RED;
    private static final String TEST_TEXT = "ABC";

    /**
     * Tests for the equals() method.
     */
    @Nested
    @DisplayName("equals() contract")
    class EqualsContract {

        private LabelBlock block1;
        private LabelBlock block2;

        @BeforeEach
        void setUp() {
            block1 = new LabelBlock(TEST_TEXT, TEST_FONT, TEST_PAINT);
            block2 = new LabelBlock(TEST_TEXT, TEST_FONT, TEST_PAINT);
        }

        @Test
        @DisplayName("should be true for two blocks with identical properties")
        void equals_isTrueForIdenticalProperties() {
            assertEquals(block1, block2);
        }

        @Test
        @DisplayName("should be false when text differs")
        void equals_isFalseWhenTextDiffers() {
            // Arrange
            LabelBlock differentBlock = new LabelBlock("XYZ", TEST_FONT, TEST_PAINT);

            // Act & Assert
            assertNotEquals(block1, differentBlock);
        }

        @Test
        @DisplayName("should be false when font differs")
        void equals_isFalseWhenFontDiffers() {
            // Arrange
            Font differentFont = new Font("Dialog", Font.BOLD, 14);
            LabelBlock differentBlock = new LabelBlock(TEST_TEXT, differentFont, TEST_PAINT);

            // Act & Assert
            assertNotEquals(block1, differentBlock);
        }

        @Test
        @DisplayName("should be false when paint differs")
        void equals_isFalseWhenPaintDiffers() {
            // Arrange
            LabelBlock differentBlock = new LabelBlock(TEST_TEXT, TEST_FONT, Color.BLUE);

            // Act & Assert
            assertNotEquals(block1, differentBlock);
        }

        @Test
        @DisplayName("should be false when tool tip text differs")
        void equals_isFalseWhenToolTipTextDiffers() {
            // Arrange
            block2.setToolTipText("A tooltip");

            // Act & Assert
            assertNotEquals(block1, block2);
        }

        @Test
        @DisplayName("should be false when URL text differs")
        void equals_isFalseWhenURLTextDiffers() {
            // Arrange
            block2.setURLText("A URL");

            // Act & Assert
            assertNotEquals(block1, block2);
        }

        @Test
        @DisplayName("should be false when content alignment point differs")
        void equals_isFalseWhenContentAlignmentPointDiffers() {
            // Arrange
            block2.setContentAlignmentPoint(TextBlockAnchor.CENTER_RIGHT);

            // Act & Assert
            assertNotEquals(block1, block2);
        }

        @Test
        @DisplayName("should be false when text anchor differs")
        void equals_isFalseWhenTextAnchorDiffers() {
            // Arrange
            block2.setTextAnchor(RectangleAnchor.BOTTOM_RIGHT);

            // Act & Assert
            assertNotEquals(block1, block2);
        }
    }

    /**
     * Verifies that cloning produces an independent and equal copy of the object.
     */
    @Test
    @DisplayName("cloning should produce an independent copy")
    void clone_shouldProduceIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        LabelBlock original = new LabelBlock(TEST_TEXT, TEST_FONT, TEST_PAINT);
        original.setToolTipText("Original Tooltip");

        // Act
        LabelBlock clone = (LabelBlock) original.clone();

        // Assert
        assertNotSame(original, clone, "Clone must be a new object instance.");
        assertEquals(original, clone, "Clone must be equal to the original object.");

        // Verify independence by modifying the clone
        clone.setToolTipText("Modified Tooltip");
        assertNotEquals(original.getToolTipText(), clone.getToolTipText(),
                "Modifying the clone's tooltip should not affect the original.");
    }

    /**
     * Verifies that a LabelBlock instance can be serialized and deserialized
     * without losing its state.
     */
    @Test
    @DisplayName("serialization should preserve object state")
    void serialization_shouldPreserveObjectState() {
        // Arrange
        GradientPaint gradientPaint = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.BLUE);
        LabelBlock originalBlock = new LabelBlock("Serializable", TEST_FONT, gradientPaint);

        // Act
        LabelBlock deserializedBlock = TestUtils.serialised(originalBlock);

        // Assert
        assertEquals(originalBlock, deserializedBlock);
    }
}