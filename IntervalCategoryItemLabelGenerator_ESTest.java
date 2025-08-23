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
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class IntervalCategoryItemLabelGenerator_ESTest extends IntervalCategoryItemLabelGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionWhenFormattingNonNumber() throws Throwable {
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator();
        DefaultStatisticalCategoryDataset<ThaiBuddhistEra, ThaiBuddhistEra> dataset = new DefaultStatisticalCategoryDataset<>();
        ThaiBuddhistEra era = ThaiBuddhistEra.BE;
        Integer layerValue = JLayeredPane.DRAG_LAYER;

        // Add non-numeric data to the dataset
        dataset.add((Number) layerValue, (Number) layerValue, ThaiBuddhistEra.BEFORE_BE, ThaiBuddhistEra.BEFORE_BE);
        dataset.add((double) 0, (double) 0, era, era);

        // Expect IllegalArgumentException due to non-numeric data
        try {
            labelGenerator.createItemArray(dataset, 0, 1);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.text.DecimalFormat", e);
        }
    }

    @Test(timeout = 4000)
    public void testIndexOutOfBoundsExceptionForInvalidIndex() throws Throwable {
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator();
        DefaultStatisticalCategoryDataset<ThaiBuddhistEra, ThaiBuddhistEra> dataset = new DefaultStatisticalCategoryDataset<>();
        ThaiBuddhistEra era = ThaiBuddhistEra.BE;
        Integer layerValue = JLayeredPane.DRAG_LAYER;

        // Add data to the dataset
        dataset.add((Number) layerValue, (Number) layerValue, era, era);

        // Expect IndexOutOfBoundsException due to invalid index
        try {
            labelGenerator.createItemArray(dataset, 0, 1);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionForNullDataset() throws Throwable {
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator();

        // Expect NullPointerException due to null dataset
        try {
            labelGenerator.createItemArray(null, 0, 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.labels.IntervalCategoryItemLabelGenerator", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionForOutOfBoundsRow() throws Throwable {
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator();
        double[][] data = new double[0][3];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);

        // Expect IllegalArgumentException due to out-of-bounds row index
        try {
            labelGenerator.createItemArray(dataset, 2, 2);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.data.category.DefaultIntervalCategoryDataset", e);
        }
    }

    @Test(timeout = 4000)
    public void testArrayIndexOutOfBoundsExceptionForEmptyData() throws Throwable {
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator();
        double[][] data = new double[7][0];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);

        // Expect ArrayIndexOutOfBoundsException due to empty data
        try {
            labelGenerator.createItemArray(dataset, 0, 0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.jfree.data.category.DefaultIntervalCategoryDataset", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionForNullNumberFormatter() throws Throwable {
        try {
            new IntervalCategoryItemLabelGenerator("{2}", (NumberFormat) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionForNullDateFormatter() throws Throwable {
        try {
            new IntervalCategoryItemLabelGenerator("{2}", (DateFormat) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArrayWithValidData() throws Throwable {
        double[][] data = new double[1][3];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);
        DateFormat dateFormat = MockDateFormat.getDateTimeInstance();
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator("{2}", dateFormat);

        // Create item array with valid data
        Object[] itemArray = labelGenerator.createItemArray(dataset, 0, 0);
        assertEquals(5, itemArray.length);
    }

    @Test(timeout = 4000)
    public void testCreateItemArrayWithStatisticalDataset() throws Throwable {
        DefaultStatisticalCategoryDataset<ThaiBuddhistEra, ThaiBuddhistEra> dataset = new DefaultStatisticalCategoryDataset<>();
        ThaiBuddhistEra era = ThaiBuddhistEra.BEFORE_BE;
        dataset.add(-2270.359128073, -2270.359128073, era, era);
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator("({0}, {1}) = {3} - {4}", dateFormat);

        // Create item array with statistical dataset
        Object[] itemArray = labelGenerator.createItemArray(dataset, 0, 0);
        assertEquals(5, itemArray.length);
    }

    @Test(timeout = 4000)
    public void testCreateItemArrayWithIntervalDataset() throws Throwable {
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator();
        double[][] data = new double[8][3];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);

        // Create item array with interval dataset
        Object[] itemArray = labelGenerator.createItemArray(dataset, 2, 2);
        assertEquals(5, itemArray.length);
    }

    @Test(timeout = 4000)
    public void testCreateItemArrayWithCustomNumberFormat() throws Throwable {
        DecimalFormat decimalFormat = new DecimalFormat("qa] ~&9");
        IntervalCategoryItemLabelGenerator labelGenerator = new IntervalCategoryItemLabelGenerator("O`zwofaJ", decimalFormat);
        double[][] data = new double[3][0];
        double[] values = new double[5];
        data[0] = values;
        data[1] = values;
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);

        // Create item array with custom number format
        Object[] itemArray = labelGenerator.createItemArray(dataset, 1, 0);
        assertEquals(5, itemArray.length);
    }
}