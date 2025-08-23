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
 * -----------------------
 * DialBackgroundTest.java
 * -----------------------
 * (C) Copyright 2006-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.plot.dial;

import java.awt.Color;
import java.awt.GradientPaint;

import org.jfree.chart.TestUtils;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.jfree.chart.internal.CloneUtils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.awt.Color.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DialBackground} class.
 *
 * This suite aims to be easy to read and maintain by:
 * - Using descriptive test names (one assertion purpose per test).
 * - Introducing small helpers for repeated objects (paints, transformers).
 * - Following a clear Arrange-Act-Assert structure in each test.
 */
public class DialBackgroundTest {

    // Common test fixtures to avoid "magic values" in tests
    private static final float X1 = 1.0f;
    private static final float Y1 = 2.0f;
    private static final float X2 = 3.0f;
    private static final float Y2 = 4.0f;

    private static GradientPaint redToYellowPaint() {
        return new GradientPaint(X1, Y1, RED, X2, Y2, YELLOW);
    }

    private static GradientPaint redToGreenPaint() {
        return new GradientPaint(X1, Y1, RED, X2, Y2, GREEN);
    }

    private static StandardGradientPaintTransformer verticalCenterTransformer() {
        return new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL);
    }

    @Test
    @DisplayName("equals: identical default instances are equal")
    public void equals_whenDefaultInstances_thenEqual() {
        // Arrange
        DialBackground a = new DialBackground();
        DialBackground b = new DialBackground();

        // Assert
        assertEquals(a, b);
    }

    @Test
    @DisplayName("equals: differing paint makes instances not equal")
    public void equals_whenPaintDiffers_thenNotEqual() {
        // Arrange
        DialBackground a = new DialBackground();
        DialBackground b = new DialBackground();

        // Act
        a.setPaint(redToYellowPaint());

        // Assert
        assertNotEquals(a, b);

        // Act: align b's paint with a
        b.setPaint(redToYellowPaint());

        // Assert
        assertEquals(a, b);
    }

    @Test
    @DisplayName("equals: differing gradient paint transformer makes instances not equal")
    public void equals_whenTransformerDiffers_thenNotEqual() {
        // Arrange
        DialBackground a = new DialBackground();
        DialBackground b = new DialBackground();

        // Act
        a.setGradientPaintTransformer(verticalCenterTransformer());

        // Assert
        assertNotEquals(a, b);

        // Act: align b's transformer with a
        b.setGradientPaintTransformer(verticalCenterTransformer());

        // Assert
        assertEquals(a, b);
    }

    @Test
    @DisplayName("equals: inherited visibility flag participates in equality")
    public void equals_whenVisibilityDiffers_thenNotEqual() {
        // Arrange
        DialBackground a = new DialBackground();
        DialBackground b = new DialBackground();

        // Act
        a.setVisible(false);

        // Assert
        assertNotEquals(a, b);

        // Act: align b's visibility with a
        b.setVisible(false);

        // Assert
        assertEquals(a, b);
    }

    @Test
    @DisplayName("hashCode: equal objects produce the same hash code")
    public void hashCode_whenObjectsEqual_thenHashCodesEqual() {
        // Arrange
        DialBackground a = new DialBackground(RED);
        DialBackground b = new DialBackground(RED);
        assertEquals(a, b);

        // Act
        int h1 = a.hashCode();
        int h2 = b.hashCode();

        // Assert
        assertEquals(h1, h2);
    }

    @Test
    @DisplayName("clone: default instance clones equal, not same")
    public void clone_whenDefaultInstance_thenEqualAndNotSame() throws CloneNotSupportedException {
        // Arrange
        DialBackground original = new DialBackground();

        // Act
        DialBackground clone = CloneUtils.clone(original);

        // Assert
        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);
    }

    @Test
    @DisplayName("clone: customized instance clones equal and listener lists are independent")
    public void clone_whenCustomizedInstance_thenEqualAndListenersIndependent() throws CloneNotSupportedException {
        // Arrange
        DialBackground original = new DialBackground();
        original.setPaint(redToGreenPaint());
        original.setGradientPaintTransformer(verticalCenterTransformer());

        // Act
        DialBackground clone = (DialBackground) original.clone();

        // Assert: object equality and type
        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);

        // Assert: listener lists are independent (shallow listener state is not shared)
        MyDialLayerChangeListener listener = new MyDialLayerChangeListener();
        original.addChangeListener(listener);
        assertTrue(original.hasListener(listener));
        assertFalse(clone.hasListener(listener));
    }

    @Test
    @DisplayName("serialization: default instance round-trips with equality")
    public void serialization_whenDefaultInstance_thenRoundTripEqual() {
        // Arrange
        DialBackground original = new DialBackground();

        // Act
        DialBackground restored = TestUtils.serialised(original);

        // Assert
        assertEquals(original, restored);
    }

    @Test
    @DisplayName("serialization: customized instance round-trips with equality")
    public void serialization_whenCustomizedInstance_thenRoundTripEqual() {
        // Arrange
        DialBackground original = new DialBackground();
        original.setPaint(redToGreenPaint());
        original.setGradientPaintTransformer(verticalCenterTransformer());

        // Act
        DialBackground restored = TestUtils.serialised(original);

        // Assert
        assertEquals(original, restored);
    }
}