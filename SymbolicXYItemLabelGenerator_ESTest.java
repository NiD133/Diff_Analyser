package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class SymbolicXYItemLabelGenerator_ESTest extends SymbolicXYItemLabelGenerator_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;

    @Test(timeout = TIMEOUT)
    public void testGenerateToolTipWithNullDatasetThrowsException() {
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        try {
            generator.generateToolTip(null, 44, 44);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGenerateToolTipWithDynamicTimeSeriesCollectionThrowsNullPointerException() {
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(6, 6);
        float[] data = new float[5];
        dataset.appendData(data, 2, 0);
        try {
            generator.generateToolTip(dataset, 1, 2);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.data.time.DynamicTimeSeriesCollection", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGenerateToolTipWithOutOfBoundsIndexThrowsIndexOutOfBoundsException() {
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        DefaultTableXYDataset<Date> dataset = new DefaultTableXYDataset<>();
        try {
            generator.generateToolTip(dataset, 1970, 1970);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGenerateToolTipWithDefaultHighLowDataset() {
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        MockDate mockDate = new MockDate(102L);
        SerialDate serialDate = SerialDate.createInstance((Date) mockDate);
        Date[] dateArray = new Date[]{mockDate, null, null};
        double[] doubleArray = new double[1];
        DefaultHighLowDataset dataset = new DefaultHighLowDataset(serialDate, dateArray, doubleArray, doubleArray, doubleArray, doubleArray, doubleArray);
        String tooltip = generator.generateToolTip(dataset, 0, 0);
        assertEquals("X: 102.0, Y: 0.0", tooltip);
    }

    @Test(timeout = TIMEOUT)
    public void testGenerateToolTipWithOutOfBoundsIndexThrowsArrayIndexOutOfBoundsException() {
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(1970, 1970);
        try {
            generator.generateToolTip(dataset, 1970, 2);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.jfree.data.time.DynamicTimeSeriesCollection", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testEqualsWithDifferentObjectReturnsFalse() {
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        Object otherObject = new Object();
        assertFalse(generator.equals(otherObject));
    }

    @Test(timeout = TIMEOUT)
    public void testEqualsWithSameObjectReturnsTrue() {
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        assertTrue(generator.equals(generator));
    }

    @Test(timeout = TIMEOUT)
    public void testHashCode() {
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        generator.hashCode(); // Just to ensure it doesn't throw any exceptions
    }

    @Test(timeout = TIMEOUT)
    public void testGenerateLabelWithInvalidIndicesReturnsNull() {
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        TimeSeriesCollection<Date> dataset = new TimeSeriesCollection<>();
        String label = generator.generateLabel(dataset, -1034, -4019);
        assertNull(label);
    }

    @Test(timeout = TIMEOUT)
    public void testCloneAndEquals() throws CloneNotSupportedException {
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        Object clonedObject = generator.clone();
        assertTrue(generator.equals(clonedObject));
    }
}