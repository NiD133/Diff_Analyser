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

    // Constants for test values
    private static final double X1 = 10.0;
    private static final double X2 = 11.0;
    private static final double Y1 = 20.0;
    private static final double Y2 = 22.0;
    private static final double WIDTH1 = 100.0;
    private static final double WIDTH2 = 101.0;
    private static final double HEIGHT1 = 200.0;
    private static final double HEIGHT2 = 202.0;
    private static final double SCALE1 = 1.0;
    private static final double SCALE2 = 2.0;
    
    /**
     * Creates a default drawable annotation for testing.
     */
    private XYDrawableAnnotation createDefaultAnnotation() {
        return new XYDrawableAnnotation(X1, Y1, WIDTH1, HEIGHT1, new TestDrawable());
    }

    /**
     * Creates a drawable annotation with a specific scale factor.
     */
    private XYDrawableAnnotation createAnnotationWithScale(double scale) {
        return new XYDrawableAnnotation(X1, Y1, WIDTH1, HEIGHT1, scale, new TestDrawable());
    }

    static class TestDrawable implements Drawable, Cloneable, Serializable {
        @Override
        public void draw(Graphics2D g2, Rectangle2D area) {
            // Empty implementation for testing
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof TestDrawable);
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    static class AnotherTestDrawable implements Drawable, Serializable {
        @Override
        public void draw(Graphics2D g2, Rectangle2D area) {
            // Empty implementation for testing
        }
    }

    @Test
    public void identicalAnnotations_ShouldBeEqual() {
        XYDrawableAnnotation a1 = createDefaultAnnotation();
        XYDrawableAnnotation a2 = createDefaultAnnotation();
        assertEquals(a1, a2, "Identical annotations should be equal");
    }

    @Test
    public void differentXValues_ShouldNotBeEqual() {
        XYDrawableAnnotation base = createDefaultAnnotation();
        XYDrawableAnnotation different = new XYDrawableAnnotation(X2, Y1, WIDTH1, HEIGHT1, new TestDrawable());
        assertNotEquals(base, different, "Different x values should not be equal");
    }

    @Test
    public void differentYValues_ShouldNotBeEqual() {
        XYDrawableAnnotation base = createDefaultAnnotation();
        XYDrawableAnnotation different = new XYDrawableAnnotation(X1, Y2, WIDTH1, HEIGHT1, new TestDrawable());
        assertNotEquals(base, different, "Different y values should not be equal");
    }

    @Test
    public void differentWidths_ShouldNotBeEqual() {
        XYDrawableAnnotation base = createDefaultAnnotation();
        XYDrawableAnnotation different = new XYDrawableAnnotation(X1, Y1, WIDTH2, HEIGHT1, new TestDrawable());
        assertNotEquals(base, different, "Different widths should not be equal");
    }

    @Test
    public void differentHeights_ShouldNotBeEqual() {
        XYDrawableAnnotation base = createDefaultAnnotation();
        XYDrawableAnnotation different = new XYDrawableAnnotation(X1, Y1, WIDTH1, HEIGHT2, new TestDrawable());
        assertNotEquals(base, different, "Different heights should not be equal");
    }

    @Test
    public void differentScaleFactors_ShouldNotBeEqual() {
        XYDrawableAnnotation base = createAnnotationWithScale(SCALE1);
        XYDrawableAnnotation different = createAnnotationWithScale(SCALE2);
        assertNotEquals(base, different, "Different scale factors should not be equal");
    }

    @Test
    public void differentDrawables_ShouldNotBeEqual() {
        XYDrawableAnnotation base = new XYDrawableAnnotation(X1, Y1, WIDTH1, HEIGHT1, new TestDrawable());
        XYDrawableAnnotation different = new XYDrawableAnnotation(X1, Y1, WIDTH1, HEIGHT1, new AnotherTestDrawable());
        assertNotEquals(base, different, "Different drawables should not be equal");
    }

    @Test
    public void equalAnnotations_ShouldHaveSameHashCode() {
        XYDrawableAnnotation a1 = createDefaultAnnotation();
        XYDrawableAnnotation a2 = createDefaultAnnotation();
        assertEquals(a1.hashCode(), a2.hashCode(), "Equal annotations must have same hash code");
    }

    @Test
    public void cloning_ShouldCreateEqualButDistinctInstance() throws CloneNotSupportedException {
        XYDrawableAnnotation original = createDefaultAnnotation();
        XYDrawableAnnotation clone = (XYDrawableAnnotation) original.clone();
        
        assertNotSame(original, clone, "Clone should be distinct object");
        assertEquals(original, clone, "Clone should be equal to original");
        assertEquals(original.getClass(), clone.getClass(), "Clone should have same class");
    }

    @Test
    public void shouldImplementPublicCloneable() {
        XYDrawableAnnotation annotation = createDefaultAnnotation();
        assertTrue(annotation instanceof PublicCloneable, "Should implement PublicCloneable");
    }

    @Test
    public void serialization_ShouldPreserveEquality() {
        XYDrawableAnnotation original = createDefaultAnnotation();
        XYDrawableAnnotation deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized instance should equal original");
    }
}