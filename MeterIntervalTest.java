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

    // Test data constants for better maintainability
    private static final String LABEL_1 = "Label 1";
    private static final String LABEL_2 = "Label 2";
    private static final String SIMPLE_LABEL = "X";
    private static final Range STANDARD_RANGE = new Range(1.2, 3.4);
    private static final Range SIMPLE_RANGE = new Range(1.0, 2.0);
    private static final Color OUTLINE_COLOR = Color.RED;
    private static final Color BACKGROUND_COLOR = Color.BLUE;
    private static final BasicStroke STANDARD_STROKE = new BasicStroke(1.0f);

    /**
     * Tests that the equals method correctly identifies equal and unequal MeterInterval instances.
     * Verifies that equals is symmetric and that changing the label makes intervals unequal.
     */
    @Test
    public void testEquals() {
        // Given: Two identical MeterInterval instances
        MeterInterval firstInterval = createFullyConfiguredInterval(LABEL_1);
        MeterInterval secondInterval = createFullyConfiguredInterval(LABEL_1);

        // When & Then: Identical intervals should be equal (and symmetric)
        assertEquals(firstInterval, secondInterval, 
            "Two MeterIntervals with identical properties should be equal");
        assertEquals(secondInterval, firstInterval, 
            "Equals should be symmetric");

        // Given: Intervals with different labels
        MeterInterval intervalWithDifferentLabel = createFullyConfiguredInterval(LABEL_2);

        // When & Then: Intervals with different labels should not be equal
        assertNotEquals(firstInterval, intervalWithDifferentLabel,
            "MeterIntervals with different labels should not be equal");

        // Given: Second interval updated to match the different label
        MeterInterval anotherIntervalWithLabel2 = createFullyConfiguredInterval(LABEL_2);

        // When & Then: Intervals with same different label should be equal
        assertEquals(intervalWithDifferentLabel, anotherIntervalWithLabel2,
            "MeterIntervals with same label should be equal");
    }

    /**
     * Tests that MeterInterval does not implement Cloneable interface.
     * This is expected behavior since MeterInterval is designed to be immutable.
     */
    @Test
    public void testCloning() {
        // Given: A MeterInterval instance
        MeterInterval interval = createSimpleInterval();

        // When & Then: MeterInterval should not implement Cloneable (immutable design)
        assertFalse(interval instanceof Cloneable,
            "MeterInterval should not implement Cloneable since it's immutable");
    }

    /**
     * Tests that MeterInterval instances can be serialized and deserialized correctly.
     * The deserialized instance should be equal to the original.
     */
    @Test
    public void testSerialization() {
        // Given: A MeterInterval instance
        MeterInterval originalInterval = createSimpleInterval();

        // When: Serializing and deserializing the interval
        MeterInterval deserializedInterval = TestUtils.serialised(originalInterval);

        // Then: The deserialized interval should equal the original
        assertEquals(originalInterval, deserializedInterval,
            "Deserialized MeterInterval should equal the original");
    }

    // Helper methods for creating test instances

    /**
     * Creates a fully configured MeterInterval with all properties set.
     * 
     * @param label the label for the interval
     * @return a MeterInterval with all properties configured
     */
    private MeterInterval createFullyConfiguredInterval(String label) {
        return new MeterInterval(
            label, 
            STANDARD_RANGE, 
            OUTLINE_COLOR, 
            STANDARD_STROKE,
            BACKGROUND_COLOR
        );
    }

    /**
     * Creates a simple MeterInterval with minimal configuration (using constructor defaults).
     * 
     * @return a MeterInterval with default styling
     */
    private MeterInterval createSimpleInterval() {
        return new MeterInterval(SIMPLE_LABEL, SIMPLE_RANGE);
    }
}