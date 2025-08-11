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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link XYInterval} class, a simple immutable data object.
 */
public class XYIntervalTest {

    private static final XYInterval BASE_INTERVAL = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);

    /**
     * The XYInterval class is designed to be immutable, so it should not support cloning.
     */
    @Test
    @DisplayName("XYInterval should not be cloneable")
    public void isNotCloneable() {
        assertFalse(BASE_INTERVAL instanceof Cloneable);
    }

    /**
     * Verifies that an instance can be serialized and deserialized without losing data.
     */
    @Test
    @DisplayName("A deserialized instance should be equal to the original")
    public void serialization_shouldPreserveEquality() {
        XYInterval deserialized = TestUtils.serialised(BASE_INTERVAL);
        assertEquals(BASE_INTERVAL, deserialized);
    }

    /**
     * Contains focused tests for the equals() method to ensure it correctly
     * handles all fields and edge cases.
     */
    @Nested
    @DisplayName("equals() contract")
    class EqualsContract {

        @Test
        @DisplayName("should return true for the same instance")
        void equals_withSameInstance_shouldReturnTrue() {
            assertEquals(BASE_INTERVAL, BASE_INTERVAL);
        }

        @Test
        @DisplayName("should return true for an identical instance")
        void equals_withIdenticalInstance_shouldReturnTrue() {
            XYInterval identicalInterval = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);
            assertEquals(BASE_INTERVAL, identicalInterval);
        }

        @Test
        @DisplayName("should return false when compared to null")
        void equals_withNull_shouldReturnFalse() {
            assertNotEquals(null, BASE_INTERVAL);
        }

        @Test
        @DisplayName("should return false when compared to a different class")
        void equals_withDifferentClass_shouldReturnFalse() {
            assertNotEquals("A string", BASE_INTERVAL);
        }

        @Test
        @DisplayName("should return false when xLow differs")
        void equals_whenXLowDiffers_shouldReturnFalse() {
            XYInterval differentInterval = new XYInterval(99.0, 2.0, 3.0, 2.5, 3.5);
            assertNotEquals(BASE_INTERVAL, differentInterval);
        }

        @Test
        @DisplayName("should return false when xHigh differs")
        void equals_whenXHighDiffers_shouldReturnFalse() {
            XYInterval differentInterval = new XYInterval(1.0, 99.0, 3.0, 2.5, 3.5);
            assertNotEquals(BASE_INTERVAL, differentInterval);
        }

        @Test
        @DisplayName("should return false when y differs")
        void equals_whenYDiffers_shouldReturnFalse() {
            XYInterval differentInterval = new XYInterval(1.0, 2.0, 99.0, 2.5, 3.5);
            assertNotEquals(BASE_INTERVAL, differentInterval);
        }

        @Test
        @DisplayName("should return false when yLow differs")
        void equals_whenYLowDiffers_shouldReturnFalse() {
            XYInterval differentInterval = new XYInterval(1.0, 2.0, 3.0, 99.0, 3.5);
            assertNotEquals(BASE_INTERVAL, differentInterval);
        }

        @Test
        @DisplayName("should return false when yHigh differs")
        void equals_whenYHighDiffers_shouldReturnFalse() {
            XYInterval differentInterval = new XYInterval(1.0, 2.0, 3.0, 2.5, 99.0);
            assertNotEquals(BASE_INTERVAL, differentInterval);
        }
    }
}