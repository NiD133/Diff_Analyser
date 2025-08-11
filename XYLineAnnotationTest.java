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
    
    // Test data constants for better readability and maintainability
    private static final double START_X = 10.0;
    private static final double START_Y = 20.0;
    private static final double END_X = 100.0;
    private static final double END_Y = 200.0;
    private static final Stroke DEFAULT_STROKE = new BasicStroke(2.0f);
    private static final Color DEFAULT_COLOR = Color.BLUE;

    @Test
    public void testConstructor_ValidParameters_SetsAllFieldsCorrectly() {
        // Given
        Stroke expectedStroke = DEFAULT_STROKE;
        Color expectedColor = DEFAULT_COLOR;
        
        // When
        XYLineAnnotation annotation = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y, expectedStroke, expectedColor);
        
        // Then
        assertEquals(START_X, annotation.getX1(), EPSILON, "Start X coordinate should be set correctly");
        assertEquals(START_Y, annotation.getY1(), EPSILON, "Start Y coordinate should be set correctly");
        assertEquals(END_X, annotation.getX2(), EPSILON, "End X coordinate should be set correctly");
        assertEquals(END_Y, annotation.getY2(), EPSILON, "End Y coordinate should be set correctly");
        assertEquals(expectedStroke, annotation.getStroke(), "Stroke should be set correctly");
        assertEquals(expectedColor, annotation.getPaint(), "Paint color should be set correctly");
    }
    
    @Test
    public void testConstructor_NaNCoordinates_ThrowsIllegalArgumentException() {
        Stroke validStroke = DEFAULT_STROKE;
        Color validColor = DEFAULT_COLOR;
        
        // Test NaN for each coordinate parameter
        assertThrows(IllegalArgumentException.class, () -> {
            new XYLineAnnotation(Double.NaN, START_Y, END_X, END_Y, validStroke, validColor);
        }, "Constructor should reject NaN for start X coordinate");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new XYLineAnnotation(START_X, Double.NaN, END_X, END_Y, validStroke, validColor);
        }, "Constructor should reject NaN for start Y coordinate");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new XYLineAnnotation(START_X, START_Y, Double.NaN, END_Y, validStroke, validColor);
        }, "Constructor should reject NaN for end X coordinate");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new XYLineAnnotation(START_X, START_Y, END_X, Double.NaN, validStroke, validColor);
        }, "Constructor should reject NaN for end Y coordinate");
    }
    
    @Test
    public void testConstructor_NullParameters_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new XYLineAnnotation(START_X, START_Y, END_X, END_Y, null, DEFAULT_COLOR);
        }, "Constructor should reject null stroke");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new XYLineAnnotation(START_X, START_Y, END_X, END_Y, DEFAULT_STROKE, null);
        }, "Constructor should reject null paint");
    }
    
    @Test
    public void testEquals_IdenticalAnnotations_ReturnsTrue() {
        // Given
        XYLineAnnotation annotation1 = createStandardAnnotation();
        XYLineAnnotation annotation2 = createStandardAnnotation();
        
        // Then
        assertEquals(annotation1, annotation2, "Identical annotations should be equal");
        assertEquals(annotation2, annotation1, "Equality should be symmetric");
    }

    @Test
    public void testEquals_DifferentStartXCoordinate_ReturnsFalse() {
        // Given
        XYLineAnnotation originalAnnotation = createStandardAnnotation();
        XYLineAnnotation modifiedAnnotation = new XYLineAnnotation(
            START_X + 1.0, START_Y, END_X, END_Y, DEFAULT_STROKE, DEFAULT_COLOR);
        
        // Then
        assertNotEquals(originalAnnotation, modifiedAnnotation, 
            "Annotations with different start X coordinates should not be equal");
        
        // Verify they become equal when both have the same modified value
        XYLineAnnotation anotherModifiedAnnotation = new XYLineAnnotation(
            START_X + 1.0, START_Y, END_X, END_Y, DEFAULT_STROKE, DEFAULT_COLOR);
        assertEquals(modifiedAnnotation, anotherModifiedAnnotation,
            "Annotations with same modified start X coordinate should be equal");
    }

    @Test
    public void testEquals_DifferentStartYCoordinate_ReturnsFalse() {
        // Given
        XYLineAnnotation originalAnnotation = createStandardAnnotation();
        XYLineAnnotation modifiedAnnotation = new XYLineAnnotation(
            START_X, START_Y + 1.0, END_X, END_Y, DEFAULT_STROKE, DEFAULT_COLOR);
        
        // Then
        assertNotEquals(originalAnnotation, modifiedAnnotation,
            "Annotations with different start Y coordinates should not be equal");
        
        // Verify they become equal when both have the same modified value
        XYLineAnnotation anotherModifiedAnnotation = new XYLineAnnotation(
            START_X, START_Y + 1.0, END_X, END_Y, DEFAULT_STROKE, DEFAULT_COLOR);
        assertEquals(modifiedAnnotation, anotherModifiedAnnotation,
            "Annotations with same modified start Y coordinate should be equal");
    }

    @Test
    public void testEquals_DifferentEndXCoordinate_ReturnsFalse() {
        // Given
        XYLineAnnotation originalAnnotation = createStandardAnnotation();
        XYLineAnnotation modifiedAnnotation = new XYLineAnnotation(
            START_X, START_Y, END_X + 1.0, END_Y, DEFAULT_STROKE, DEFAULT_COLOR);
        
        // Then
        assertNotEquals(originalAnnotation, modifiedAnnotation,
            "Annotations with different end X coordinates should not be equal");
        
        // Verify they become equal when both have the same modified value
        XYLineAnnotation anotherModifiedAnnotation = new XYLineAnnotation(
            START_X, START_Y, END_X + 1.0, END_Y, DEFAULT_STROKE, DEFAULT_COLOR);
        assertEquals(modifiedAnnotation, anotherModifiedAnnotation,
            "Annotations with same modified end X coordinate should be equal");
    }

    @Test
    public void testEquals_DifferentEndYCoordinate_ReturnsFalse() {
        // Given
        XYLineAnnotation originalAnnotation = createStandardAnnotation();
        XYLineAnnotation modifiedAnnotation = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y + 1.0, DEFAULT_STROKE, DEFAULT_COLOR);
        
        // Then
        assertNotEquals(originalAnnotation, modifiedAnnotation,
            "Annotations with different end Y coordinates should not be equal");
        
        // Verify they become equal when both have the same modified value
        XYLineAnnotation anotherModifiedAnnotation = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y + 1.0, DEFAULT_STROKE, DEFAULT_COLOR);
        assertEquals(modifiedAnnotation, anotherModifiedAnnotation,
            "Annotations with same modified end Y coordinate should be equal");
    }

    @Test
    public void testEquals_DifferentStroke_ReturnsFalse() {
        // Given
        XYLineAnnotation originalAnnotation = createStandardAnnotation();
        Stroke differentStroke = new BasicStroke(0.99f);
        XYLineAnnotation modifiedAnnotation = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y, differentStroke, DEFAULT_COLOR);
        
        // Then
        assertNotEquals(originalAnnotation, modifiedAnnotation,
            "Annotations with different strokes should not be equal");
        
        // Verify they become equal when both have the same modified stroke
        XYLineAnnotation anotherModifiedAnnotation = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y, differentStroke, DEFAULT_COLOR);
        assertEquals(modifiedAnnotation, anotherModifiedAnnotation,
            "Annotations with same modified stroke should be equal");
    }

    @Test
    public void testEquals_DifferentPaint_ReturnsFalse() {
        // Given
        XYLineAnnotation originalAnnotation = createStandardAnnotation();
        GradientPaint gradientPaint1 = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.WHITE);
        XYLineAnnotation modifiedAnnotation = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y, DEFAULT_STROKE, gradientPaint1);
        
        // Then
        assertNotEquals(originalAnnotation, modifiedAnnotation,
            "Annotations with different paints should not be equal");
        
        // Verify they become equal when both have equivalent gradient paints
        GradientPaint gradientPaint2 = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.WHITE);
        XYLineAnnotation anotherModifiedAnnotation = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y, DEFAULT_STROKE, gradientPaint2);
        assertEquals(modifiedAnnotation, anotherModifiedAnnotation,
            "Annotations with equivalent gradient paints should be equal");
    }

    @Test
    public void testHashCode_EqualAnnotations_ReturnSameHashCode() {
        // Given
        XYLineAnnotation annotation1 = createStandardAnnotation();
        XYLineAnnotation annotation2 = createStandardAnnotation();
        
        // When
        int hashCode1 = annotation1.hashCode();
        int hashCode2 = annotation2.hashCode();
        
        // Then
        assertEquals(annotation1, annotation2, "Annotations should be equal");
        assertEquals(hashCode1, hashCode2, "Equal annotations must have the same hash code");
    }

    @Test
    public void testCloning_ValidAnnotation_CreatesIndependentCopy() throws CloneNotSupportedException {
        // Given
        XYLineAnnotation originalAnnotation = createStandardAnnotation();
        
        // When
        XYLineAnnotation clonedAnnotation = (XYLineAnnotation) originalAnnotation.clone();
        
        // Then
        assertNotSame(originalAnnotation, clonedAnnotation, 
            "Cloned annotation should be a different object instance");
        assertSame(originalAnnotation.getClass(), clonedAnnotation.getClass(), 
            "Cloned annotation should have the same class");
        assertEquals(originalAnnotation, clonedAnnotation, 
            "Cloned annotation should be equal to the original");
    }

    @Test
    public void testPublicCloneable_AnnotationInstance_ImplementsInterface() {
        // Given
        XYLineAnnotation annotation = createStandardAnnotation();
        
        // Then
        assertTrue(annotation instanceof PublicCloneable, 
            "XYLineAnnotation should implement PublicCloneable interface");
    }

    @Test
    public void testSerialization_ValidAnnotation_PreservesStateAfterSerialization() {
        // Given
        XYLineAnnotation originalAnnotation = createStandardAnnotation();
        
        // When
        XYLineAnnotation deserializedAnnotation = TestUtils.serialised(originalAnnotation);
        
        // Then
        assertEquals(originalAnnotation, deserializedAnnotation, 
            "Deserialized annotation should be equal to the original");
    }
    
    /**
     * Helper method to create a standard XYLineAnnotation for testing.
     * This reduces code duplication and makes tests more maintainable.
     */
    private XYLineAnnotation createStandardAnnotation() {
        return new XYLineAnnotation(START_X, START_Y, END_X, END_Y, DEFAULT_STROKE, DEFAULT_COLOR);
    }
}