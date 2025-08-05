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
 * XYIntervalTest.java
 * -------------------
 * (C) Copyright 2006-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.xy;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    // Test constants for consistent test data
    private static final double X_LOW = 1.0;
    private static final double X_HIGH = 2.0;
    private static final double Y = 3.0;
    private static final double Y_LOW = 4.0;
    private static final double Y_HIGH = 5.0;

    /**
     * Tests that the constructor sets values correctly and getters retrieve them.
     */
    @Test
    public void constructorAndGetters() {
        XYInterval interval = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        
        assertEquals(X_LOW, interval.getXLow(), "XLow should match constructor argument");
        assertEquals(X_HIGH, interval.getXHigh(), "XHigh should match constructor argument");
        assertEquals(Y, interval.getY(), "Y should match constructor argument");
        assertEquals(Y_LOW, interval.getYLow(), "YLow should match constructor argument");
        assertEquals(Y_HIGH, interval.getYHigh(), "YHigh should match constructor argument");
    }

    /**
     * Tests that the equals method returns true when comparing the same object.
     */
    @Test
    public void equals_SameObject() {
        XYInterval interval = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        assertEquals(interval, interval, "Object should equal itself");
    }

    /**
     * Tests that the equals method returns true for two equal objects.
     */
    @Test
    public void equals_EqualObject() {
        XYInterval i1 = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        XYInterval i2 = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        assertEquals(i1, i2, "Identical objects should be equal");
    }

    /**
     * Tests that the equals method returns false for different xLow values.
     */
    @Test
    public void equals_DifferentXLow() {
        XYInterval base = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        XYInterval other = new XYInterval(99.0, X_HIGH, Y, Y_LOW, Y_HIGH);
        assertNotEquals(base, other, "Objects with different xLow should not be equal");
    }

    /**
     * Tests that the equals method returns false for different xHigh values.
     */
    @Test
    public void equals_DifferentXHigh() {
        XYInterval base = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        XYInterval other = new XYInterval(X_LOW, 99.0, Y, Y_LOW, Y_HIGH);
        assertNotEquals(base, other, "Objects with different xHigh should not be equal");
    }

    /**
     * Tests that the equals method returns false for different y values.
     */
    @Test
    public void equals_DifferentY() {
        XYInterval base = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        XYInterval other = new XYInterval(X_LOW, X_HIGH, 99.0, Y_LOW, Y_HIGH);
        assertNotEquals(base, other, "Objects with different y should not be equal");
    }

    /**
     * Tests that the equals method returns false for different yLow values.
     */
    @Test
    public void equals_DifferentYLow() {
        XYInterval base = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        XYInterval other = new XYInterval(X_LOW, X_HIGH, Y, 99.0, Y_HIGH);
        assertNotEquals(base, other, "Objects with different yLow should not be equal");
    }

    /**
     * Tests that the equals method returns false for different yHigh values.
     */
    @Test
    public void equals_DifferentYHigh() {
        XYInterval base = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        XYInterval other = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, 99.0);
        assertNotEquals(base, other, "Objects with different yHigh should not be equal");
    }

    /**
     * Tests that the equals method returns false when comparing to null.
     */
    @Test
    public void equals_Null() {
        XYInterval interval = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        assertNotEquals(null, interval, "Object should not equal null");
    }

    /**
     * Tests that the equals method returns false when comparing to a different class.
     */
    @Test
    public void equals_DifferentClass() {
        XYInterval interval = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        assertNotEquals(interval, new Object(), "Object should not equal different class");
    }

    /**
     * Tests that XYInterval does not implement Cloneable.
     */
    @Test
    public void cloning() {
        XYInterval interval = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        assertFalse(interval instanceof Cloneable, "XYInterval should not implement Cloneable");
    }

    /**
     * Tests serialization and deserialization for equivalence.
     */
    @Test
    public void serialization() {
        XYInterval original = new XYInterval(X_LOW, X_HIGH, Y, Y_LOW, Y_HIGH);
        XYInterval deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized object should equal original");
    }

}