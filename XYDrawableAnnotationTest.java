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
 * -----------------------------
 * XYDrawableAnnotationTest.java
 * -----------------------------
 * (C) Copyright 2003-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.annotations;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.jfree.chart.TestUtils;
import org.jfree.chart.Drawable;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    // Test constants for better readability and maintainability
    private static final double DEFAULT_X = 10.0;
    private static final double DEFAULT_Y = 20.0;
    private static final double DEFAULT_WIDTH = 100.0;
    private static final double DEFAULT_HEIGHT = 200.0;
    private static final double DEFAULT_SCALE_FACTOR = 2.0;
    
    private static final double MODIFIED_X = 11.0;
    private static final double MODIFIED_Y = 22.0;
    private static final double MODIFIED_WIDTH = 101.0;
    private static final double MODIFIED_HEIGHT = 202.0;

    /**
     * A simple test implementation of Drawable for testing purposes.
     * This class provides minimal functionality needed for XYDrawableAnnotation tests.
     */
    static class TestDrawable implements Drawable, Cloneable, Serializable {
        
        public TestDrawable() {
            // Default constructor
        }
        
        @Override
        public void draw(Graphics2D g2, Rectangle2D area) {
            // Empty implementation - no actual drawing needed for tests
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            return obj instanceof TestDrawable;
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    /**
     * Helper method to create a standard XYDrawableAnnotation for testing.
     */
    private XYDrawableAnnotation createStandardAnnotation() {
        return new XYDrawableAnnotation(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT, new TestDrawable());
    }

    /**
     * Helper method to create an XYDrawableAnnotation with scale factor.
     */
    private XYDrawableAnnotation createAnnotationWithScaleFactor(double scaleFactor) {
        return new XYDrawableAnnotation(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT, scaleFactor, new TestDrawable());
    }

    /**
     * Tests that the equals method correctly identifies equal annotations and 
     * distinguishes annotations that differ in each field.
     */
    @Test
    public void testEquals() {
        // Test basic equality
        XYDrawableAnnotation annotation1 = createStandardAnnotation();
        XYDrawableAnnotation annotation2 = createStandardAnnotation();
        assertEquals(annotation1, annotation2, "Two annotations with identical parameters should be equal");

        // Test inequality when X coordinate differs
        testFieldInequality(
            createStandardAnnotation(),
            new XYDrawableAnnotation(MODIFIED_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT, new TestDrawable()),
            "Annotations with different X coordinates should not be equal"
        );

        // Test inequality when Y coordinate differs
        testFieldInequality(
            createStandardAnnotation(),
            new XYDrawableAnnotation(DEFAULT_X, MODIFIED_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT, new TestDrawable()),
            "Annotations with different Y coordinates should not be equal"
        );

        // Test inequality when width differs
        testFieldInequality(
            createStandardAnnotation(),
            new XYDrawableAnnotation(DEFAULT_X, DEFAULT_Y, MODIFIED_WIDTH, DEFAULT_HEIGHT, new TestDrawable()),
            "Annotations with different widths should not be equal"
        );

        // Test inequality when height differs
        testFieldInequality(
            createStandardAnnotation(),
            new XYDrawableAnnotation(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, MODIFIED_HEIGHT, new TestDrawable()),
            "Annotations with different heights should not be equal"
        );

        // Test inequality when scale factor differs
        testFieldInequality(
            createStandardAnnotation(),
            createAnnotationWithScaleFactor(DEFAULT_SCALE_FACTOR),
            "Annotations with different scale factors should not be equal"
        );
    }

    /**
     * Helper method to test field inequality and subsequent equality restoration.
     */
    private void testFieldInequality(XYDrawableAnnotation original, XYDrawableAnnotation modified, String message) {
        assertNotEquals(original, modified, message);
        
        // Verify that creating a new annotation with the same modified parameters results in equality
        XYDrawableAnnotation matchingModified = new XYDrawableAnnotation(
            modified.getX(), modified.getY(), 
            modified.getDisplayWidth(), modified.getDisplayHeight(), 
            modified.getDrawScaleFactor(), new TestDrawable()
        );
        assertEquals(modified, matchingModified, "Annotations with identical parameters should be equal after modification");
    }

    /**
     * Verifies that equal objects return the same hash code, as required by the Object contract.
     */
    @Test
    public void testHashCode() {
        XYDrawableAnnotation annotation1 = createStandardAnnotation();
        XYDrawableAnnotation annotation2 = createStandardAnnotation();
        
        assertEquals(annotation1, annotation2, "Annotations should be equal");
        
        int hashCode1 = annotation1.hashCode();
        int hashCode2 = annotation2.hashCode();
        
        assertEquals(hashCode1, hashCode2, "Equal objects must have equal hash codes");
    }

    /**
     * Verifies that cloning creates a separate but equal instance.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        XYDrawableAnnotation original = createStandardAnnotation();
        XYDrawableAnnotation cloned = (XYDrawableAnnotation) original.clone();
        
        assertNotSame(original, cloned, "Cloned object should be a different instance");
        assertSame(original.getClass(), cloned.getClass(), "Cloned object should have the same class");
        assertEquals(original, cloned, "Cloned object should be equal to the original");
    }

    /**
     * Verifies that XYDrawableAnnotation implements the PublicCloneable interface.
     */
    @Test
    public void testPublicCloneable() {
        XYDrawableAnnotation annotation = createStandardAnnotation();
        assertTrue(annotation instanceof PublicCloneable, 
                  "XYDrawableAnnotation should implement PublicCloneable interface");
    }

    /**
     * Tests serialization and deserialization to ensure object state is preserved.
     */
    @Test
    public void testSerialization() {
        XYDrawableAnnotation original = createStandardAnnotation();
        XYDrawableAnnotation deserialized = TestUtils.serialised(original);
        
        assertEquals(original, deserialized, 
                    "Deserialized annotation should be equal to the original");
    }
}