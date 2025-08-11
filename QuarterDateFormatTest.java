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

import java.util.TimeZone;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link QuarterDateFormat} class.
 */
public class QuarterDateFormatTest {

    // Test data constants for better readability and reusability
    private static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
    private static final TimeZone PST_TIMEZONE = TimeZone.getTimeZone("PST");
    private static final String[] NUMERIC_QUARTERS = {"1", "2", "3", "4"};
    private static final String[] CUSTOM_QUARTERS = {"A", "2", "3", "4"};

    /**
     * Tests that the equals method correctly distinguishes all required fields:
     * - TimeZone
     * - Quarter symbols array
     * - Quarter-first flag
     */
    @Test
    public void testEquals() {
        // Test 1: Identical objects should be equal
        QuarterDateFormat format1 = createQuarterDateFormat(GMT_TIMEZONE, NUMERIC_QUARTERS);
        QuarterDateFormat format2 = createQuarterDateFormat(GMT_TIMEZONE, NUMERIC_QUARTERS);
        
        assertFormatsAreEqual(format1, format2, "Identical QuarterDateFormat objects should be equal");

        // Test 2: Different timezones should not be equal
        QuarterDateFormat formatWithPST = createQuarterDateFormat(PST_TIMEZONE, NUMERIC_QUARTERS);
        assertFormatsAreNotEqual(format1, formatWithPST, "QuarterDateFormat objects with different timezones should not be equal");
        
        // Verify PST formats are equal to each other
        QuarterDateFormat anotherPSTFormat = createQuarterDateFormat(PST_TIMEZONE, NUMERIC_QUARTERS);
        assertFormatsAreEqual(formatWithPST, anotherPSTFormat, "QuarterDateFormat objects with same PST timezone should be equal");

        // Test 3: Different quarter symbols should not be equal
        QuarterDateFormat formatWithCustomQuarters = createQuarterDateFormat(PST_TIMEZONE, CUSTOM_QUARTERS);
        assertFormatsAreNotEqual(formatWithPST, formatWithCustomQuarters, "QuarterDateFormat objects with different quarter symbols should not be equal");
        
        // Verify custom quarter formats are equal to each other
        QuarterDateFormat anotherCustomFormat = createQuarterDateFormat(PST_TIMEZONE, CUSTOM_QUARTERS);
        assertFormatsAreEqual(formatWithCustomQuarters, anotherCustomFormat, "QuarterDateFormat objects with same custom quarters should be equal");

        // Test 4: Different quarterFirst flag should not be equal
        QuarterDateFormat formatWithQuarterFirst = createQuarterDateFormatWithFlag(PST_TIMEZONE, CUSTOM_QUARTERS, true);
        assertFormatsAreNotEqual(formatWithCustomQuarters, formatWithQuarterFirst, "QuarterDateFormat objects with different quarterFirst flags should not be equal");
        
        // Verify quarter-first formats are equal to each other
        QuarterDateFormat anotherQuarterFirstFormat = createQuarterDateFormatWithFlag(PST_TIMEZONE, CUSTOM_QUARTERS, true);
        assertFormatsAreEqual(formatWithQuarterFirst, anotherQuarterFirstFormat, "QuarterDateFormat objects with same quarterFirst flag should be equal");
    }

    /**
     * Tests that equal objects return the same hash code (required by the equals/hashCode contract).
     */
    @Test
    public void testHashCode() {
        QuarterDateFormat format1 = createQuarterDateFormat(GMT_TIMEZONE, NUMERIC_QUARTERS);
        QuarterDateFormat format2 = createQuarterDateFormat(GMT_TIMEZONE, NUMERIC_QUARTERS);
        
        assertEquals(format1, format2, "Formats should be equal before testing hash codes");
        
        int hashCode1 = format1.hashCode();
        int hashCode2 = format2.hashCode();
        
        assertEquals(hashCode1, hashCode2, "Equal QuarterDateFormat objects must have equal hash codes");
    }

    /**
     * Tests that cloning creates a proper deep copy of the QuarterDateFormat object.
     */
    @Test
    public void testCloning() {
        QuarterDateFormat originalFormat = createQuarterDateFormat(GMT_TIMEZONE, NUMERIC_QUARTERS);
        
        QuarterDateFormat clonedFormat = (QuarterDateFormat) originalFormat.clone();
        
        // Verify cloning contract
        assertNotSame(originalFormat, clonedFormat, "Cloned object should be a different instance");
        assertSame(originalFormat.getClass(), clonedFormat.getClass(), "Cloned object should have the same class");
        assertEquals(originalFormat, clonedFormat, "Cloned object should be equal to the original");
    }

    /**
     * Tests that serialization and deserialization preserve object equality.
     */
    @Test
    public void testSerialization() {
        QuarterDateFormat originalFormat = createQuarterDateFormat(GMT_TIMEZONE, NUMERIC_QUARTERS);
        
        QuarterDateFormat deserializedFormat = TestUtils.serialised(originalFormat);
        
        assertEquals(originalFormat, deserializedFormat, "Deserialized QuarterDateFormat should be equal to the original");
    }

    // Helper methods to improve readability and reduce code duplication

    /**
     * Creates a QuarterDateFormat with the specified timezone and quarter symbols.
     */
    private QuarterDateFormat createQuarterDateFormat(TimeZone timeZone, String[] quarterSymbols) {
        return new QuarterDateFormat(timeZone, quarterSymbols);
    }

    /**
     * Creates a QuarterDateFormat with the specified timezone, quarter symbols, and quarterFirst flag.
     */
    private QuarterDateFormat createQuarterDateFormatWithFlag(TimeZone timeZone, String[] quarterSymbols, boolean quarterFirst) {
        return new QuarterDateFormat(timeZone, quarterSymbols, quarterFirst);
    }

    /**
     * Asserts that two QuarterDateFormat objects are equal (both directions) with a descriptive message.
     */
    private void assertFormatsAreEqual(QuarterDateFormat format1, QuarterDateFormat format2, String message) {
        assertEquals(format1, format2, message);
        assertEquals(format2, format1, message + " (symmetric)");
    }

    /**
     * Asserts that two QuarterDateFormat objects are not equal with a descriptive message.
     */
    private void assertFormatsAreNotEqual(QuarterDateFormat format1, QuarterDateFormat format2, String message) {
        assertNotEquals(format1, format2, message);
    }
}