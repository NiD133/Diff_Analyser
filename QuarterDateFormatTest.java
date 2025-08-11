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

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final TimeZone PST = TimeZone.getTimeZone("PST");
    private static final String[] DEFAULT_SYMBOLS = new String[] {"1", "2", "3", "4"};
    private static final String[] MODIFIED_SYMBOLS = new String[] {"A", "2", "3", "4"};

    @Test
    public void identicalInstancesShouldBeEqual() {
        QuarterDateFormat qf1 = new QuarterDateFormat(GMT, DEFAULT_SYMBOLS);
        QuarterDateFormat qf2 = new QuarterDateFormat(GMT, DEFAULT_SYMBOLS);
        assertEquals(qf1, qf2, "Instances with same configuration should be equal");
    }

    @Test
    public void differentTimeZonesShouldNotBeEqual() {
        QuarterDateFormat qf1 = new QuarterDateFormat(PST, DEFAULT_SYMBOLS);
        QuarterDateFormat qf2 = new QuarterDateFormat(GMT, DEFAULT_SYMBOLS);
        assertNotEquals(qf1, qf2, "Instances with different time zones should not be equal");
    }

    @Test
    public void differentQuarterSymbolsShouldNotBeEqual() {
        QuarterDateFormat qf1 = new QuarterDateFormat(GMT, MODIFIED_SYMBOLS);
        QuarterDateFormat qf2 = new QuarterDateFormat(GMT, DEFAULT_SYMBOLS);
        assertNotEquals(qf1, qf2, "Instances with different quarter symbols should not be equal");
    }

    @Test
    public void identicalQuarterSymbolsShouldBeEqual() {
        QuarterDateFormat qf1 = new QuarterDateFormat(GMT, MODIFIED_SYMBOLS);
        QuarterDateFormat qf2 = new QuarterDateFormat(GMT, MODIFIED_SYMBOLS);
        assertEquals(qf1, qf2, "Instances with same quarter symbols should be equal");
    }

    @Test
    public void differentQuarterFirstFlagsShouldNotBeEqual() {
        QuarterDateFormat qf1 = new QuarterDateFormat(GMT, DEFAULT_SYMBOLS, true);
        QuarterDateFormat qf2 = new QuarterDateFormat(GMT, DEFAULT_SYMBOLS, false);
        assertNotEquals(qf1, qf2, "Instances with different quarterFirst flags should not be equal");
    }

    @Test
    public void identicalQuarterFirstFlagsShouldBeEqual() {
        QuarterDateFormat qf1 = new QuarterDateFormat(GMT, DEFAULT_SYMBOLS, true);
        QuarterDateFormat qf2 = new QuarterDateFormat(GMT, DEFAULT_SYMBOLS, true);
        assertEquals(qf1, qf2, "Instances with same quarterFirst flags should be equal");
    }

    @Test
    public void equalInstancesShouldHaveSameHashCode() {
        QuarterDateFormat qf1 = new QuarterDateFormat(GMT, DEFAULT_SYMBOLS);
        QuarterDateFormat qf2 = new QuarterDateFormat(GMT, DEFAULT_SYMBOLS);
        assertEquals(qf1.hashCode(), qf2.hashCode(), "Equal instances must have same hashCode");
    }

    @Test
    public void clonedInstanceShouldBeEqualButNotSame() {
        QuarterDateFormat original = new QuarterDateFormat(GMT, DEFAULT_SYMBOLS);
        QuarterDateFormat clone = (QuarterDateFormat) original.clone();
        
        assertNotSame(original, clone, "Clone should be a different object");
        assertSame(original.getClass(), clone.getClass(), "Clone should have same class");
        assertEquals(original, clone, "Clone should be equal to original");
    }

    @Test
    public void serializedInstanceShouldEqualOriginal() {
        QuarterDateFormat original = new QuarterDateFormat(GMT, DEFAULT_SYMBOLS);
        QuarterDateFormat deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized instance should equal original");
    }

}