package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.chrono.ThaiBuddhistEra;
import javax.swing.JLayeredPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.text.MockDateFormat;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class IntervalCategoryItemLabelGenerator_ESTest {

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void createItemArray_WithNullValue_ThrowsIllegalArgumentException() throws Throwable {
        // Setup: Create dataset with valid and null values
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();
        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();
        
        // Add valid data point at (BEFORE_BE, BEFORE_BE)
        ThaiBuddhistEra beforeBE = ThaiBuddhistEra.BEFORE_BE;
        Integer dragLayer = JLayeredPane.DRAG_LAYER;
        dataset.add(dragLayer, dragLayer, beforeBE, beforeBE);
        
        // Add another data point at (BE, BE)
        ThaiBuddhistEra be = ThaiBuddhistEra.BE;
        dataset.add(0.0, 0.0, be, be);
        
        // Attempt to generate label for null value at (row0, column1)
        // This cell doesn't exist in the dataset and will be null
        generator.createItemArray(dataset, 0, 1);
    }

    @Test(timeout = 4000, expected = IndexOutOfBoundsException.class)
    public void createItemArray_WithInvalidColumnIndex_ThrowsIndexOutOfBoundsException() throws Throwable {
        // Setup: Create dataset with single data point
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();
        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();
        
        ThaiBuddhistEra be = ThaiBuddhistEra.BE;
        Integer dragLayer = JLayeredPane.DRAG_LAYER;
        dataset.add(dragLayer, dragLayer, be, be);
        
        // Attempt to access non-existent column (index 1)
        generator.createItemArray(dataset, 0, 1);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void createItemArray_WithNullDataset_ThrowsNullPointerException() {
        // Attempt to generate label with null dataset
        new IntervalCategoryItemLabelGenerator().createItemArray(null, 0, 0);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void createItemArray_WithInvalidRowIndex_ThrowsIllegalArgumentException() throws Throwable {
        // Setup: Create empty dataset (0 rows, 3 columns)
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();
        double[][] emptyData = new double[0][3];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(emptyData, emptyData);
        
        // Attempt to access row index 2 in empty dataset
        generator.createItemArray(dataset, 2, 2);
    }

    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void createItemArray_WithEmptyColumns_ThrowsArrayIndexOutOfBoundsException() throws Throwable {
        // Setup: Create dataset with rows but no columns (7 rows, 0 columns)
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();
        double[][] rowData = new double[7][0];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(rowData, rowData);
        
        // Attempt to access column index 0 in columnless dataset
        generator.createItemArray(dataset, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_WithNullNumberFormat_ThrowsIllegalArgumentException() {
        // Attempt to create generator with null formatter
        new IntervalCategoryItemLabelGenerator("{2}", (NumberFormat) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_WithNullDateFormat_ThrowsIllegalArgumentException() {
        // Attempt to create generator with null formatter
        new IntervalCategoryItemLabelGenerator("{2}", (DateFormat) null);
    }

    @Test
    public void createItemArray_WithDateFormat_CreatesValidItemArray() throws Throwable {
        // Setup: Create valid interval dataset
        double[][] data = {{1.0, 2.0, 3.0}};
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);
        
        // Create generator with date formatting
        DateFormat dateFormat = MockDateFormat.getDateTimeInstance();
        IntervalCategoryItemLabelGenerator generator = 
            new IntervalCategoryItemLabelGenerator("{2}", dateFormat);
        
        // Verify item array creation
        Object[] result = generator.createItemArray(dataset, 0, 0);
        assertEquals("Item array should contain 5 elements", 5, result.length);
    }

    @Test
    public void createItemArray_WithCustomFormatAndDateFormat_CreatesValidItemArray() throws Throwable {
        // Setup: Create statistical dataset
        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();
        ThaiBuddhistEra era = ThaiBuddhistEra.BEFORE_BE;
        dataset.add(-2270.359, -2270.359, era, era);
        
        // Create generator with custom format and date formatting
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        IntervalCategoryItemLabelGenerator generator = 
            new IntervalCategoryItemLabelGenerator("({0}, {1}) = {3} - {4}", dateFormat);
        
        // Verify item array creation
        Object[] result = generator.createItemArray(dataset, 0, 0);
        assertEquals("Item array should contain 5 elements", 5, result.length);
    }

    @Test
    public void createItemArray_WithIntervalDataset_CreatesValidItemArray() throws Throwable {
        // Setup: Create valid interval dataset
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();
        double[][] data = new double[8][3];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);
        
        // Verify item array creation
        Object[] result = generator.createItemArray(dataset, 2, 2);
        assertEquals("Item array should contain 5 elements", 5, result.length);
    }

    @Test
    public void createItemArray_WithCustomNumberFormat_CreatesValidItemArray() throws Throwable {
        // Setup: Create custom number formatter
        DecimalFormat decimalFormat = new DecimalFormat("qa] ~&9");
        IntervalCategoryItemLabelGenerator generator = 
            new IntervalCategoryItemLabelGenerator("O`zwofaJ", decimalFormat);
        
        // Setup: Create dataset with valid structure
        double[][] rowData = new double[3][];
        double[] columnData = new double[5];
        rowData[0] = columnData;
        rowData[1] = columnData;
        rowData[2] = new double[0];
        DefaultIntervalCategoryDataset dataset = 
            new DefaultIntervalCategoryDataset(rowData, rowData);
        
        // Verify item array creation
        Object[] result = generator.createItemArray(dataset, 1, 0);
        assertEquals("Item array should contain 5 elements", 5, result.length);
    }
}