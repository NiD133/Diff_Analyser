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

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the immutable {@link MeterInterval} class.
 */
@DisplayName("A MeterInterval")
class MeterIntervalTest {

    @Test
    @DisplayName("should not be cloneable, as it is immutable")
    void isNotCloneable() {
        MeterInterval interval = new MeterInterval("Test", new Range(1.0, 2.0));
        assertFalse(interval instanceof Cloneable);
    }

    @Nested
    @DisplayName("when serializing")
    class SerializationTests {

        @Test
        @DisplayName("should serialize and deserialize correctly with default properties")
        void serializationWithDefaults() {
            MeterInterval original = new MeterInterval("Normal", new Range(25.0, 75.0));
            MeterInterval deserialized = TestUtils.serialised(original);
            assertEquals(original, deserialized);
        }

        @Test
        @DisplayName("should serialize and deserialize correctly with custom transient properties")
        void serializationWithCustomProperties() {
            MeterInterval original = new MeterInterval("Warning", new Range(75.0, 90.0),
                    Color.ORANGE, new BasicStroke(2.0f), Color.YELLOW);
            MeterInterval deserialized = TestUtils.serialised(original);
            assertEquals(original, deserialized);
        }
    }

    @Nested
    @DisplayName("when checking for equality")
    class EqualsContract {
        private MeterInterval interval;

        @BeforeEach
        void setUp() {
            interval = new MeterInterval(
                "Label", new Range(1.0, 2.0), Color.RED, new BasicStroke(1.0f), Color.BLUE
            );
        }

        @Test
        @DisplayName("is reflexive")
        void isReflexive() {
            assertEquals(interval, interval);
        }

        @Test
        @DisplayName("is symmetric with an equal object")
        void isSymmetric() {
            MeterInterval equalInterval = new MeterInterval(
                "Label", new Range(1.0, 2.0), Color.RED, new BasicStroke(1.0f), Color.BLUE
            );
            assertEquals(interval, equalInterval);
            assertEquals(equalInterval, interval);
        }

        @Test
        @DisplayName("has a consistent hash code with an equal object")
        void hasConsistentHashCode() {
            MeterInterval equalInterval = new MeterInterval(
                "Label", new Range(1.0, 2.0), Color.RED, new BasicStroke(1.0f), Color.BLUE
            );
            assertEquals(interval.hashCode(), equalInterval.hashCode());
        }

        @Test
        @DisplayName("returns false when compared to null")
        void returnsFalseForNull() {
            assertNotEquals(null, interval);
        }

        @Test
        @DisplayName("returns false when compared to a different type")
        void returnsFalseForDifferentType() {
            assertNotEquals("A String", interval);
        }

        @Test
        @DisplayName("distinguishes between different labels")
        void distinguishesDifferentLabels() {
            MeterInterval differentInterval = new MeterInterval(
                "Different Label", new Range(1.0, 2.0), Color.RED, new BasicStroke(1.0f), Color.BLUE
            );
            assertNotEquals(interval, differentInterval);
        }

        @Test
        @DisplayName("distinguishes between different ranges")
        void distinguishesDifferentRanges() {
            MeterInterval differentInterval = new MeterInterval(
                "Label", new Range(3.0, 4.0), Color.RED, new BasicStroke(1.0f), Color.BLUE
            );
            assertNotEquals(interval, differentInterval);
        }

        @Test
        @DisplayName("distinguishes between different outline paints")
        void distinguishesDifferentOutlinePaints() {
            MeterInterval differentInterval = new MeterInterval(
                "Label", new Range(1.0, 2.0), Color.GREEN, new BasicStroke(1.0f), Color.BLUE
            );
            assertNotEquals(interval, differentInterval);
        }

        @Test
        @DisplayName("distinguishes between different outline strokes")
        void distinguishesDifferentOutlineStrokes() {
            MeterInterval differentInterval = new MeterInterval(
                "Label", new Range(1.0, 2.0), Color.RED, new BasicStroke(2.0f), Color.BLUE
            );
            assertNotEquals(interval, differentInterval);
        }

        @Test
        @DisplayName("distinguishes between different background paints")
        void distinguishesDifferentBackgroundPaints() {
            MeterInterval differentInterval = new MeterInterval(
                "Label", new Range(1.0, 2.0), Color.RED, new BasicStroke(1.0f), Color.CYAN
            );
            assertNotEquals(interval, differentInterval);
        }
    }
}