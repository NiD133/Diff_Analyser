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
 * --------------------------
 * QuarterDateFormatTest.java
 * --------------------------
 * (C) Copyright 2005-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link QuarterDateFormat} class.
 */
@DisplayName("QuarterDateFormat")
class QuarterDateFormatTest {

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final TimeZone PST = TimeZone.getTimeZone("PST");
    private static final String[] QUARTERS_AS_NUMBERS = {"1", "2", "3", "4"};
    private static final String[] QUARTERS_AS_LETTERS = {"A", "B", "C", "D"};

    @Nested
    @DisplayName("equals() and hashCode() contracts")
    class EqualsAndHashCode {

        @Test
        @DisplayName("should be equal to itself")
        void testEquals_Self() {
            QuarterDateFormat formatter = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS);
            assertEquals(formatter, formatter);
        }

        @Test
        @DisplayName("should be equal to an identical instance")
        void testEquals_Symmetric() {
            QuarterDateFormat formatter1 = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS);
            QuarterDateFormat formatter2 = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS);
            assertEquals(formatter1, formatter2);
            assertEquals(formatter2, formatter1);
        }

        @Test
        @DisplayName("should not be equal to null")
        void testEquals_Null() {
            QuarterDateFormat formatter = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS);
            assertNotEquals(null, formatter);
        }

        @Test
        @DisplayName("should not be equal to an object of a different type")
        void testEquals_DifferentType() {
            QuarterDateFormat formatter = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS);
            assertNotEquals("Not a QuarterDateFormat", formatter);
        }

        @Test
        @DisplayName("should not be equal if time zones differ")
        void testEquals_DifferentTimeZone() {
            QuarterDateFormat formatter1 = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS);
            QuarterDateFormat formatter2 = new QuarterDateFormat(PST, QUARTERS_AS_NUMBERS);
            assertNotEquals(formatter1, formatter2);
        }

        @Test
        @DisplayName("should not be equal if quarter symbols differ")
        void testEquals_DifferentQuarterSymbols() {
            QuarterDateFormat formatter1 = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS);
            QuarterDateFormat formatter2 = new QuarterDateFormat(GMT, QUARTERS_AS_LETTERS);
            assertNotEquals(formatter1, formatter2);
        }

        @Test
        @DisplayName("should not be equal if 'quarterFirst' flag differs")
        void testEquals_DifferentQuarterFirstFlag() {
            QuarterDateFormat formatter1 = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS, false);
            QuarterDateFormat formatter2 = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS, true);
            assertNotEquals(formatter1, formatter2);
        }

        @Test
        @DisplayName("should have the same hash code for equal objects")
        void testHashCode_Contract() {
            QuarterDateFormat formatter1 = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS);
            QuarterDateFormat formatter2 = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS);
            assertEquals(formatter1, formatter2);
            assertEquals(formatter1.hashCode(), formatter2.hashCode());
        }
    }

    @Test
    @DisplayName("Cloning should produce an equal but not identical object")
    void testCloning() {
        QuarterDateFormat original = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS);
        
        QuarterDateFormat clone = (QuarterDateFormat) original.clone();

        assertNotSame(original, clone, "Clone should be a different object instance.");
        assertEquals(original, clone, "Clone should be equal to the original object.");
    }

    @Test
    @DisplayName("Serialization should preserve object state")
    void testSerialization() {
        QuarterDateFormat original = new QuarterDateFormat(GMT, QUARTERS_AS_NUMBERS);
        
        QuarterDateFormat deserialized = TestUtils.serialised(original);

        assertEquals(original, deserialized, "Deserialized object should be equal to the original.");
    }
}