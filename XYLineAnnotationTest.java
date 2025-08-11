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
 * -------------------------
 * XYLineAnnotationTest.java
 * -------------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.annotations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    private static final double EPSILON = 0.000000001;
    private static final Stroke DEFAULT_STROKE = new BasicStroke(2.0f);
    private static final Color DEFAULT_PAINT = Color.BLUE;
    private static final double X1 = 10.0;
    private static final double Y1 = 20.0;
    private static final double X2 = 100.0;
    private static final double Y2 = 200.0;

    // Helper method to create a default annotation
    private XYLineAnnotation createDefaultAnnotation() {
        return new XYLineAnnotation(X1, Y1, X2, Y2, DEFAULT_STROKE, DEFAULT_PAINT);
    }

    // Constructor Tests ======================================================

    @Test
    public void constructor_InitializesFieldsCorrectly() {
        XYLineAnnotation annotation = createDefaultAnnotation();
        
        assertEquals(X1, annotation.getX1(), EPSILON, "X1");
        assertEquals(Y1, annotation.getY1(), EPSILON, "Y1");
        assertEquals(X2, annotation.getX2(), EPSILON, "X2");
        assertEquals(Y2, annotation.getY2(), EPSILON, "Y2");
        assertEquals(DEFAULT_STROKE, annotation.getStroke(), "Stroke");
        assertEquals(DEFAULT_PAINT, annotation.getPaint(), "Paint");
    }

    @Test
    public void constructor_ThrowsException_WhenX1IsNaN() {
        assertThrows(IllegalArgumentException.class, () -> 
            new XYLineAnnotation(Double.NaN, Y1, X2, Y2, DEFAULT_STROKE, DEFAULT_PAINT)
        );
    }

    @Test
    public void constructor_ThrowsException_WhenY1IsNaN() {
        assertThrows(IllegalArgumentException.class, () -> 
            new XYLineAnnotation(X1, Double.NaN, X2, Y2, DEFAULT_STROKE, DEFAULT_PAINT)
        );
    }

    @Test
    public void constructor_ThrowsException_WhenX2IsNaN() {
        assertThrows(IllegalArgumentException.class, () -> 
            new XYLineAnnotation(X1, Y1, Double.NaN, Y2, DEFAULT_STROKE, DEFAULT_PAINT)
        );
    }

    @Test
    public void constructor_ThrowsException_WhenY2IsNaN() {
        assertThrows(IllegalArgumentException.class, () -> 
            new XYLineAnnotation(X1, Y1, X2, Double.NaN, DEFAULT_STROKE, DEFAULT_PAINT)
        );
    }

    @Test
    public void constructor_ThrowsException_WhenStrokeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            new XYLineAnnotation(X1, Y1, X2, Y2, null, DEFAULT_PAINT)
        );
    }

    @Test
    public void constructor_ThrowsException_WhenPaintIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            new XYLineAnnotation(X1, Y1, X2, Y2, DEFAULT_STROKE, null)
        );
    }

    // equals() Tests ========================================================

    @Test
    public void equals_WithSameObject_ReturnsTrue() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        assertEquals(a1, a1, "Same instance");
    }

    @Test
    public void equals_WithEqualObject_ReturnsTrue() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        XYLineAnnotation a2 = createDefaultAnnotation();
        assertEquals(a1, a2, "Equal instances");
    }

    @Test
    public void equals_WithNull_ReturnsFalse() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        assertNotEquals(null, a1, "Compared to null");
    }

    @Test
    public void equals_WithDifferentClass_ReturnsFalse() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        assertNotEquals(a1, new Object(), "Different class");
    }

    @Test
    public void equals_WithDifferentX1_ReturnsFalse() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        XYLineAnnotation a2 = new XYLineAnnotation(11.0, Y1, X2, Y2, DEFAULT_STROKE, DEFAULT_PAINT);
        assertNotEquals(a1, a2, "Different X1");
    }

    @Test
    public void equals_WithDifferentY1_ReturnsFalse() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        XYLineAnnotation a2 = new XYLineAnnotation(X1, 21.0, X2, Y2, DEFAULT_STROKE, DEFAULT_PAINT);
        assertNotEquals(a1, a2, "Different Y1");
    }

    @Test
    public void equals_WithDifferentX2_ReturnsFalse() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        XYLineAnnotation a2 = new XYLineAnnotation(X1, Y1, 101.0, Y2, DEFAULT_STROKE, DEFAULT_PAINT);
        assertNotEquals(a1, a2, "Different X2");
    }

    @Test
    public void equals_WithDifferentY2_ReturnsFalse() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        XYLineAnnotation a2 = new XYLineAnnotation(X1, Y1, X2, 201.0, DEFAULT_STROKE, DEFAULT_PAINT);
        assertNotEquals(a1, a2, "Different Y2");
    }

    @Test
    public void equals_WithDifferentStroke_ReturnsFalse() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        Stroke differentStroke = new BasicStroke(0.99f);
        XYLineAnnotation a2 = new XYLineAnnotation(X1, Y1, X2, Y2, differentStroke, DEFAULT_PAINT);
        assertNotEquals(a1, a2, "Different Stroke");
    }

    @Test
    public void equals_WithEqualStrokeDifferentInstance_ReturnsTrue() {
        Stroke stroke1 = new BasicStroke(2.0f);
        Stroke stroke2 = new BasicStroke(2.0f);
        XYLineAnnotation a1 = new XYLineAnnotation(X1, Y1, X2, Y2, stroke1, DEFAULT_PAINT);
        XYLineAnnotation a2 = new XYLineAnnotation(X1, Y1, X2, Y2, stroke2, DEFAULT_PAINT);
        assertEquals(a1, a2, "Equal Strokes (different instances)");
    }

    @Test
    public void equals_WithDifferentPaint_ReturnsFalse() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        XYLineAnnotation a2 = new XYLineAnnotation(X1, Y1, X2, Y2, DEFAULT_STROKE, Color.RED);
        assertNotEquals(a1, a2, "Different Paint");
    }

    @Test
    public void equals_WithEqualPaintDifferentInstance_ReturnsTrue() {
        GradientPaint paint1 = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.WHITE);
        GradientPaint paint2 = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.WHITE);
        XYLineAnnotation a1 = new XYLineAnnotation(X1, Y1, X2, Y2, DEFAULT_STROKE, paint1);
        XYLineAnnotation a2 = new XYLineAnnotation(X1, Y1, X2, Y2, DEFAULT_STROKE, paint2);
        assertEquals(a1, a2, "Equal Paints (different instances)");
    }

    // hashCode() Tests ======================================================

    @Test
    public void hashCode_ForEqualObjects_IsConsistent() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        XYLineAnnotation a2 = createDefaultAnnotation();
        assertEquals(a1.hashCode(), a2.hashCode(), "Equal objects must have same hashCode");
    }

    // Cloning Tests =========================================================

    @Test
    public void cloning_CreatesEqualButDistinctInstance() throws CloneNotSupportedException {
        XYLineAnnotation a1 = createDefaultAnnotation();
        XYLineAnnotation a2 = (XYLineAnnotation) a1.clone();
        
        assertNotSame(a1, a2, "Cloned instance");
        assertSame(a1.getClass(), a2.getClass(), "Same class");
        assertEquals(a1, a2, "Equal after cloning");
    }

    // PublicCloneable Tests =================================================

    @Test
    public void publicCloneable_IsImplemented() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        assertTrue(a1 instanceof PublicCloneable, "PublicCloneable implemented");
    }

    // Serialization Tests ===================================================

    @Test
    public void serialization_DeserializedObjectMatchesOriginal() {
        XYLineAnnotation a1 = createDefaultAnnotation();
        XYLineAnnotation a2 = TestUtils.serialised(a1);
        assertEquals(a1, a2, "Deserialized object");
    }
}