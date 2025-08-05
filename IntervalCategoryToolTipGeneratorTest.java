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

    private static final String CUSTOM_LABEL_FORMAT = "{3} - {4}";
    private static final String DEFAULT_LABEL_FORMAT = IntervalCategoryToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT_STRING;

    @Test
    public void testEquals_DefaultObjects() {
        // Create two generators with default settings
        IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator();
        
        // Verify they are equal
        assertEquals(g1, g2, "Default instances should be equal");
        assertEquals(g2, g1, "Equality should be symmetric");
    }

    @Test
    public void testEquals_WithDifferentLabelFormat() {
        // Create generators with different configurations
        IntervalCategoryToolTipGenerator defaultGenerator = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator customGenerator = new IntervalCategoryToolTipGenerator(
                CUSTOM_LABEL_FORMAT, new DecimalFormat("0.000"));

        // Verify inequality due to custom format
        assertNotEquals(defaultGenerator, customGenerator, 
                "Generators with different label formats should not be equal");

        // Create second generator with same custom format
        IntervalCategoryToolTipGenerator customGenerator2 = new IntervalCategoryToolTipGenerator(
                CUSTOM_LABEL_FORMAT, new DecimalFormat("0.000"));
        
        // Verify custom generators are equal
        assertEquals(customGenerator, customGenerator2, 
                "Generators with identical custom formats should be equal");
    }

    @Test
    public void testEquals_WithDifferentDateFormat() {
        // Create generator with date format
        IntervalCategoryToolTipGenerator dateFormatGenerator = new IntervalCategoryToolTipGenerator(
                CUSTOM_LABEL_FORMAT, new SimpleDateFormat("d-MMM"));
        
        // Create default generator for comparison
        IntervalCategoryToolTipGenerator defaultGenerator = new IntervalCategoryToolTipGenerator();
        
        // Verify inequality due to date format
        assertNotEquals(defaultGenerator, dateFormatGenerator,
                "Generators with different formatter types should not be equal");

        // Create second generator with same date format
        IntervalCategoryToolTipGenerator dateFormatGenerator2 = new IntervalCategoryToolTipGenerator(
                CUSTOM_LABEL_FORMAT, new SimpleDateFormat("d-MMM"));
        
        // Verify date format generators are equal
        assertEquals(dateFormatGenerator, dateFormatGenerator2,
                "Generators with identical date formats should be equal");
    }

    @Test
    public void testEquals_WithDifferentClass() {
        // Create generator and superclass instance with same format
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        StandardCategoryToolTipGenerator superclassGenerator = new StandardCategoryToolTipGenerator(
                DEFAULT_LABEL_FORMAT, NumberFormat.getInstance());
        
        // Verify different class types are not equal
        assertNotEquals(generator, superclassGenerator,
                "Should not be equal to superclass instance despite similar format");
    }

    @Test
    public void testHashCode_EqualObjects() {
        // Create two identical generators
        IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator();
        
        // Verify hash code consistency
        assertEquals(g1.hashCode(), g2.hashCode(),
                "Equal objects must have equal hash codes");
    }

    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Create and clone generator
        IntervalCategoryToolTipGenerator original = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator clone = CloneUtils.clone(original);
        
        // Verify cloning results
        assertNotSame(original, clone, "Cloned object should be different instance");
        assertSame(original.getClass(), clone.getClass(), "Cloned object should be same class");
        assertEquals(original, clone, "Cloned object should be equal to original");
    }

    @Test
    public void testPublicCloneable() {
        // Verify cloneability contract
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        assertTrue(generator instanceof PublicCloneable, 
                "Should implement PublicCloneable interface");
    }

    @Test
    public void testSerialization() {
        // Create and serialize generator
        IntervalCategoryToolTipGenerator original = new IntervalCategoryToolTipGenerator(
                CUSTOM_LABEL_FORMAT, DateFormat.getInstance());
        IntervalCategoryToolTipGenerator deserialized = TestUtils.serialised(original);
        
        // Verify serialization integrity
        assertEquals(original, deserialized,
                "Deserialized object should equal original");
    }

}