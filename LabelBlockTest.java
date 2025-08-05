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

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;

import org.jfree.chart.TestUtils;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.internal.CloneUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    // Test data constants
    private static final String DEFAULT_TEXT = "ABC";
    private static final String ALTERNATIVE_TEXT = "XYZ";
    private static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 12);
    private static final Font BOLD_FONT = new Font("Dialog", Font.BOLD, 12);
    private static final Color DEFAULT_COLOR = Color.RED;
    private static final Color ALTERNATIVE_COLOR = Color.BLUE;
    private static final String TEST_TOOLTIP = "Tooltip";
    private static final String TEST_URL = "URL";

    /**
     * Tests that the equals() method correctly identifies equal and unequal LabelBlock instances
     * by verifying all significant fields: text, font, color, tooltip, URL, content alignment, and text anchor.
     */
    @Test
    public void testEquals() {
        // Test basic equality with identical constructor parameters
        LabelBlock labelBlock1 = createDefaultLabelBlock();
        LabelBlock labelBlock2 = createDefaultLabelBlock();
        
        assertEquals(labelBlock1, labelBlock2, "LabelBlocks with identical parameters should be equal");
        assertEquals(labelBlock2, labelBlock2, "LabelBlock should be equal to itself");

        // Test inequality and equality for text field
        testTextFieldEquality();
        
        // Test inequality and equality for font field
        testFontFieldEquality();
        
        // Test inequality and equality for color field
        testColorFieldEquality();
        
        // Test inequality and equality for tooltip field
        testTooltipFieldEquality();
        
        // Test inequality and equality for URL field
        testUrlFieldEquality();
        
        // Test inequality and equality for content alignment field
        testContentAlignmentFieldEquality();
        
        // Test inequality and equality for text anchor field
        testTextAnchorFieldEquality();
    }

    private void testTextFieldEquality() {
        LabelBlock blockWithDefaultText = createDefaultLabelBlock();
        LabelBlock blockWithAlternativeText = new LabelBlock(ALTERNATIVE_TEXT, DEFAULT_FONT, DEFAULT_COLOR);
        
        assertNotEquals(blockWithDefaultText, blockWithAlternativeText, 
            "LabelBlocks with different text should not be equal");
        
        LabelBlock anotherBlockWithAlternativeText = new LabelBlock(ALTERNATIVE_TEXT, DEFAULT_FONT, DEFAULT_COLOR);
        assertEquals(blockWithAlternativeText, anotherBlockWithAlternativeText, 
            "LabelBlocks with same alternative text should be equal");
    }

    private void testFontFieldEquality() {
        LabelBlock blockWithDefaultFont = new LabelBlock(ALTERNATIVE_TEXT, DEFAULT_FONT, DEFAULT_COLOR);
        LabelBlock blockWithBoldFont = new LabelBlock(ALTERNATIVE_TEXT, BOLD_FONT, DEFAULT_COLOR);
        
        assertNotEquals(blockWithDefaultFont, blockWithBoldFont, 
            "LabelBlocks with different fonts should not be equal");
        
        LabelBlock anotherBlockWithBoldFont = new LabelBlock(ALTERNATIVE_TEXT, BOLD_FONT, DEFAULT_COLOR);
        assertEquals(blockWithBoldFont, anotherBlockWithBoldFont, 
            "LabelBlocks with same bold font should be equal");
    }

    private void testColorFieldEquality() {
        LabelBlock blockWithRedColor = new LabelBlock(ALTERNATIVE_TEXT, BOLD_FONT, DEFAULT_COLOR);
        LabelBlock blockWithBlueColor = new LabelBlock(ALTERNATIVE_TEXT, BOLD_FONT, ALTERNATIVE_COLOR);
        
        assertNotEquals(blockWithRedColor, blockWithBlueColor, 
            "LabelBlocks with different colors should not be equal");
        
        LabelBlock anotherBlockWithBlueColor = new LabelBlock(ALTERNATIVE_TEXT, BOLD_FONT, ALTERNATIVE_COLOR);
        assertEquals(blockWithBlueColor, anotherBlockWithBlueColor, 
            "LabelBlocks with same blue color should be equal");
    }

    private void testTooltipFieldEquality() {
        LabelBlock blockWithoutTooltip = new LabelBlock(ALTERNATIVE_TEXT, BOLD_FONT, ALTERNATIVE_COLOR);
        LabelBlock blockWithTooltip = new LabelBlock(ALTERNATIVE_TEXT, BOLD_FONT, ALTERNATIVE_COLOR);
        blockWithTooltip.setToolTipText(TEST_TOOLTIP);
        
        assertNotEquals(blockWithoutTooltip, blockWithTooltip, 
            "LabelBlocks with different tooltips should not be equal");
        
        blockWithoutTooltip.setToolTipText(TEST_TOOLTIP);
        assertEquals(blockWithoutTooltip, blockWithTooltip, 
            "LabelBlocks with same tooltip should be equal");
    }

    private void testUrlFieldEquality() {
        LabelBlock blockWithoutUrl = createFullyConfiguredLabelBlock();
        LabelBlock blockWithUrl = createFullyConfiguredLabelBlock();
        blockWithUrl.setURLText(TEST_URL);
        
        assertNotEquals(blockWithoutUrl, blockWithUrl, 
            "LabelBlocks with different URLs should not be equal");
        
        blockWithoutUrl.setURLText(TEST_URL);
        assertEquals(blockWithoutUrl, blockWithUrl, 
            "LabelBlocks with same URL should be equal");
    }

    private void testContentAlignmentFieldEquality() {
        LabelBlock blockWithDefaultAlignment = createFullyConfiguredLabelBlockWithUrl();
        LabelBlock blockWithCenterRightAlignment = createFullyConfiguredLabelBlockWithUrl();
        blockWithCenterRightAlignment.setContentAlignmentPoint(TextBlockAnchor.CENTER_RIGHT);
        
        assertNotEquals(blockWithDefaultAlignment, blockWithCenterRightAlignment, 
            "LabelBlocks with different content alignments should not be equal");
        
        blockWithDefaultAlignment.setContentAlignmentPoint(TextBlockAnchor.CENTER_RIGHT);
        assertEquals(blockWithDefaultAlignment, blockWithCenterRightAlignment, 
            "LabelBlocks with same content alignment should be equal");
    }

    private void testTextAnchorFieldEquality() {
        LabelBlock blockWithDefaultAnchor = createFullyConfiguredLabelBlockWithAlignment();
        LabelBlock blockWithBottomRightAnchor = createFullyConfiguredLabelBlockWithAlignment();
        blockWithBottomRightAnchor.setTextAnchor(RectangleAnchor.BOTTOM_RIGHT);
        
        assertNotEquals(blockWithDefaultAnchor, blockWithBottomRightAnchor, 
            "LabelBlocks with different text anchors should not be equal");
        
        blockWithDefaultAnchor.setTextAnchor(RectangleAnchor.BOTTOM_RIGHT);
        assertEquals(blockWithDefaultAnchor, blockWithBottomRightAnchor, 
            "LabelBlocks with same text anchor should be equal");
    }

    /**
     * Tests that cloning creates a separate instance with the same content.
     * Verifies that the clone is not the same object reference but has identical content.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        LabelBlock originalBlock = createDefaultLabelBlock();
        LabelBlock clonedBlock = CloneUtils.clone(originalBlock);
        
        assertNotSame(originalBlock, clonedBlock, 
            "Cloned LabelBlock should be a different object instance");
        assertSame(originalBlock.getClass(), clonedBlock.getClass(), 
            "Cloned LabelBlock should be the same class");
        assertEquals(originalBlock, clonedBlock, 
            "Cloned LabelBlock should have equal content to original");
    }

    /**
     * Tests serialization and deserialization of LabelBlock instances.
     * Uses a GradientPaint to ensure complex Paint objects are properly serialized.
     */
    @Test
    public void testSerialization() {
        GradientPaint gradientPaint = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.BLUE);
        LabelBlock originalBlock = new LabelBlock(DEFAULT_TEXT, DEFAULT_FONT, gradientPaint);
        
        LabelBlock deserializedBlock = TestUtils.serialised(originalBlock);
        
        assertEquals(originalBlock, deserializedBlock, 
            "Deserialized LabelBlock should be equal to original");
    }

    // Helper methods for creating test instances

    private LabelBlock createDefaultLabelBlock() {
        return new LabelBlock(DEFAULT_TEXT, DEFAULT_FONT, DEFAULT_COLOR);
    }

    private LabelBlock createFullyConfiguredLabelBlock() {
        LabelBlock block = new LabelBlock(ALTERNATIVE_TEXT, BOLD_FONT, ALTERNATIVE_COLOR);
        block.setToolTipText(TEST_TOOLTIP);
        return block;
    }

    private LabelBlock createFullyConfiguredLabelBlockWithUrl() {
        LabelBlock block = createFullyConfiguredLabelBlock();
        block.setURLText(TEST_URL);
        return block;
    }

    private LabelBlock createFullyConfiguredLabelBlockWithAlignment() {
        LabelBlock block = createFullyConfiguredLabelBlockWithUrl();
        block.setContentAlignmentPoint(TextBlockAnchor.CENTER_RIGHT);
        return block;
    }
}