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
 * --------------------
 * MeterNeedleTest.java
 * --------------------
 * (C) Copyright 2005-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.plot.compass;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link MeterNeedle} class.
 */
public class MeterNeedleTest {

    // Test data constants for better maintainability
    private static final GradientPaint FILL_GRADIENT = 
        new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.BLUE);
    
    private static final GradientPaint OUTLINE_GRADIENT = 
        new GradientPaint(5.0f, 6.0f, Color.RED, 7.0f, 8.0f, Color.BLUE);
    
    private static final GradientPaint HIGHLIGHT_GRADIENT = 
        new GradientPaint(9.0f, 0.0f, Color.RED, 1.0f, 2.0f, Color.BLUE);
    
    private static final Stroke TEST_STROKE = new BasicStroke(1.23f);
    
    private static final double ROTATE_X_VALUE = 1.23;
    private static final double ROTATE_Y_VALUE = 4.56;
    private static final int SIZE_VALUE = 11;

    /**
     * Verifies that the equals() method correctly distinguishes between all MeterNeedle fields.
     * This test ensures that two MeterNeedle objects are equal only when all their properties match.
     */
    @Test
    public void testEquals_AllFieldsAreComparedCorrectly() {
        // Given: Two identical MeterNeedle instances
        MeterNeedle firstNeedle = new LineNeedle();
        MeterNeedle secondNeedle = new LineNeedle();
        
        // Then: They should be equal initially
        assertEquals(firstNeedle, secondNeedle, "Two default MeterNeedle instances should be equal");

        // Test fillPaint property
        testFieldEquality(
            firstNeedle, secondNeedle,
            needle -> needle.setFillPaint(FILL_GRADIENT),
            "MeterNeedle instances should differ when fillPaint is different"
        );

        // Test outlinePaint property
        testFieldEquality(
            firstNeedle, secondNeedle,
            needle -> needle.setOutlinePaint(OUTLINE_GRADIENT),
            "MeterNeedle instances should differ when outlinePaint is different"
        );

        // Test highlightPaint property
        testFieldEquality(
            firstNeedle, secondNeedle,
            needle -> needle.setHighlightPaint(HIGHLIGHT_GRADIENT),
            "MeterNeedle instances should differ when highlightPaint is different"
        );

        // Test outlineStroke property
        testFieldEquality(
            firstNeedle, secondNeedle,
            needle -> needle.setOutlineStroke(TEST_STROKE),
            "MeterNeedle instances should differ when outlineStroke is different"
        );

        // Test rotateX property
        testFieldEquality(
            firstNeedle, secondNeedle,
            needle -> needle.setRotateX(ROTATE_X_VALUE),
            "MeterNeedle instances should differ when rotateX is different"
        );

        // Test rotateY property
        testFieldEquality(
            firstNeedle, secondNeedle,
            needle -> needle.setRotateY(ROTATE_Y_VALUE),
            "MeterNeedle instances should differ when rotateY is different"
        );

        // Test size property
        testFieldEquality(
            firstNeedle, secondNeedle,
            needle -> needle.setSize(SIZE_VALUE),
            "MeterNeedle instances should differ when size is different"
        );
    }

    /**
     * Helper method to test equality behavior for a specific field.
     * This reduces code duplication and makes the test pattern more explicit.
     * 
     * @param firstNeedle the first needle to compare
     * @param secondNeedle the second needle to compare
     * @param fieldSetter a function that sets a specific field on a needle
     * @param inequalityMessage error message when needles should not be equal
     */
    private void testFieldEquality(MeterNeedle firstNeedle, 
                                 MeterNeedle secondNeedle,
                                 java.util.function.Consumer<MeterNeedle> fieldSetter,
                                 String inequalityMessage) {
        // When: Only the first needle's field is modified
        fieldSetter.accept(firstNeedle);
        
        // Then: The needles should not be equal
        assertNotEquals(firstNeedle, secondNeedle, inequalityMessage);
        
        // When: The second needle's field is set to the same value
        fieldSetter.accept(secondNeedle);
        
        // Then: The needles should be equal again
        assertEquals(firstNeedle, secondNeedle, 
            "MeterNeedle instances should be equal when all fields match");
    }
}