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
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class IntervalCategoryToolTipGenerator_ESTest extends IntervalCategoryToolTipGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCreateItemArray_NullDataset_ShouldThrowNullPointerException() {
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        try {
            generator.createItemArray(null, 70, 70);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.labels.IntervalCategoryToolTipGenerator", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_InvalidIndex_ShouldThrowIndexOutOfBoundsException() {
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        DefaultBoxAndWhiskerCategoryDataset<ChronoLocalDate, ChronoLocalDate> dataset = new DefaultBoxAndWhiskerCategoryDataset<>();
        try {
            generator.createItemArray(dataset, -2580, -2580);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_EmptyDataset_ShouldThrowIllegalArgumentException() {
        double[][] data = new double[0][1];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        try {
            generator.createItemArray(dataset, 1696, 1696);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.data.category.DefaultIntervalCategoryDataset", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_OutOfBoundsIndex_ShouldThrowArrayIndexOutOfBoundsException() {
        double[][] data = new double[14][4];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        try {
            generator.createItemArray(dataset, 13, 13);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.jfree.data.category.DefaultIntervalCategoryDataset", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_NullNumberFormat_ShouldThrowIllegalArgumentException() {
        try {
            new IntervalCategoryToolTipGenerator("({0}, {1}) = {2}", (NumberFormat) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_NullDateFormat_ShouldThrowIllegalArgumentException() {
        try {
            new IntervalCategoryToolTipGenerator("({0}, {1}) = {2}", (DateFormat) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentFormatStrings_ShouldReturnFalse() {
        DateFormat dateFormat = MockDateFormat.getTimeInstance();
        IntervalCategoryToolTipGenerator generator1 = new IntervalCategoryToolTipGenerator("", dateFormat);
        IntervalCategoryToolTipGenerator generator2 = new IntervalCategoryToolTipGenerator("({0}, {1}) = {3} - {4}", dateFormat);
        assertFalse(generator1.equals(generator2));
    }

    @Test(timeout = 4000)
    public void testEquals_SameInstance_ShouldReturnTrue() {
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        assertTrue(generator.equals(generator));
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_ValidData_ShouldReturnCorrectArrayLength() {
        double[][] data = new double[8][4];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        Object[] itemArray = generator.createItemArray(dataset, 0, 0);
        assertEquals(5, itemArray.length);
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_WithMockDateFormat_ShouldReturnCorrectArrayLength() {
        MockSimpleDateFormat mockDateFormat = new MockSimpleDateFormat();
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator("org.jfree.chart.labels.IntervalCategoryToolTipGenerator", mockDateFormat);
        double[][] data = new double[5][4];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);
        Object[] itemArray = generator.createItemArray(dataset, 0, 0);
        assertEquals(5, itemArray.length);
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentObjectType_ShouldReturnFalse() {
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator("Te{[wtf@3LG|<`-", numberFormat);
        assertFalse(generator.equals("Te{[wtf@3LG|<`-"));
    }

    @Test(timeout = 4000)
    public void testCreateItemArray_WithDateFormat_ShouldReturnCorrectArrayLength() {
        String formatString = "t:RbWj+5v<m\\uAAUW";
        DateFormat dateFormat = DateFormat.getDateTimeInstance(0, 0);
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator(formatString, dateFormat);
        double[][] data = new double[7][4];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);
        Object[] itemArray = generator.createItemArray(dataset, 0, 3);
        assertEquals(5, itemArray.length);
    }

    @Test(timeout = 4000)
    public void testHashCode() {
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        generator.hashCode(); // Ensures no exception is thrown
    }
}