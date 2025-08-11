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

    // Test data constants for better readability and maintainability
    private static final double X_LOW = 1.0;
    private static final double X_HIGH = 2.0;
    private static final double Y_VALUE = 3.0;
    private static final double Y_LOW = 2.5;
    private static final double Y_HIGH = 3.5;

    /**
     * Verify that the equals method correctly compares all fields of XYInterval objects.
     * Tests that two intervals are equal when all parameters match, and unequal when any parameter differs.
     */
    @Test
    public void testEquals() {
        // Test: Two identical intervals should be equal
        XYInterval baseInterval = createStandardInterval();
        XYInterval identicalInterval = createStandardInterval();
        assertEquals(baseInterval, identicalInterval, "Identical intervals should be equal");

        // Test each field individually to ensure equals method checks all parameters
        testFieldEquality_XLow(baseInterval);
        testFieldEquality_XHigh(baseInterval);
        testFieldEquality_YValue(baseInterval);
        testFieldEquality_YLow(baseInterval);
        testFieldEquality_YHigh(baseInterval);
    }

    private void testFieldEquality_XLow(XYInterval baseInterval) {
        // Test: Different xLow values should make intervals unequal
        XYInterval differentXLow = new XYInterval(1.1, X_HIGH, Y_VALUE, Y_LOW, Y_HIGH);
        assertNotEquals(baseInterval, differentXLow, "Intervals with different xLow should not be equal");
        
        // Test: Same xLow values should make intervals equal again
        XYInterval sameXLow = new XYInterval(1.1, X_HIGH, Y_VALUE, Y_LOW, Y_HIGH);
        assertEquals(differentXLow, sameXLow, "Intervals with same xLow should be equal");
    }

    private void testFieldEquality_XHigh(XYInterval baseInterval) {
        // Test: Different xHigh values should make intervals unequal
        XYInterval differentXHigh = new XYInterval(1.1, 2.2, Y_VALUE, Y_LOW, Y_HIGH);
        XYInterval comparisonInterval = new XYInterval(1.1, X_HIGH, Y_VALUE, Y_LOW, Y_HIGH);
        assertNotEquals(differentXHigh, comparisonInterval, "Intervals with different xHigh should not be equal");
        
        // Test: Same xHigh values should make intervals equal
        XYInterval sameXHigh = new XYInterval(1.1, 2.2, Y_VALUE, Y_LOW, Y_HIGH);
        assertEquals(differentXHigh, sameXHigh, "Intervals with same xHigh should be equal");
    }

    private void testFieldEquality_YValue(XYInterval baseInterval) {
        // Test: Different y values should make intervals unequal
        XYInterval differentY = new XYInterval(1.1, 2.2, 3.3, Y_LOW, Y_HIGH);
        XYInterval comparisonInterval = new XYInterval(1.1, 2.2, Y_VALUE, Y_LOW, Y_HIGH);
        assertNotEquals(differentY, comparisonInterval, "Intervals with different y values should not be equal");
        
        // Test: Same y values should make intervals equal
        XYInterval sameY = new XYInterval(1.1, 2.2, 3.3, Y_LOW, Y_HIGH);
        assertEquals(differentY, sameY, "Intervals with same y values should be equal");
    }

    private void testFieldEquality_YLow(XYInterval baseInterval) {
        // Test: Different yLow values should make intervals unequal
        XYInterval differentYLow = new XYInterval(1.1, 2.2, 3.3, 2.6, Y_HIGH);
        XYInterval comparisonInterval = new XYInterval(1.1, 2.2, 3.3, Y_LOW, Y_HIGH);
        assertNotEquals(differentYLow, comparisonInterval, "Intervals with different yLow should not be equal");
        
        // Test: Same yLow values should make intervals equal
        XYInterval sameYLow = new XYInterval(1.1, 2.2, 3.3, 2.6, Y_HIGH);
        assertEquals(differentYLow, sameYLow, "Intervals with same yLow should be equal");
    }

    private void testFieldEquality_YHigh(XYInterval baseInterval) {
        // Test: Different yHigh values should make intervals unequal
        XYInterval differentYHigh = new XYInterval(1.1, 2.2, 3.3, 2.6, 3.6);
        XYInterval comparisonInterval = new XYInterval(1.1, 2.2, 3.3, 2.6, Y_HIGH);
        assertNotEquals(differentYHigh, comparisonInterval, "Intervals with different yHigh should not be equal");
        
        // Test: Same yHigh values should make intervals equal
        XYInterval sameYHigh = new XYInterval(1.1, 2.2, 3.3, 2.6, 3.6);
        assertEquals(differentYHigh, sameYHigh, "Intervals with same yHigh should be equal");
    }

    /**
     * Verify that XYInterval is immutable by confirming it doesn't implement Cloneable.
     * Immutable objects don't need cloning since their state cannot be modified.
     */
    @Test
    public void testImmutability_DoesNotImplementCloneable() {
        XYInterval interval = createStandardInterval();
        assertFalse(interval instanceof Cloneable, 
            "XYInterval should not implement Cloneable since it's immutable");
    }

    /**
     * Verify that XYInterval objects can be serialized and deserialized correctly.
     * The deserialized object should be equal to the original.
     */
    @Test
    public void testSerialization_PreservesEquality() {
        XYInterval originalInterval = createStandardInterval();
        XYInterval deserializedInterval = TestUtils.serialised(originalInterval);
        
        assertEquals(originalInterval, deserializedInterval, 
            "Deserialized interval should be equal to the original");
    }

    /**
     * Helper method to create a standard XYInterval for testing.
     * Centralizes test data creation for consistency and maintainability.
     * 
     * @return A new XYInterval with standard test values
     */
    private XYInterval createStandardInterval() {
        return new XYInterval(X_LOW, X_HIGH, Y_VALUE, Y_LOW, Y_HIGH);
    }
}