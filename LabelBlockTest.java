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
 * Tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    // Constants for test values
    private static final String DEFAULT_TEXT = "ABC";
    private static final String DIFFERENT_TEXT = "XYZ";
    private static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 12);
    private static final Font DIFFERENT_FONT = new Font("Dialog", Font.BOLD, 12);
    private static final Paint DEFAULT_PAINT = Color.RED;
    private static final Paint DIFFERENT_PAINT = Color.BLUE;
    private static final String TOOLTIP_TEXT = "Tooltip";
    private static final String URL_TEXT = "URL";
    private static final TextBlockAnchor CONTENT_ANCHOR = TextBlockAnchor.CENTER_RIGHT;
    private static final RectangleAnchor TEXT_ANCHOR = RectangleAnchor.BOTTOM_RIGHT;

    /**
     * Creates a base LabelBlock instance for testing.
     */
    private LabelBlock createBaseLabelBlock() {
        return new LabelBlock(DEFAULT_TEXT, DEFAULT_FONT, DEFAULT_PAINT);
    }

    @Test
    public void equals_SameObject() {
        LabelBlock block = createBaseLabelBlock();
        assertEquals(block, block);
    }

    @Test
    public void equals_NullComparison() {
        LabelBlock block = createBaseLabelBlock();
        assertNotEquals(null, block);
    }

    @Test
    public void equals_DifferentClass() {
        LabelBlock block = createBaseLabelBlock();
        assertNotEquals(block, new Object());
    }

    @Test
    public void equals_EqualAfterConstruction() {
        LabelBlock b1 = createBaseLabelBlock();
        LabelBlock b2 = createBaseLabelBlock();
        assertEquals(b1, b2);
    }

    @Test
    public void equals_DifferentText() {
        LabelBlock base = createBaseLabelBlock();
        LabelBlock differentText = new LabelBlock(DIFFERENT_TEXT, DEFAULT_FONT, DEFAULT_PAINT);
        assertNotEquals(base, differentText);
    }

    @Test
    public void equals_DifferentFont() {
        LabelBlock base = createBaseLabelBlock();
        LabelBlock differentFont = new LabelBlock(DEFAULT_TEXT, DIFFERENT_FONT, DEFAULT_PAINT);
        assertNotEquals(base, differentFont);
    }

    @Test
    public void equals_DifferentPaint() {
        LabelBlock base = createBaseLabelBlock();
        LabelBlock differentPaint = new LabelBlock(DEFAULT_TEXT, DEFAULT_FONT, DIFFERENT_PAINT);
        assertNotEquals(base, differentPaint);
    }

    @Test
    public void equals_DifferentToolTipText() {
        LabelBlock b1 = createBaseLabelBlock();
        LabelBlock b2 = createBaseLabelBlock();
        
        b1.setToolTipText(TOOLTIP_TEXT);
        assertNotEquals(b1, b2);
        
        b2.setToolTipText(TOOLTIP_TEXT);
        assertEquals(b1, b2);
    }

    @Test
    public void equals_DifferentUrlText() {
        LabelBlock b1 = createBaseLabelBlock();
        LabelBlock b2 = createBaseLabelBlock();
        
        b1.setURLText(URL_TEXT);
        assertNotEquals(b1, b2);
        
        b2.setURLText(URL_TEXT);
        assertEquals(b1, b2);
    }

    @Test
    public void equals_DifferentContentAlignmentPoint() {
        LabelBlock b1 = createBaseLabelBlock();
        LabelBlock b2 = createBaseLabelBlock();
        
        b1.setContentAlignmentPoint(CONTENT_ANCHOR);
        assertNotEquals(b1, b2);
        
        b2.setContentAlignmentPoint(CONTENT_ANCHOR);
        assertEquals(b1, b2);
    }

    @Test
    public void equals_DifferentTextAnchor() {
        LabelBlock b1 = createBaseLabelBlock();
        LabelBlock b2 = createBaseLabelBlock();
        
        b1.setTextAnchor(TEXT_ANCHOR);
        assertNotEquals(b1, b2);
        
        b2.setTextAnchor(TEXT_ANCHOR);
        assertEquals(b1, b2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        LabelBlock original = createBaseLabelBlock();
        LabelBlock clone = CloneUtils.clone(original);
        
        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        GradientPaint gp = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.BLUE);
        LabelBlock original = new LabelBlock(DEFAULT_TEXT, DEFAULT_FONT, gp);
        LabelBlock deserialized = TestUtils.serialised(original);
        
        assertEquals(original, deserialized);
    }
}