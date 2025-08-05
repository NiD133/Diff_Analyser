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
public class DefaultKeyedValueDataset_ESTest extends DefaultKeyedValueDataset_ESTest_scaffolding {

    // Tests for value update functionality
    @Test(timeout = 4000)
    public void updateValue_WithExistingKey_NotifyFlagRemainsTrue() throws Throwable {
        SerialDate key = SerialDate.createInstance(1670);
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, 2958465);
        dataset.updateValue(0);
        assertTrue("Notify flag should remain true after update", dataset.getNotify());
    }

    // Tests for value retrieval after construction
    @Test(timeout = 4000)
    public void getValue_AfterConstructionWithNegativeBigInteger_ReturnsSameObject() throws Throwable {
        OHLCDataItem key = createOHLCDataItem();
        BigInteger value = new BigInteger(new byte[]{0, 0, 0, (byte) -53, 0});
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, value);
        assertSame("Value should match constructed BigInteger", value, dataset.getValue());
    }

    @Test(timeout = 4000)
    public void getValue_AfterConstructionWithPositiveBigInteger_ReturnsSameObject() throws Throwable {
        OHLCDataItem key = createOHLCDataItem();
        BigInteger value = new BigInteger(new byte[]{0, 0, 0, 0, (byte) 13});
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, value);
        assertSame("Value should match constructed BigInteger", value, dataset.getValue());
    }

    @Test(timeout = 4000)
    public void getValue_AfterConstructionWithSpecificBigInteger_ReturnsCorrectByteValue() throws Throwable {
        SimpleHistogramBin key = new SimpleHistogramBin(-1201.1136, 1.0, false, false);
        BigInteger value = new BigInteger(new byte[]{0, 0, (byte) -93});
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, value);
        assertEquals("Byte value should match", (byte) -93, dataset.getValue().byteValue());
    }

    @Test(timeout = 4000)
    public void getValue_AfterConstructionWithZeroBigInteger_ReturnsZero() throws Throwable {
        OHLCDataItem key = createOHLCDataItem();
        BigInteger value = new BigInteger(new byte[]{0, 0, 0, 0, 0});
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, value);
        assertEquals("Short value should be zero", (short) 0, dataset.getValue().shortValue());
    }

    // Tests for exception handling
    @Test(timeout = 4000)
    public void setValue_WithNullKey_ThrowsIllegalArgumentException() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        try {
            dataset.setValue(null, JLayeredPane.PALETTE_LAYER);
            fail("Should throw IllegalArgumentException for null key");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void constructor_WithNullKey_ThrowsIllegalArgumentException() {
        try {
            new DefaultKeyedValueDataset(null, null);
            fail("Should throw IllegalArgumentException for null key");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void updateValue_WhenNoKeyExists_ThrowsIllegalArgumentException() {
        DefaultKeyedValueDataset original = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(original);
        try {
            dataset.updateValue(JLayeredPane.DRAG_LAYER);
            fail("Should throw IllegalArgumentException when key is null");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void updateValue_WithNullValue_ThrowsRuntimeException() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        try {
            dataset.updateValue(null);
            fail("Should throw RuntimeException for null value");
        } catch (RuntimeException e) {
            verifyException("org.jfree.data.general.DefaultKeyedValueDataset", e);
        }
    }

    // Tests for default state behavior
    @Test(timeout = 4000)
    public void getValue_DefaultConstructor_ReturnsNull() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        assertNull("Initial value should be null", dataset.getValue());
    }

    @Test(timeout = 4000)
    public void getKey_DefaultConstructor_ReturnsNull() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        assertNull("Initial key should be null", dataset.getKey());
    }

    @Test(timeout = 4000)
    public void getKey_AfterConstructionWithKey_ReturnsSameKey() {
        SerialDate key = SerialDate.createInstance(1670);
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, 2958465);
        assertSame("Key should match constructed key", key, dataset.getKey());
    }

    // Tests for hashCode behavior
    @Test(timeout = 4000)
    public void hashCode_WithDatasetConstructedFromAnother_ComputesSuccessfully() {
        DefaultKeyedValueDataset original = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(original);
        dataset.hashCode();  // Just verify no exception occurs
    }

    @Test(timeout = 4000)
    public void hashCode_DefaultConstructor_ComputesSuccessfully() {
        new DefaultKeyedValueDataset().hashCode();  // Just verify no exception occurs
    }

    // Tests for equality comparisons
    @Test(timeout = 4000)
    public void equals_WithEquivalentDataset_ReturnsTrue() {
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset(dataset1);
        assertTrue("Equivalent datasets should be equal", dataset1.equals(dataset2));
    }

    @Test(timeout = 4000)
    public void equals_WithDifferentValues_ReturnsFalse() {
        SpreadsheetDate key = new SpreadsheetDate(379);
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset(key, 2);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset(key, 3);
        assertFalse("Datasets with different values should not be equal", dataset1.equals(dataset2));
    }

    @Test(timeout = 4000)
    public void equals_WithDifferentKeys_ReturnsFalse() {
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset(createOHLCDataItem(), BigInteger.ZERO);
        assertFalse("Datasets with different keys should not be equal", dataset1.equals(dataset2));
    }

    @Test(timeout = 4000)
    public void equals_WithNonNullDatasetVsEmpty_ReturnsFalse() {
        DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset(BigInteger.TEN, BigInteger.TEN);
        DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset();
        assertFalse("Non-empty vs empty datasets should not be equal", dataset1.equals(dataset2));
    }

    @Test(timeout = 4000)
    public void equals_WithSameObject_ReturnsTrue() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        assertTrue("Object should equal itself", dataset.equals(dataset));
    }

    @Test(timeout = 4000)
    public void equals_WithClonedObject_ReturnsTrue() throws CloneNotSupportedException {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        Object clone = dataset.clone();
        assertTrue("Clone should equal original", dataset.equals(clone));
    }

    @Test(timeout = 4000)
    public void equals_WithNonDatasetObject_ReturnsFalse() {
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset();
        BigInteger other = new BigInteger(new byte[3]);
        assertFalse("Dataset should not equal non-dataset object", dataset.equals(other));
    }

    // Tests for setValue functionality
    @Test(timeout = 4000)
    public void setValue_WithValidKeyAndValue_DoesNotThrow() {
        MockDate date = new MockDate(-2363, -1655, 1600);
        OHLCDataItem key = new OHLCDataItem(date, 1600, -1655, 1.0, 1600, -1655);
        DefaultKeyedValueDataset dataset = new DefaultKeyedValueDataset(key, BigInteger.TEN);
        dataset.setValue(key, BigInteger.TEN);  // Verify no exception
        assertTrue("Notify flag should remain true", dataset.getNotify());
    }

    // Helper method for creating consistent test objects
    private OHLCDataItem createOHLCDataItem() {
        MockDate date = new MockDate(0, 1, 0);
        return new OHLCDataItem(date, -1.0, -1.0, 0.0, 0.0, 0.0);
    }
}