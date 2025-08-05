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
 */
package org.jfree.chart.plot.dial;

import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.GradientPaintTransformer;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.junit.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.*;

/**
 * A collection of tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    // --- CONSTRUCTOR TESTS ---

    /**
     * Verifies that the default constructor initializes the paint to Color.WHITE.
     */
    @Test
    public void defaultConstructor_createsInstanceWithWhitePaint() {
        // Arrange & Act
        DialBackground background = new DialBackground();

        // Assert
        assertEquals(Color.WHITE, background.getPaint());
    }

    /**
     * Verifies that the constructor correctly sets the specified paint.
     */
    @Test
    public void constructor_withSpecificPaint_setsPaintCorrectly() {
        // Arrange & Act
        DialBackground background = new DialBackground(Color.GREEN);

        // Assert
        assertEquals(Color.GREEN, background.getPaint());
    }

    /**
     * Verifies that the constructor throws an exception when passed a null paint.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_withNullPaint_throwsIllegalArgumentException() {
        // Act
        new DialBackground(null); // Should throw
    }

    // --- PROPERTY SETTER AND GETTER TESTS ---

    /**
     * Verifies that setPaint correctly updates the paint property.
     */
    @Test
    public void setPaint_withValidPaint_updatesPaintProperty() {
        // Arrange
        DialBackground background = new DialBackground();

        // Act
        background.setPaint(Color.BLUE);

        // Assert
        assertEquals(Color.BLUE, background.getPaint());
    }

    /**
     * Verifies that setPaint throws an exception for a null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setPaint_withNullPaint_throwsIllegalArgumentException() {
        // Arrange
        DialBackground background = new DialBackground();

        // Act
        background.setPaint(null); // Should throw
    }

    /**
     * Verifies that the default gradient paint transformer is a vertical one.
     */
    @Test
    public void getGradientPaintTransformer_returnsVerticalTransformer_byDefault() {
        // Arrange
        DialBackground background = new DialBackground();

        // Act
        GradientPaintTransformer transformer = background.getGradientPaintTransformer();

        // Assert
        assertTrue(transformer instanceof StandardGradientPaintTransformer);
        assertEquals(GradientPaintTransformType.VERTICAL,
                ((StandardGradientPaintTransformer) transformer).getType());
    }

    /**
     * Verifies that setGradientPaintTransformer throws an exception for a null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setGradientPaintTransformer_withNullTransformer_throwsIllegalArgumentException() {
        // Arrange
        DialBackground background = new DialBackground();

        // Act
        background.setGradientPaintTransformer(null); // Should throw
    }

    // --- DRAWING AND BEHAVIOR TESTS ---

    /**
     * A smoke test for the draw() method to ensure it executes without errors given valid inputs.
     */
    @Test
    public void draw_withValidArguments_completesWithoutError() {
        // Arrange
        DialBackground background = new DialBackground();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        DialPlot plot = new DialPlot();
        Rectangle2D frame = new Rectangle2D.Double(0, 0, 100, 100);

        // Act & Assert (no exception thrown is the success condition)
        try {
            background.draw(g2, plot, frame, frame);
        } catch (Exception e) {
            fail("Drawing with valid arguments should not throw an exception: " + e.getMessage());
        }
    }

    /**
     * Verifies that the isClippedToWindow() method always returns true.
     */
    @Test
    public void isClippedToWindow_alwaysReturnsTrue() {
        // Arrange
        DialBackground background = new DialBackground();

        // Act & Assert
        assertTrue(background.isClippedToWindow());
    }

    // --- EQUALS, HASHCODE, AND CLONE TESTS ---

    /**
     * Verifies that an object is equal to itself.
     */
    @Test
    public void equals_returnsTrue_forSameInstance() {
        // Arrange
        DialBackground background = new DialBackground();

        // Assert
        assertTrue(background.equals(background));
    }

    /**
     * Verifies that two default instances are considered equal and have the same hashCode.
     */
    @Test
    public void equals_and_hashCode_areConsistent_forEqualInstances() {
        // Arrange
        DialBackground bg1 = new DialBackground();
        DialBackground bg2 = new DialBackground();

        // Assert
        assertTrue(bg1.equals(bg2));
        assertEquals(bg1.hashCode(), bg2.hashCode());
    }

    /**
     * Verifies that equals() returns false when the paint property is different.
     */
    @Test
    public void equals_returnsFalse_whenPaintIsDifferent() {
        // Arrange
        DialBackground bg1 = new DialBackground(Color.WHITE);
        DialBackground bg2 = new DialBackground(Color.BLUE);

        // Assert
        assertFalse(bg1.equals(bg2));
    }

    /**
     * Verifies that equals() returns false when the gradient paint transformer is different.
     */
    @Test
    public void equals_returnsFalse_whenGradientPaintTransformerIsDifferent() {
        // Arrange
        DialBackground bg1 = new DialBackground();
        DialBackground bg2 = new DialBackground();
        bg2.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_HORIZONTAL));

        // Assert
        assertFalse(bg1.equals(bg2));
    }

    /**
     * Verifies that equals() returns false when comparing with an object of a different type.
     */
    @Test
    public void equals_returnsFalse_forDifferentClass() {
        // Arrange
        DialBackground background = new DialBackground();
        Object otherObject = new Object();

        // Assert
        assertFalse(background.equals(otherObject));
    }

    /**
     * Verifies that cloning produces an independent but equal instance.
     */
    @Test
    public void clone_createsIndependentAndEqualCopy() throws CloneNotSupportedException {
        // Arrange
        DialBackground original = new DialBackground(Color.RED);

        // Act
        DialBackground clone = (DialBackground) original.clone();

        // Assert
        assertNotSame("Clone should be a different instance", original, clone);
        assertEquals("Clone should be equal to the original", original, clone);
    }

    // --- SERIALIZATION TESTS ---

    /**
     * Verifies that the object state is preserved after serialization and deserialization.
     */
    @Test
    public void serialization_preservesObjectState() throws Exception {
        // Arrange
        DialBackground original = new DialBackground(Color.ORANGE);
        original.setGradientPaintTransformer(
                new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL));

        // Act
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        out.writeObject(original);

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        DialBackground restored = (DialBackground) in.readObject();

        // Assert
        assertEquals("Restored object should be equal to the original", original, restored);
    }
}