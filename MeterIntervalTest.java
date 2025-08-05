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
 * ----------------------
 * MeterIntervalTest.java
 * ----------------------
 * (C) Copyright 2005-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.plot;

import java.awt.BasicStroke;
import java.awt.Color;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Tests that equal MeterInterval instances are correctly identified.
     * Covers:
     * - Equality of identical objects
     * - Symmetry of equals() method
     * - Inequality when labels differ
     * - Equality restoration when labels match
     */
    @Test
    public void testEquals() {
        // Create two identical intervals
        MeterInterval interval1 = new MeterInterval(
            "Label 1", 
            new Range(1.2, 3.4), 
            Color.RED, 
            new BasicStroke(1.0f),
            Color.BLUE
        );
        MeterInterval interval2 = new MeterInterval(
            "Label 1", 
            new Range(1.2, 3.4), 
            Color.RED, 
            new BasicStroke(1.0f),
            Color.BLUE
        );
        
        // Verify equality and symmetry
        assertEquals(interval1, interval2, "Identical intervals should be equal");
        assertEquals(interval2, interval1, "Equals method should be symmetric");

        // Test with different label
        MeterInterval intervalWithDifferentLabel = new MeterInterval(
            "Label 2", 
            new Range(1.2, 3.4), 
            Color.RED, 
            new BasicStroke(1.0f),
            Color.BLUE
        );
        assertNotEquals(interval1, intervalWithDifferentLabel, 
            "Intervals with different labels should not be equal");

        // Verify equality is restored when labels match
        MeterInterval intervalWithMatchingLabel = new MeterInterval(
            "Label 2", 
            new Range(1.2, 3.4), 
            Color.RED, 
            new BasicStroke(1.0f),
            Color.BLUE
        );
        assertEquals(intervalWithDifferentLabel, intervalWithMatchingLabel,
            "Intervals with same label should be equal");
    }

    /**
     * Confirms that MeterInterval does not support cloning since it's immutable.
     */
    @Test
    public void testCloning() {
        MeterInterval interval = new MeterInterval("Test Interval", new Range(1.0, 2.0));
        assertFalse(interval instanceof Cloneable, 
            "Immutable objects should not implement Cloneable");
    }

    /**
     * Verifies that serialization produces equal objects.
     */
    @Test
    public void testSerialization() {
        // Create original object
        MeterInterval original = new MeterInterval("Serialized", new Range(1.0, 2.0));
        
        // Serialize and deserialize
        MeterInterval deserialized = TestUtils.serialised(original);
        
        // Verify object integrity after round trip
        assertEquals(original, deserialized, 
            "Deserialized object should equal original");
    }
}