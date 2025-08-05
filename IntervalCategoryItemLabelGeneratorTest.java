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

    // ========== Equality Tests ==========
    
    /**
     * Verifies that two instances created with the default constructor are equal.
     */
    @Test
    public void testEquals_DefaultInstancesAreEqual() {
        IntervalCategoryItemLabelGenerator g1 = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator g2 = new IntervalCategoryItemLabelGenerator();
        assertEquals(g1, g2, "Default instances should be equal");
    }

    /**
     * Verifies that different label formats cause inequality.
     */
    @Test
    public void testEquals_NonDefaultLabelFormatCausesInequality() {
        // Create generators with different label formats
        IntervalCategoryItemLabelGenerator defaultGenerator = 
            new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator customGenerator = 
            new IntervalCategoryItemLabelGenerator("{3} - {4}", new DecimalFormat("0.000"));
        
        assertNotEquals(defaultGenerator, customGenerator, 
            "Generators with different label formats should not be equal");
    }

    /**
     * Verifies that identical label formats and number formatters produce equality.
     */
    @Test
    public void testEquals_SameLabelFormatAndNumberFormatProduceEquality() {
        // Create two generators with identical custom configuration
        IntervalCategoryItemLabelGenerator g1 = 
            new IntervalCategoryItemLabelGenerator("{3} - {4}", new DecimalFormat("0.000"));
        IntervalCategoryItemLabelGenerator g2 = 
            new IntervalCategoryItemLabelGenerator("{3} - {4}", new DecimalFormat("0.000"));
        
        assertEquals(g1, g2, 
            "Generators with identical label formats and number formatters should be equal");
    }

    /**
     * Verifies that different formatter types (Number vs Date) cause inequality.
     */
    @Test
    public void testEquals_NumberFormatAndDateFormatCauseInequality() {
        // Create generators with same label format but different formatter types
        IntervalCategoryItemLabelGenerator numberGenerator = 
            new IntervalCategoryItemLabelGenerator("{3} - {4}", new DecimalFormat("0.000"));
        IntervalCategoryItemLabelGenerator dateGenerator = 
            new IntervalCategoryItemLabelGenerator("{3} - {4}", new SimpleDateFormat("d-MMM"));
        
        assertNotEquals(numberGenerator, dateGenerator, 
            "Generators with different formatter types should not be equal");
    }

    /**
     * Verifies that identical label formats and date formatters produce equality.
     */
    @Test
    public void testEquals_SameLabelFormatAndDateFormatProduceEquality() {
        // Create two generators with identical date formatters
        IntervalCategoryItemLabelGenerator g1 = 
            new IntervalCategoryItemLabelGenerator("{3} - {4}", new SimpleDateFormat("d-MMM"));
        IntervalCategoryItemLabelGenerator g2 = 
            new IntervalCategoryItemLabelGenerator("{3} - {4}", new SimpleDateFormat("d-MMM"));
        
        assertEquals(g1, g2, 
            "Generators with identical label formats and date formatters should be equal");
    }

    // ========== HashCode Tests ==========
    
    /**
     * Verifies that equal objects have the same hash code.
     */
    @Test
    public void testHashCode_EqualObjectsHaveSameHashCode() {
        IntervalCategoryItemLabelGenerator g1 = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator g2 = new IntervalCategoryItemLabelGenerator();
        
        assertEquals(g1, g2, "Objects must be equal for hashCode test");
        assertEquals(g1.hashCode(), g2.hashCode(), 
            "Equal objects must have identical hash codes");
    }

    // ========== Cloning Tests ==========
    
    /**
     * Verifies that cloning creates a distinct but equal copy.
     */
    @Test
    public void testCloning_CreatesDistinctButEqualCopy() throws CloneNotSupportedException {
        IntervalCategoryItemLabelGenerator original = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator clone = (IntervalCategoryItemLabelGenerator) original.clone();
        
        assertNotSame(original, clone, "Cloned object should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Cloned object should be same class");
        assertEquals(original, clone, "Cloned object should be equal to original");
    }

    /**
     * Confirms that the class implements the PublicCloneable interface.
     */
    @Test
    public void testPublicCloneable_ImplementsInterface() {
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();
        assertTrue(generator instanceof PublicCloneable, 
            "Class must implement PublicCloneable interface");
    }

    // ========== Serialization Tests ==========
    
    /**
     * Verifies that serialization preserves object equality.
     */
    @Test
    public void testSerialization_RoundTripPreservesEquality() {
        // Create generator with non-default configuration
        IntervalCategoryItemLabelGenerator original = 
            new IntervalCategoryItemLabelGenerator("{3} - {4}", DateFormat.getInstance());
        
        // Serialize and deserialize
        IntervalCategoryItemLabelGenerator deserialized = TestUtils.serialised(original);
        
        assertEquals(original, deserialized, 
            "Deserialized object should equal original");
    }

}