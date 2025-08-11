package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.util.MockDate;
import org.jfree.chart.date.SerialDate;
import org.jfree.chart.labels.SymbolicXYItemLabelGenerator;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.runner.RunWith;

/**
 * Test suite for SymbolicXYItemLabelGenerator class.
 * Tests tooltip generation, label generation, and basic object operations.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                    resetStaticState = true, separateClassLoader = true) 
public class SymbolicXYItemLabelGenerator_ESTest extends SymbolicXYItemLabelGenerator_ESTest_scaffolding {

    // Constants for test data
    private static final int SERIES_INDEX = 0;
    private static final int ITEM_INDEX = 0;
    private static final long TEST_DATE_VALUE = 102L;
    private static final int INVALID_SERIES_INDEX = 1970;
    private static final int INVALID_ITEM_INDEX = 1970;

    @Test(timeout = 4000)
    public void testGenerateToolTip_WithDynamicTimeSeriesCollection_ThrowsNullPointerException() throws Throwable {
        // Given: A label generator and a dynamic time series collection with incomplete data
        SymbolicXYItemLabelGenerator labelGenerator = new SymbolicXYItemLabelGenerator();
        DynamicTimeSeriesCollection dataset = createDynamicTimeSeriesWithIncompleteData();
        
        // When & Then: Generating tooltip should throw NullPointerException due to incomplete data
        try {
            labelGenerator.generateToolTip(dataset, 1, 2);
            fail("Expected NullPointerException due to incomplete time series data");
        } catch (NullPointerException e) {
            // Expected behavior - incomplete time series data causes NPE
            assertTrue("Exception should originate from DynamicTimeSeriesCollection", 
                      e.getStackTrace()[0].getClassName().contains("DynamicTimeSeriesCollection"));
        }
    }

    @Test(timeout = 4000)
    public void testGenerateToolTip_WithValidHighLowDataset_ReturnsFormattedString() throws Throwable {
        // Given: A label generator and a properly configured high-low dataset
        SymbolicXYItemLabelGenerator labelGenerator = new SymbolicXYItemLabelGenerator();
        DefaultHighLowDataset dataset = createValidHighLowDataset();
        
        // When: Generating tooltip for valid data point
        String tooltip = labelGenerator.generateToolTip(dataset, SERIES_INDEX, ITEM_INDEX);
        
        // Then: Should return properly formatted tooltip
        assertEquals("Tooltip should show X and Y values in expected format", 
                    "X: 102.0, Y: 0.0", tooltip);
    }

    @Test(timeout = 4000)
    public void testGenerateToolTip_WithEmptyDataset_ThrowsIndexOutOfBoundsException() throws Throwable {
        // Given: A label generator and an empty dataset
        SymbolicXYItemLabelGenerator labelGenerator = new SymbolicXYItemLabelGenerator();
        DefaultTableXYDataset<ChronoLocalDate> emptyDataset = new DefaultTableXYDataset<>();
        
        // When & Then: Accessing non-existent data should throw IndexOutOfBoundsException
        try {
            labelGenerator.generateToolTip(emptyDataset, INVALID_SERIES_INDEX, INVALID_ITEM_INDEX);
            fail("Expected IndexOutOfBoundsException when accessing empty dataset");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior when trying to access data that doesn't exist
        }
    }

    @Test(timeout = 4000)
    public void testGenerateToolTip_WithNullDataset_ThrowsIllegalArgumentException() throws Throwable {
        // Given: A label generator
        SymbolicXYItemLabelGenerator labelGenerator = new SymbolicXYItemLabelGenerator();
        
        // When & Then: Null dataset should throw IllegalArgumentException
        try {
            labelGenerator.generateToolTip(null, 44, 44);
            fail("Expected IllegalArgumentException for null dataset");
        } catch (IllegalArgumentException e) {
            assertEquals("Should reject null dataset with appropriate message", 
                        "Null 'dataset' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGenerateToolTip_WithInvalidSeriesIndex_ThrowsArrayIndexOutOfBoundsException() throws Throwable {
        // Given: A label generator and dataset with limited series
        SymbolicXYItemLabelGenerator labelGenerator = new SymbolicXYItemLabelGenerator();
        DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(INVALID_SERIES_INDEX, INVALID_ITEM_INDEX);
        
        // When & Then: Invalid series index should throw ArrayIndexOutOfBoundsException
        try {
            labelGenerator.generateToolTip(dataset, INVALID_SERIES_INDEX, 2);
            fail("Expected ArrayIndexOutOfBoundsException for invalid series index");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue("Exception message should indicate index out of bounds", 
                      e.getMessage().contains("Index 1970 out of bounds"));
        }
    }

    @Test(timeout = 4000)
    public void testEquals_WithDifferentObjectType_ReturnsFalse() throws Throwable {
        // Given: A label generator and a different type of object
        SymbolicXYItemLabelGenerator labelGenerator = new SymbolicXYItemLabelGenerator();
        Object differentObject = new Object();
        
        // When: Comparing with different object type
        boolean isEqual = labelGenerator.equals(differentObject);
        
        // Then: Should return false
        assertFalse("Label generator should not equal different object type", isEqual);
    }

    @Test(timeout = 4000)
    public void testEquals_WithSameInstance_ReturnsTrue() throws Throwable {
        // Given: A label generator
        SymbolicXYItemLabelGenerator labelGenerator = new SymbolicXYItemLabelGenerator();
        
        // When: Comparing with itself
        boolean isEqual = labelGenerator.equals(labelGenerator);
        
        // Then: Should return true
        assertTrue("Label generator should equal itself", isEqual);
    }

    @Test(timeout = 4000)
    public void testHashCode_ExecutesWithoutException() throws Throwable {
        // Given: A label generator
        SymbolicXYItemLabelGenerator labelGenerator = new SymbolicXYItemLabelGenerator();
        
        // When: Getting hash code
        int hashCode = labelGenerator.hashCode();
        
        // Then: Should execute without throwing exception
        // Hash code value itself is not important, just that it doesn't throw
    }

    @Test(timeout = 4000)
    public void testGenerateLabel_WithEmptyTimeSeriesCollection_ReturnsNull() throws Throwable {
        // Given: A label generator and empty time series collection
        SymbolicXYItemLabelGenerator labelGenerator = new SymbolicXYItemLabelGenerator();
        TimeSeriesCollection<ChronoLocalDate> emptyCollection = new TimeSeriesCollection<>();
        
        // When: Generating label for non-existent data
        String label = labelGenerator.generateLabel(emptyCollection, -1034, -4019);
        
        // Then: Should return null for non-existent data
        assertNull("Label should be null for non-existent data", label);
    }

    @Test(timeout = 4000)
    public void testClone_CreatesEqualObject() throws Throwable {
        // Given: A label generator
        SymbolicXYItemLabelGenerator originalGenerator = new SymbolicXYItemLabelGenerator();
        
        // When: Cloning the generator
        Object clonedGenerator = originalGenerator.clone();
        
        // Then: Clone should be equal to original
        assertTrue("Cloned generator should equal original", 
                  originalGenerator.equals(clonedGenerator));
    }

    // Helper methods for creating test data
    
    private DynamicTimeSeriesCollection createDynamicTimeSeriesWithIncompleteData() {
        DynamicTimeSeriesCollection collection = new DynamicTimeSeriesCollection(6, 6);
        float[] incompleteData = new float[5]; // Array with default values (0.0)
        collection.appendData(incompleteData, 2, 0);
        return collection;
    }
    
    private DefaultHighLowDataset createValidHighLowDataset() {
        MockDate testDate = new MockDate(TEST_DATE_VALUE);
        SerialDate serialDate = SerialDate.createInstance(testDate);
        
        Date[] dates = new Date[3];
        dates[0] = testDate;
        
        double[] values = new double[1]; // Default value is 0.0
        
        return new DefaultHighLowDataset(serialDate, dates, values, values, values, values, values);
    }
}