package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.BigInteger;
import javax.swing.JLayeredPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.util.MockDate;
import org.jfree.chart.date.SerialDate;
import org.jfree.chart.date.SpreadsheetDate;
import org.jfree.data.general.DefaultKeyedValueDataset;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.xy.OHLCDataItem;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class DefaultKeyedValueDatasetTest extends DefaultKeyedValueDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testUpdateValueWithValidKey() throws Throwable {
        SerialDate date = SerialDate.createInstance(1670);
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(date, 2958465);
        dataset.updateValue(0);
        assertTrue(dataset.getNotify());
    }

    @Test(timeout = 4000)
    public void testGetValueWithOHLCDataItem() throws Throwable {
        MockDate mockDate = new MockDate(0, 1, 0);
        OHLCDataItem dataItem = new OHLCDataItem(mockDate, -1.0, -1.0, 0.0, 0.0, 0.0);
        BigInteger bigInteger = new BigInteger(new byte[]{0, 0, 0, -53, 0});
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(dataItem, bigInteger);
        assertSame(bigInteger, dataset.getValue());
    }

    @Test(timeout = 4000)
    public void testGetValueWithDifferentByteArray() throws Throwable {
        MockDate mockDate = new MockDate(0, 1, 0);
        OHLCDataItem dataItem = new OHLCDataItem(mockDate, -1.0, -1.0, 0.0, 0.0, 0.0);
        BigInteger bigInteger = new BigInteger(new byte[]{0, 0, 0, 0, 13});
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(dataItem, bigInteger);
        assertSame(bigInteger, dataset.getValue());
    }

    @Test(timeout = 4000)
    public void testGetValueWithSimpleHistogramBin() throws Throwable {
        SimpleHistogramBin bin = new SimpleHistogramBin(-1201.1136675752093, 1.0, false, false);
        BigInteger bigInteger = new BigInteger(new byte[]{0, 0, -93});
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(bin, bigInteger);
        assertEquals((byte) (-93), dataset.getValue().byteValue());
    }

    @Test(timeout = 4000)
    public void testSetValueWithNullKey() throws Throwable {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        Integer value = JLayeredPane.PALETTE_LAYER;
        try {
            dataset.setValue(null, value);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullKey() throws Throwable {
        try {
            new DefaultKeyedValueDataset(null, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetValueWithEmptyDataset() throws Throwable {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        assertNull(dataset.getValue());
    }

    @Test(timeout = 4000)
    public void testGetKeyWithEmptyDataset() throws Throwable {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        assertNull(dataset.getKey());
    }

    @Test(timeout = 4000)
    public void testGetKeyWithValidData() throws Throwable {
        SerialDate date = SerialDate.createInstance(1670);
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(date, 2958465);
        assertSame(date, dataset.getKey());
    }

    @Test(timeout = 4000)
    public void testHashCodeWithEmptyDataset() throws Throwable {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        dataset.hashCode();
    }

    @Test(timeout = 4000)
    public void testEqualsWithIdenticalDatasets() throws Throwable {
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset(dataset1);
        assertTrue(dataset2.equals(dataset1));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentValues() throws Throwable {
        SpreadsheetDate date = new SpreadsheetDate(379);
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset(date, 2);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset(date, 3);
        assertFalse(dataset1.equals(dataset2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentTypes() throws Throwable {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        BigInteger bigInteger = new BigInteger(new byte[]{0, 0, 0});
        assertFalse(dataset.equals(bigInteger));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSelf() throws Throwable {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        assertTrue(dataset.equals(dataset));
    }

    @Test(timeout = 4000)
    public void testUpdateValueWithNull() throws Throwable {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        try {
            dataset.updateValue(null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.jfree.data.general.DefaultKeyedValueDataset", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpdateValueWithNullKey() throws Throwable {
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset(dataset1);
        Integer value = JLayeredPane.DRAG_LAYER;
        try {
            dataset2.updateValue(value);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloneAndEquals() throws Throwable {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        Object clonedDataset = dataset.clone();
        assertNotSame(clonedDataset, dataset);
        assertTrue(dataset.equals(clonedDataset));
    }

    @Test(timeout = 4000)
    public void testSetValueAndNotify() throws Throwable {
        MockDate mockDate = new MockDate(-2363, -1655, 1600);
        OHLCDataItem dataItem = new OHLCDataItem(mockDate, 1600, -1655, 1.0, 1600, -1655);
        BigInteger bigInteger = BigInteger.TEN;
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(dataItem, bigInteger);
        dataset.setValue(dataItem, bigInteger);
        assertTrue(dataset.getNotify());
    }
}