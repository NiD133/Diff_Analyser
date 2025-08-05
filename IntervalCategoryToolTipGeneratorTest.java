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

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link IntervalCategoryToolTipGenerator} class.
 */
public class IntervalCategoryToolTipGeneratorTest {

    // Test data constants for better readability
    private static final String CUSTOM_FORMAT = "{3} - {4}";
    private static final String DECIMAL_PATTERN = "0.000";
    private static final String DATE_PATTERN = "d-MMM";

    @Test
    public void testEquals_DefaultConstructors_ShouldBeEqual() {
        // Given: Two generators created with default constructors
        IntervalCategoryToolTipGenerator generator1 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator generator2 = new IntervalCategoryToolTipGenerator();
        
        // Then: They should be equal (reflexive and symmetric)
        assertEquals(generator1, generator2);
        assertEquals(generator2, generator1);
    }

    @Test
    public void testEquals_DifferentNumberFormatters_ShouldNotBeEqual() {
        // Given: One generator with default constructor, another with custom decimal format
        IntervalCategoryToolTipGenerator defaultGenerator = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator customGenerator = new IntervalCategoryToolTipGenerator(
                CUSTOM_FORMAT, new DecimalFormat(DECIMAL_PATTERN));
        
        // Then: They should not be equal
        assertNotEquals(customGenerator, defaultGenerator);
    }

    @Test
    public void testEquals_SameNumberFormatters_ShouldBeEqual() {
        // Given: Two generators with identical custom decimal formatters
        IntervalCategoryToolTipGenerator generator1 = new IntervalCategoryToolTipGenerator(
                CUSTOM_FORMAT, new DecimalFormat(DECIMAL_PATTERN));
        IntervalCategoryToolTipGenerator generator2 = new IntervalCategoryToolTipGenerator(
                CUSTOM_FORMAT, new DecimalFormat(DECIMAL_PATTERN));
        
        // Then: They should be equal
        assertEquals(generator1, generator2);
    }

    @Test
    public void testEquals_DifferentDateFormatters_ShouldNotBeEqual() {
        // Given: One generator with decimal format, another with date format
        IntervalCategoryToolTipGenerator decimalGenerator = new IntervalCategoryToolTipGenerator(
                CUSTOM_FORMAT, new DecimalFormat(DECIMAL_PATTERN));
        IntervalCategoryToolTipGenerator dateGenerator = new IntervalCategoryToolTipGenerator(
                CUSTOM_FORMAT, new SimpleDateFormat(DATE_PATTERN));
        
        // Then: They should not be equal
        assertNotEquals(dateGenerator, decimalGenerator);
    }

    @Test
    public void testEquals_SameDateFormatters_ShouldBeEqual() {
        // Given: Two generators with identical date formatters
        IntervalCategoryToolTipGenerator generator1 = new IntervalCategoryToolTipGenerator(
                CUSTOM_FORMAT, new SimpleDateFormat(DATE_PATTERN));
        IntervalCategoryToolTipGenerator generator2 = new IntervalCategoryToolTipGenerator(
                CUSTOM_FORMAT, new SimpleDateFormat(DATE_PATTERN));
        
        // Then: They should be equal
        assertEquals(generator1, generator2);
    }

    @Test
    public void testEquals_SubclassVsSuperclass_ShouldNotBeEqual() {
        // Given: An IntervalCategoryToolTipGenerator and its superclass StandardCategoryToolTipGenerator
        IntervalCategoryToolTipGenerator intervalGenerator = new IntervalCategoryToolTipGenerator();
        StandardCategoryToolTipGenerator standardGenerator = new StandardCategoryToolTipGenerator(
                IntervalCategoryToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT_STRING,
                NumberFormat.getInstance());
        
        // Then: Subclass should not equal superclass instance
        assertNotEquals(intervalGenerator, standardGenerator);
    }

    @Test
    public void testHashCode_EqualObjects_ShouldHaveSameHashCode() {
        // Given: Two equal generators
        IntervalCategoryToolTipGenerator generator1 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator generator2 = new IntervalCategoryToolTipGenerator();
        
        // When: Objects are equal
        assertEquals(generator1, generator2);
        
        // Then: Hash codes should also be equal
        assertEquals(generator1.hashCode(), generator2.hashCode());
    }

    @Test
    public void testCloning_ShouldCreateDistinctButEqualObject() throws CloneNotSupportedException {
        // Given: An original generator
        IntervalCategoryToolTipGenerator originalGenerator = new IntervalCategoryToolTipGenerator();
        
        // When: Cloning the generator
        IntervalCategoryToolTipGenerator clonedGenerator = CloneUtils.clone(originalGenerator);
        
        // Then: Clone should be a different instance but equal in value
        assertNotSame(originalGenerator, clonedGenerator, "Clone should be a different object instance");
        assertSame(originalGenerator.getClass(), clonedGenerator.getClass(), "Clone should have same class");
        assertEquals(originalGenerator, clonedGenerator, "Clone should be equal to original");
    }

    @Test
    public void testPublicCloneable_ShouldImplementInterface() {
        // Given: A generator instance
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        
        // Then: It should implement PublicCloneable interface
        assertTrue(generator instanceof PublicCloneable, 
                "IntervalCategoryToolTipGenerator should implement PublicCloneable");
    }

    @Test
    public void testSerialization_ShouldPreserveEquality() {
        // Given: A generator with custom formatting
        IntervalCategoryToolTipGenerator originalGenerator = new IntervalCategoryToolTipGenerator(
                CUSTOM_FORMAT, DateFormat.getInstance());
        
        // When: Serializing and deserializing the generator
        IntervalCategoryToolTipGenerator deserializedGenerator = TestUtils.serialised(originalGenerator);
        
        // Then: Deserialized object should equal the original
        assertEquals(originalGenerator, deserializedGenerator, 
                "Deserialized generator should equal the original");
    }
}