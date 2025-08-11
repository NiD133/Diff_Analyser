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
 * -----------------------------------------
 * IntervalCategoryToolTipGeneratorTest.java
 * -----------------------------------------
 * (C) Copyright 2008-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.labels;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link IntervalCategoryToolTipGenerator} class.
 */
public class IntervalCategoryToolTipGeneratorTest {

    // Common, self-explanatory constants used across tests
    private static final String CUSTOM_LABEL = "{3} - {4}";
    private static final String NUMBER_PATTERN = "0.000";
    private static final String DATE_PATTERN = "d-MMM";

    private static IntervalCategoryToolTipGenerator newWithNumberFormat() {
        return new IntervalCategoryToolTipGenerator(CUSTOM_LABEL, new DecimalFormat(NUMBER_PATTERN));
    }

    private static IntervalCategoryToolTipGenerator newWithDateFormat() {
        // Locale specified for determinism across environments
        return new IntervalCategoryToolTipGenerator(CUSTOM_LABEL, new SimpleDateFormat(DATE_PATTERN, Locale.US));
    }

    @Test
    @DisplayName("equals: default instances are equal (and symmetric)")
    public void equals_defaultInstancesAreEqual() {
        IntervalCategoryToolTipGenerator a = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator b = new IntervalCategoryToolTipGenerator();

        assertEquals(a, b);
        assertEquals(b, a); // symmetry
    }

    @Test
    @DisplayName("equals: differently configured instance is not equal; same NumberFormat is equal")
    public void equals_withNumberFormat() {
        IntervalCategoryToolTipGenerator defaultGen = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator numberGen1 = newWithNumberFormat();

        // Different configuration -> not equal
        assertNotEquals(numberGen1, defaultGen);

        // Same configuration -> equal
        IntervalCategoryToolTipGenerator numberGen2 = newWithNumberFormat();
        assertEquals(numberGen1, numberGen2);
    }

    @Test
    @DisplayName("equals: differently configured instance is not equal; same DateFormat is equal")
    public void equals_withDateFormat() {
        IntervalCategoryToolTipGenerator defaultGen = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator dateGen1 = newWithDateFormat();

        // Different configuration -> not equal
        assertNotEquals(dateGen1, defaultGen);

        // Same configuration -> equal
        IntervalCategoryToolTipGenerator dateGen2 = newWithDateFormat();
        assertEquals(dateGen1, dateGen2);
    }

    /**
     * Check that an instance is not equal to a different (superclass) type.
     */
    @Test
    @DisplayName("equals: not equal to an instance of the superclass")
    public void equals_notEqualToSuperclassInstance() {
        IntervalCategoryToolTipGenerator intervalGen = new IntervalCategoryToolTipGenerator();
        StandardCategoryToolTipGenerator standardGen =
                new StandardCategoryToolTipGenerator(
                        IntervalCategoryToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT_STRING,
                        NumberFormat.getInstance());

        assertNotEquals(intervalGen, standardGen);
    }

    /**
     * Simple check that equal objects have the same hashCode.
     */
    @Test
    @DisplayName("hashCode: equal objects must have equal hash codes")
    public void hashCode_consistentWithEquals() {
        IntervalCategoryToolTipGenerator a = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator b = new IntervalCategoryToolTipGenerator();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    /**
     * Confirm that cloning creates a distinct but equal instance.
     */
    @Test
    @DisplayName("clone: produces a distinct but equal copy")
    public void clone_producesEqualButDistinctCopy() throws CloneNotSupportedException {
        IntervalCategoryToolTipGenerator original = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator clone = CloneUtils.clone(original);

        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);
    }

    /**
     * Check that this class implements PublicCloneable (contract of the library).
     */
    @Test
    @DisplayName("implements PublicCloneable")
    public void implementsPublicCloneableInterface() {
        IntervalCategoryToolTipGenerator gen = new IntervalCategoryToolTipGenerator();
        assertTrue(gen instanceof PublicCloneable);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    @DisplayName("serialization: round-trip preserves equality")
    public void serialization_roundTripPreservesEquality() {
        IntervalCategoryToolTipGenerator original =
                new IntervalCategoryToolTipGenerator(CUSTOM_LABEL, DateFormat.getInstance());
        IntervalCategoryToolTipGenerator restored = TestUtils.serialised(original);

        assertEquals(original, restored);
    }
}