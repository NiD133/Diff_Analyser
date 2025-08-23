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

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.jfree.chart.Drawable;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link XYDrawableAnnotation} class.
 *
 * These tests favor readability by:
 * - Using clear Arrange / Act / Assert sections
 * - Isolating each field-specific equality check in its own test
 * - Providing small factory methods to reduce duplication
 * - Using assertion messages to explain failures
 */
public class XYDrawableAnnotationTest {

    // Baseline values used to construct instances for most tests
    private static final double BASE_X = 10.0;
    private static final double BASE_Y = 20.0;
    private static final double BASE_WIDTH = 100.0;
    private static final double BASE_HEIGHT = 200.0;
    private static final double BASE_SCALE = 1.0;

    /**
     * Minimal stub Drawable that is cloneable and serializable.
     * Its equals() returns true for any other TestDrawable instance.
     */
    static class TestDrawable implements Drawable, Cloneable, Serializable {
        @Override
        public void draw(Graphics2D g2, Rectangle2D area) {
            // Intentionally empty: drawing is not under test here
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestDrawable;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    // Factory methods to reduce noise in tests

    private static XYDrawableAnnotation annotation(double x, double y, double w, double h) {
        return new XYDrawableAnnotation(x, y, w, h, new TestDrawable());
    }

    private static XYDrawableAnnotation annotation(
            double x, double y, double w, double h, double scale) {
        return new XYDrawableAnnotation(x, y, w, h, scale, new TestDrawable());
    }

    @Test
    @DisplayName("equals: same values -> objects are equal")
    public void equals_sameValues_returnsTrue() {
        // Arrange
        XYDrawableAnnotation a1 = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT);
        XYDrawableAnnotation a2 = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT);

        // Assert
        assertEquals(a1, a2, "Two annotations with identical state should be equal");
    }

    @Test
    @DisplayName("equals: different x -> not equal")
    public void equals_differentX_returnsFalse() {
        XYDrawableAnnotation baseline = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT);
        XYDrawableAnnotation changed = annotation(BASE_X + 1, BASE_Y, BASE_WIDTH, BASE_HEIGHT);

        assertNotEquals(baseline, changed, "Changing x should break equality");
    }

    @Test
    @DisplayName("equals: different y -> not equal")
    public void equals_differentY_returnsFalse() {
        XYDrawableAnnotation baseline = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT);
        XYDrawableAnnotation changed = annotation(BASE_X, BASE_Y + 2, BASE_WIDTH, BASE_HEIGHT);

        assertNotEquals(baseline, changed, "Changing y should break equality");
    }

    @Test
    @DisplayName("equals: different display width -> not equal")
    public void equals_differentWidth_returnsFalse() {
        XYDrawableAnnotation baseline = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT);
        XYDrawableAnnotation changed = annotation(BASE_X, BASE_Y, BASE_WIDTH + 1, BASE_HEIGHT);

        assertNotEquals(baseline, changed, "Changing display width should break equality");
    }

    @Test
    @DisplayName("equals: different display height -> not equal")
    public void equals_differentHeight_returnsFalse() {
        XYDrawableAnnotation baseline = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT);
        XYDrawableAnnotation changed = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT + 2);

        assertNotEquals(baseline, changed, "Changing display height should break equality");
    }

    @Test
    @DisplayName("equals: different draw scale factor -> not equal")
    public void equals_differentScale_returnsFalse() {
        XYDrawableAnnotation baseline = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT, BASE_SCALE);
        XYDrawableAnnotation changed = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT, BASE_SCALE + 1);

        assertNotEquals(baseline, changed, "Changing draw scale factor should break equality");
    }

    @Test
    @DisplayName("hashCode: equal objects have equal hash codes")
    public void hashCode_consistentWithEquals() {
        // Arrange
        XYDrawableAnnotation a1 = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT);
        XYDrawableAnnotation a2 = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT);

        // Precondition
        assertEquals(a1, a2, "Precondition: objects must be equal to compare hash codes");

        // Act
        int h1 = a1.hashCode();
        int h2 = a2.hashCode();

        // Assert
        assertEquals(h1, h2, "Equal objects must have the same hash code");
    }

    @Test
    @DisplayName("clone: produces an equal but distinct instance")
    public void cloning_producesEqualButDistinctInstance() throws CloneNotSupportedException {
        // Arrange
        XYDrawableAnnotation original = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT);

        // Act
        XYDrawableAnnotation clone = (XYDrawableAnnotation) original.clone();

        // Assert
        assertNotSame(original, clone, "Clone should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should be of the same runtime type");
        assertEquals(original, clone, "Clone should be equal to the original");
    }

    @Test
    @DisplayName("implements PublicCloneable marker interface")
    public void implementsPublicCloneable() {
        XYDrawableAnnotation a = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT);
        assertTrue(a instanceof PublicCloneable, "XYDrawableAnnotation should implement PublicCloneable");
    }

    @Test
    @DisplayName("serialization: round-trip preserves equality")
    public void serialization_roundTrip_preservesEquality() {
        XYDrawableAnnotation original = annotation(BASE_X, BASE_Y, BASE_WIDTH, BASE_HEIGHT);
        XYDrawableAnnotation restored = TestUtils.serialised(original);

        assertEquals(original, restored, "Deserialized instance should be equal to the original");
    }
}