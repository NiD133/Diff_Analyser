/*
 * Test suite for IntervalCategoryToolTipGenerator
 * Tests tooltip generation for interval category datasets
 */

package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.chrono.ChronoLocalDate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.text.MockDateFormat;
import org.evosuite.runtime.mock.java.text.MockSimpleDateFormat;
import org.jfree.chart.labels.IntervalCategoryToolTipGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class IntervalCategoryToolTipGenerator_ESTest extends IntervalCategoryToolTipGenerator_ESTest_scaffolding {

    // ========== Constructor Validation Tests ==========
    
    @Test(timeout = 4000)
    public void testConstructorWithNullNumberFormatter_ShouldThrowException() throws Throwable {
        // Given: A null NumberFormat
        NumberFormat nullFormatter = null;
        String validLabelFormat = "({0}, {1}) = {2}";
        
        // When & Then: Creating generator should throw IllegalArgumentException
        try {
            new IntervalCategoryToolTipGenerator(validLabelFormat, nullFormatter);
            fail("Expected IllegalArgumentException for null NumberFormat");
        } catch(IllegalArgumentException e) {
            assertEquals("Null 'formatter' argument.", e.getMessage());
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullDateFormatter_ShouldThrowException() throws Throwable {
        // Given: A null DateFormat
        DateFormat nullFormatter = null;
        String validLabelFormat = "({0}, {1}) = {2}";
        
        // When & Then: Creating generator should throw IllegalArgumentException
        try {
            new IntervalCategoryToolTipGenerator(validLabelFormat, nullFormatter);
            fail("Expected IllegalArgumentException for null DateFormat");
        } catch(IllegalArgumentException e) {
            assertEquals("Null 'formatter' argument.", e.getMessage());
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    // ========== createItemArray Method Tests ==========
    
    @Test(timeout = 4000)
    public void testCreateItemArrayWithNullDataset_ShouldThrowException() throws Throwable {
        // Given: A generator and null dataset
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        CategoryDataset nullDataset = null;
        int validRow = 70;
        int validColumn = 70;
        
        // When & Then: Should throw NullPointerException
        try {
            generator.createItemArray(nullDataset, validRow, validColumn);
            fail("Expected NullPointerException for null dataset");
        } catch(NullPointerException e) {
            verifyException("org.jfree.chart.labels.IntervalCategoryToolTipGenerator", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArrayWithNegativeIndices_ShouldThrowException() throws Throwable {
        // Given: A generator with empty dataset and negative indices
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        DefaultBoxAndWhiskerCategoryDataset<ChronoLocalDate, ChronoLocalDate> emptyDataset = 
            new DefaultBoxAndWhiskerCategoryDataset<>();
        int negativeRow = -2580;
        int negativeColumn = -2580;
        
        // When & Then: Should throw IndexOutOfBoundsException
        try {
            generator.createItemArray(emptyDataset, negativeRow, negativeColumn);
            fail("Expected IndexOutOfBoundsException for negative indices");
        } catch(IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArrayWithInvalidRowIndex_ShouldThrowException() throws Throwable {
        // Given: A dataset with no rows and invalid row index
        double[][] emptyStartValues = new double[0][1];
        double[][] emptyEndValues = new double[0][1];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(emptyStartValues, emptyEndValues);
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        int invalidRowIndex = 1696;
        int validColumnIndex = 1696;
        
        // When & Then: Should throw IllegalArgumentException
        try {
            generator.createItemArray(dataset, invalidRowIndex, validColumnIndex);
            fail("Expected IllegalArgumentException for out-of-bounds row");
        } catch(IllegalArgumentException e) {
            assertEquals("The 'row' argument is out of bounds.", e.getMessage());
            verifyException("org.jfree.data.category.DefaultIntervalCategoryDataset", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArrayWithInvalidColumnIndex_ShouldThrowException() throws Throwable {
        // Given: A dataset with limited columns and invalid column index
        double[][] startValues = new double[14][4]; // 14 rows, 4 columns
        double[][] endValues = new double[14][4];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(startValues, endValues);
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        int validRowIndex = 13;
        int invalidColumnIndex = 13; // Only 4 columns available (0-3)
        
        // When & Then: Should throw ArrayIndexOutOfBoundsException
        try {
            generator.createItemArray(dataset, validRowIndex, invalidColumnIndex);
            fail("Expected ArrayIndexOutOfBoundsException for out-of-bounds column");
        } catch(ArrayIndexOutOfBoundsException e) {
            assertEquals("Index 13 out of bounds for length 4", e.getMessage());
            verifyException("org.jfree.data.category.DefaultIntervalCategoryDataset", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArrayWithValidIndices_ShouldReturnCorrectArray() throws Throwable {
        // Given: A valid dataset and generator with default formatter
        double[][] startValues = new double[8][4];
        double[][] endValues = new double[8][4];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(startValues, endValues);
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        
        // When: Creating item array with valid indices
        Object[] result = generator.createItemArray(dataset, 0, 0);
        
        // Then: Should return array with 5 elements (series, category, value, start, end)
        assertNotNull("Result should not be null", result);
        assertEquals("Array should contain 5 elements", 5, result.length);
    }

    @Test(timeout = 4000)
    public void testCreateItemArrayWithDateFormatter_ShouldReturnCorrectArray() throws Throwable {
        // Given: A generator with date formatter and valid dataset
        MockSimpleDateFormat dateFormatter = new MockSimpleDateFormat();
        String customFormat = "org.jfree.chart.labels.IntervalCategoryToolTipGenerator";
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator(customFormat, dateFormatter);
        
        double[][] startValues = new double[5][4];
        double[][] endValues = new double[5][4];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(startValues, endValues);
        
        // When: Creating item array
        Object[] result = generator.createItemArray(dataset, 0, 0);
        
        // Then: Should return array with 5 elements
        assertNotNull("Result should not be null", result);
        assertEquals("Array should contain 5 elements", 5, result.length);
    }

    @Test(timeout = 4000)
    public void testCreateItemArrayWithDifferentIndices_ShouldReturnCorrectArray() throws Throwable {
        // Given: A generator with date formatter and valid dataset
        String customFormat = "t:RbWj+5v<m\\uAAUW";
        DateFormat dateFormatter = DateFormat.getDateTimeInstance(0, 0);
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator(customFormat, dateFormatter);
        
        double[][] startValues = new double[7][4];
        double[][] endValues = new double[7][4];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(startValues, endValues);
        
        // When: Creating item array with different valid indices
        Object[] result = generator.createItemArray(dataset, 0, 3);
        
        // Then: Should return array with 5 elements
        assertNotNull("Result should not be null", result);
        assertEquals("Array should contain 5 elements", 5, result.length);
    }

    // ========== Equality Tests ==========
    
    @Test(timeout = 4000)
    public void testEquals_SameInstance_ShouldReturnTrue() throws Throwable {
        // Given: A single generator instance
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        
        // When & Then: Comparing with itself should return true
        assertTrue("Generator should equal itself", generator.equals(generator));
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentFormats_ShouldReturnFalse() throws Throwable {
        // Given: Two generators with different formats but same formatter
        DateFormat sharedFormatter = MockDateFormat.getTimeInstance();
        IntervalCategoryToolTipGenerator generator1 = new IntervalCategoryToolTipGenerator("", sharedFormatter);
        IntervalCategoryToolTipGenerator generator2 = new IntervalCategoryToolTipGenerator("({0}, {1}) = {3} - {4}", sharedFormatter);
        
        // When & Then: Should not be equal due to different formats
        assertFalse("Generators with different formats should not be equal", generator1.equals(generator2));
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentObjectType_ShouldReturnFalse() throws Throwable {
        // Given: A generator and a string object
        NumberFormat numberFormatter = NumberFormat.getPercentInstance();
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator("Te{[wtf@3LG|<`-", numberFormatter);
        String differentTypeObject = "Te{[wtf@3LG|<`-";
        
        // When & Then: Should not be equal to different object type
        assertFalse("Generator should not equal string object", generator.equals(differentTypeObject));
    }

    // ========== Hash Code Test ==========
    
    @Test(timeout = 4000)
    public void testHashCode_ShouldExecuteWithoutException() throws Throwable {
        // Given: A generator with default settings
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        
        // When & Then: Hash code calculation should not throw exception
        generator.hashCode(); // Should complete without throwing
    }
}