package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.chrono.ThaiBuddhistEra;
import javax.swing.JLayeredPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.text.MockDateFormat;
import org.jfree.chart.labels.IntervalCategoryItemLabelGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.junit.runner.RunWith;

/**
 * Test suite for IntervalCategoryItemLabelGenerator class.
 * Tests the creation of item arrays for label generation from interval category datasets.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true)
public class IntervalCategoryItemLabelGenerator_ESTest extends IntervalCategoryItemLabelGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCreateItemArray_WithIncompatibleDataType_ThrowsIllegalArgumentException() throws Throwable {
        // Given: A label generator with default settings
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator();
        
        // And: A statistical dataset with mixed data types (Integer and ThaiBuddhistEra)
        DefaultStatisticalCategoryDataset<ThaiBuddhistEra, ThaiBuddhistEra> dataset = 
            new DefaultStatisticalCategoryDataset<>();
        
        ThaiBuddhistEra era1 = ThaiBuddhistEra.BE;
        ThaiBuddhistEra era2 = ThaiBuddhistEra.BEFORE_BE;
        Integer dragLayerValue = JLayeredPane.DRAG_LAYER;
        
        dataset.add(dragLayerValue, dragLayerValue, era2, era2);
        dataset.add(0.0, 0.0, era1, era1);
        
        // When & Then: Attempting to create item array should throw IllegalArgumentException
        // because ThaiBuddhistEra cannot be formatted as a Number
        try {
            labelGenerator.createItemArray(dataset, 0, 1);
            fail("Expected IllegalArgumentException for incompatible data type");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot format given Object as a Number", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_WithInvalidColumnIndex_ThrowsIndexOutOfBoundsException() throws Throwable {
        // Given: A label generator with default settings
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator();
        
        // And: A statistical dataset with only one column (index 0)
        DefaultStatisticalCategoryDataset<ThaiBuddhistEra, ThaiBuddhistEra> dataset = 
            new DefaultStatisticalCategoryDataset<>();
        
        ThaiBuddhistEra era = ThaiBuddhistEra.BE;
        Integer value = JLayeredPane.DRAG_LAYER;
        dataset.add(value, value, era, era);
        
        // When & Then: Attempting to access column index 1 (which doesn't exist) should throw exception
        try {
            labelGenerator.createItemArray(dataset, 0, 1);
            fail("Expected IndexOutOfBoundsException for invalid column index");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_WithNullDataset_ThrowsNullPointerException() throws Throwable {
        // Given: A label generator with default settings
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator();
        
        // When & Then: Passing null dataset should throw NullPointerException
        try {
            labelGenerator.createItemArray(null, 0, 0);
            fail("Expected NullPointerException for null dataset");
        } catch (NullPointerException e) {
            // Expected behavior - null dataset is not allowed
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_WithInvalidRowIndex_ThrowsIllegalArgumentException() throws Throwable {
        // Given: A label generator with default settings
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator();
        
        // And: An empty interval dataset (0 rows, 3 columns)
        double[][] emptyStartValues = new double[0][3];
        double[][] emptyEndValues = new double[0][3];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(emptyStartValues, emptyEndValues);
        
        // When & Then: Attempting to access row index 2 (which doesn't exist) should throw exception
        try {
            labelGenerator.createItemArray(dataset, 2, 2);
            fail("Expected IllegalArgumentException for invalid row index");
        } catch (IllegalArgumentException e) {
            assertEquals("The 'row' argument is out of bounds.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_WithInvalidColumnData_ThrowsArrayIndexOutOfBoundsException() throws Throwable {
        // Given: A label generator with default settings
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator();
        
        // And: A dataset with 7 rows but 0 columns
        double[][] startValues = new double[7][0];
        double[][] endValues = new double[7][0];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(startValues, endValues);
        
        // When & Then: Attempting to access column 0 should throw ArrayIndexOutOfBoundsException
        try {
            labelGenerator.createItemArray(dataset, 0, 0);
            fail("Expected ArrayIndexOutOfBoundsException for invalid column data");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Index 0 out of bounds for length 0", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_WithNullNumberFormatter_ThrowsIllegalArgumentException() throws Throwable {
        // When & Then: Creating generator with null NumberFormat should throw exception
        try {
            new IntervalCategoryItemLabelGenerator("{2}", (NumberFormat) null);
            fail("Expected IllegalArgumentException for null NumberFormat");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'formatter' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_WithNullDateFormatter_ThrowsIllegalArgumentException() throws Throwable {
        // When & Then: Creating generator with null DateFormat should throw exception
        try {
            new IntervalCategoryItemLabelGenerator("{2}", (DateFormat) null);
            fail("Expected IllegalArgumentException for null DateFormat");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'formatter' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_WithDateFormatter_ReturnsCorrectArraySize() throws Throwable {
        // Given: An interval dataset with valid data
        double[][] startValues = new double[1][3];
        double[][] endValues = new double[1][3];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(startValues, endValues);
        
        // And: A label generator with custom format and date formatter
        DateFormat dateFormatter = MockDateFormat.getDateTimeInstance();
        IntervalCategoryItemLabelGenerator labelGenerator = 
            new IntervalCategoryItemLabelGenerator("{2}", dateFormatter);
        
        // When: Creating item array for valid indices
        Object[] itemArray = labelGenerator.createItemArray(dataset, 0, 0);
        
        // Then: Should return array with 5 elements (standard format for interval data)
        assertEquals("Item array should contain 5 elements", 5, itemArray.length);
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_WithStatisticalDataset_ReturnsCorrectArraySize() throws Throwable {
        // Given: A statistical dataset with valid numeric data
        DefaultStatisticalCategoryDataset<ThaiBuddhistEra, ThaiBuddhistEra> dataset = 
            new DefaultStatisticalCategoryDataset<>();
        
        ThaiBuddhistEra era = ThaiBuddhistEra.BEFORE_BE;
        double testValue = -2270.359128073;
        dataset.add(testValue, testValue, era, era);
        
        // And: A label generator with custom format and date formatter
        DateFormat dateFormatter = DateFormat.getDateTimeInstance();
        IntervalCategoryItemLabelGenerator labelGenerator = 
            new IntervalCategoryItemLabelGenerator("({0}, {1}) = {3} - {4}", dateFormatter);
        
        // When: Creating item array for the data
        Object[] itemArray = labelGenerator.createItemArray(dataset, 0, 0);
        
        // Then: Should return array with 5 elements
        assertEquals("Item array should contain 5 elements", 5, itemArray.length);
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_WithDefaultGenerator_ReturnsCorrectArraySize() throws Throwable {
        // Given: A default label generator
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator();
        
        // And: An interval dataset with sufficient data
        double[][] startValues = new double[8][3];
        double[][] endValues = new double[8][3];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(startValues, endValues);
        
        // When: Creating item array for valid indices
        Object[] itemArray = labelGenerator.createItemArray(dataset, 2, 2);
        
        // Then: Should return array with 5 elements
        assertEquals("Item array should contain 5 elements", 5, itemArray.length);
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_WithCustomDecimalFormatter_ReturnsCorrectArraySize() throws Throwable {
        // Given: A custom decimal formatter and label generator
        DecimalFormat customFormatter = new DecimalFormat("qa] ~&9");
        IntervalCategoryItemLabelGenerator labelGenerator = 
            new IntervalCategoryItemLabelGenerator("O`zwofaJ", customFormatter);
        
        // And: An interval dataset with extended column data
        double[][] startValues = new double[3][0];
        double[][] endValues = new double[3][0];
        double[] columnData = new double[5];
        
        startValues[0] = columnData;
        startValues[1] = columnData;
        endValues[0] = columnData;
        endValues[1] = columnData;
        
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(startValues, endValues);
        
        // When: Creating item array for valid indices
        Object[] itemArray = labelGenerator.createItemArray(dataset, 1, 0);
        
        // Then: Should return array with 5 elements
        assertEquals("Item array should contain 5 elements", 5, itemArray.length);
    }
}