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
 * -------------------------------------------
 * IntervalCategoryItemLabelGeneratorTest.java
 * -------------------------------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.labels;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link IntervalCategoryItemLabelGenerator} class.
 */
public class IntervalCategoryItemLabelGeneratorTest {

    // Test constants for better readability and maintainability
    private static final String CUSTOM_LABEL_FORMAT = "{3} - {4}";
    private static final String DECIMAL_PATTERN = "0.000";
    private static final String DATE_PATTERN = "d-MMM";

    /**
     * Tests the equals() method to ensure proper equality comparison.
     * Verifies that generators are equal when they have the same configuration.
     */
    @Test
    public void testEquals() {
        // Test default constructors produce equal objects
        IntervalCategoryItemLabelGenerator defaultGenerator1 = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator defaultGenerator2 = new IntervalCategoryItemLabelGenerator();
        
        assertEquals(defaultGenerator1, defaultGenerator2, "Default generators should be equal");
        assertEquals(defaultGenerator2, defaultGenerator1, "Equality should be symmetric");

        // Test generators with custom decimal format
        IntervalCategoryItemLabelGenerator decimalGenerator1 = new IntervalCategoryItemLabelGenerator(
                CUSTOM_LABEL_FORMAT, new DecimalFormat(DECIMAL_PATTERN));
        
        assertNotEquals(decimalGenerator1, defaultGenerator2, 
                "Generator with custom format should not equal default generator");
        
        IntervalCategoryItemLabelGenerator decimalGenerator2 = new IntervalCategoryItemLabelGenerator(
                CUSTOM_LABEL_FORMAT, new DecimalFormat(DECIMAL_PATTERN));
        
        assertEquals(decimalGenerator1, decimalGenerator2, 
                "Generators with same decimal format should be equal");

        // Test generators with custom date format
        IntervalCategoryItemLabelGenerator dateGenerator1 = new IntervalCategoryItemLabelGenerator(
                CUSTOM_LABEL_FORMAT, new SimpleDateFormat(DATE_PATTERN));
        
        assertNotEquals(dateGenerator1, decimalGenerator2, 
                "Generator with date format should not equal generator with decimal format");
        
        IntervalCategoryItemLabelGenerator dateGenerator2 = new IntervalCategoryItemLabelGenerator(
                CUSTOM_LABEL_FORMAT, new SimpleDateFormat(DATE_PATTERN));
        
        assertEquals(dateGenerator1, dateGenerator2, 
                "Generators with same date format should be equal");
    }

    /**
     * Tests that hashCode() is properly implemented and consistent with equals().
     * Equal objects must have equal hash codes.
     */
    @Test
    public void testHashCode() {
        IntervalCategoryItemLabelGenerator generator1 = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator generator2 = new IntervalCategoryItemLabelGenerator();
        
        assertEquals(generator1, generator2, "Generators should be equal");
        assertEquals(generator1.hashCode(), generator2.hashCode(), 
                "Equal generators must have equal hash codes");
    }

    /**
     * Tests that cloning creates a proper deep copy of the generator.
     * The clone should be equal to the original but not the same instance.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        IntervalCategoryItemLabelGenerator originalGenerator = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator clonedGenerator = 
                (IntervalCategoryItemLabelGenerator) originalGenerator.clone();
        
        assertNotSame(originalGenerator, clonedGenerator, 
                "Clone should be a different instance");
        assertSame(originalGenerator.getClass(), clonedGenerator.getClass(), 
                "Clone should have the same class");
        assertEquals(originalGenerator, clonedGenerator, 
                "Clone should be equal to original");
    }

    /**
     * Verifies that the class implements the PublicCloneable interface.
     * This ensures the class can be cloned by external code.
     */
    @Test
    public void testImplementsPublicCloneable() {
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();
        
        assertTrue(generator instanceof PublicCloneable, 
                "Generator should implement PublicCloneable interface");
    }

    /**
     * Tests serialization and deserialization to ensure the generator
     * maintains its state when serialized and restored.
     */
    @Test
    public void testSerialization() {
        IntervalCategoryItemLabelGenerator originalGenerator = new IntervalCategoryItemLabelGenerator(
                CUSTOM_LABEL_FORMAT, DateFormat.getInstance());
        
        IntervalCategoryItemLabelGenerator deserializedGenerator = TestUtils.serialised(originalGenerator);
        
        assertEquals(originalGenerator, deserializedGenerator, 
                "Deserialized generator should equal original");
    }
}