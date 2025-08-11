package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.OutputStream;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;
import org.apache.commons.io.output.DeferredFileOutputStream;
import org.apache.commons.io.output.ThresholdingOutputStream;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ThresholdingOutputStream_ESTest extends ThresholdingOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void isThresholdExceededReturnsFalseWhenBelowThreshold() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(76);
        assertFalse("Threshold should not be exceeded initially", outputStream.isThresholdExceeded());
        assertEquals("Threshold value should match", 76, outputStream.getThreshold());
    }

    @Test(timeout = 4000)
    public void writeWithNullArrayAndZeroLengthSucceeds() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(0);
        outputStream.write((byte[]) null, 0, 0);
        assertEquals("Byte count should remain 0", 0L, outputStream.getByteCount());
        assertEquals("Threshold should be 0", 0, outputStream.getThreshold());
    }

    @Test(timeout = 4000)
    public void thresholdReachedCanBeCalledWithoutException() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(661);
        outputStream.thresholdReached();
        assertEquals("Threshold should remain unchanged", 661, outputStream.getThreshold());
    }

    @Test(timeout = 4000)
    public void getThresholdReturnsCorrectValue() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(76);
        assertEquals("Should return configured threshold", 76, outputStream.getThreshold());
    }

    @Test(timeout = 4000)
    public void getStreamSucceedsWithNegativeThreshold() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(-2596);
        outputStream.getStream();
        assertEquals("Negative threshold should be normalized to 0", 0, outputStream.getThreshold());
    }

    @Test(timeout = 4000)
    public void getOutputStreamSucceedsWithZeroThreshold() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(0);
        outputStream.getOutputStream();
        assertEquals("Threshold should be 0", 0, outputStream.getThreshold());
    }

    @Test(timeout = 4000)
    public void getByteCountReflectsWrittenBytes() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(-2596);
        byte[] data = new byte[1];
        outputStream.write(data);
        assertEquals("Byte count should match written bytes", 1L, outputStream.getByteCount());
    }

    @Test(timeout = 4000)
    public void writeWithNullArrayThrowsNullPointerException() {
        DeferredFileOutputStream outputStream = new DeferredFileOutputStream.Builder().get();
        try {
            outputStream.write((byte[]) null, 76, 76);
            fail("Expected NullPointerException when writing null array");
        } catch (NullPointerException e) {
            // Expected due to null array parameter
        }
    }

    @Test(timeout = 4000)
    public void writeWithNegativeOffsetThrowsIndexOutOfBoundsException() {
        DeferredFileOutputStream outputStream = new DeferredFileOutputStream.Builder().get();
        try {
            outputStream.write((byte[]) null, -122, -122);
            fail("Expected IndexOutOfBoundsException for negative offset");
        } catch (IndexOutOfBoundsException e) {
            // Expected due to invalid offset/length
        }
    }

    @Test(timeout = 4000)
    public void writeWithInvalidPrefixThrowsIllegalArgumentException() {
        DeferredFileOutputStream outputStream = new DeferredFileOutputStream.Builder()
            .setPrefix("/-f5")
            .get();
        try {
            outputStream.write((byte[]) null, -3053, 76);
            fail("Expected IllegalArgumentException for invalid prefix");
        } catch (IllegalArgumentException e) {
            // Expected due to invalid file prefix
        }
    }

    @Test(timeout = 4000)
    public void writeNullArrayThrowsNullPointerException() {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(379);
        try {
            outputStream.write((byte[]) null);
            fail("Expected NullPointerException for null array write");
        } catch (NullPointerException e) {
            // Expected due to null array
        }
    }

    @Test(timeout = 4000)
    public void writeSingleByteThrowsNullPointerException() {
        DeferredFileOutputStream outputStream = new DeferredFileOutputStream.Builder().get();
        try {
            outputStream.write(0);
            fail("Expected NullPointerException for underlying stream issue");
        } catch (NullPointerException e) {
            // Expected due to uninitialized stream
        }
    }

    @Test(timeout = 4000)
    public void thresholdReachedThrowsNullPointerException() {
        DeferredFileOutputStream outputStream = new DeferredFileOutputStream.Builder().get();
        try {
            outputStream.thresholdReached();
            fail("Expected NullPointerException in thresholdReached");
        } catch (NullPointerException e) {
            // Expected due to uninitialized stream
        }
    }

    @Test(timeout = 4000)
    public void checkThresholdThrowsNullPointerException() {
        DeferredFileOutputStream outputStream = new DeferredFileOutputStream.Builder().get();
        try {
            outputStream.checkThreshold(1);
            fail("Expected NullPointerException in checkThreshold");
        } catch (NullPointerException e) {
            // Expected due to uninitialized stream
        }
    }

    @Test(timeout = 4000)
    public void writeWithInvalidOffsetsStillUpdatesByteCount() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(-2596);
        byte[] data = new byte[1];
        outputStream.write(data);
        outputStream.write(data, -1473, 1950);
        assertEquals("Byte count should reflect all written bytes", 1951L, outputStream.getByteCount());
    }

    @Test(timeout = 4000)
    public void flushCompletesWithoutException() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(-2596);
        outputStream.flush();
        assertEquals("Threshold should be normalized to 0", 0, outputStream.getThreshold());
    }

    @Test(timeout = 4000)
    public void isThresholdExceededReturnsTrueAfterWrite() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(-2596);
        byte[] data = new byte[1];
        outputStream.write(data);
        assertTrue("Threshold should be exceeded after write", outputStream.isThresholdExceeded());
        assertEquals("Byte count should be 1", 1L, outputStream.getByteCount());
    }

    @Test(timeout = 4000)
    public void isThresholdExceededReturnsFalseInitially() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(-2596);
        assertFalse("Threshold should not be exceeded initially", outputStream.isThresholdExceeded());
        assertEquals("Threshold should be normalized to 0", 0, outputStream.getThreshold());
    }

    @Test(timeout = 4000)
    public void constructorAcceptsThresholdAndCallbacks() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(
            1, 
            (IOConsumer<ThresholdingOutputStream>) null, 
            (IOFunction<ThresholdingOutputStream, OutputStream>) null
        );
        assertEquals("Should store threshold value", 1, outputStream.getThreshold());
    }

    @Test(timeout = 4000)
    public void setByteCountUpdatesInternalCounter() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(129);
        outputStream.setByteCount((byte) 1);
        assertEquals("Byte count should be updated", 1L, outputStream.getByteCount());
    }

    @Test(timeout = 4000)
    public void getByteCountReturnsZeroInitially() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(-2596);
        assertEquals("Initial byte count should be 0", 0L, outputStream.getByteCount());
        assertEquals("Threshold should be normalized to 0", 0, outputStream.getThreshold());
    }

    @Test(timeout = 4000)
    public void getThresholdNormalizesNegativeValuesToZero() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(-2596);
        assertEquals("Negative threshold should be normalized to 0", 0, outputStream.getThreshold());
    }

    @Test(timeout = 4000)
    public void resetByteCountClearsCounter() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(-2596);
        outputStream.resetByteCount();
        assertEquals("Byte count should be reset to 0", 0L, outputStream.getByteCount());
        assertEquals("Threshold should be normalized to 0", 0, outputStream.getThreshold());
    }

    @Test(timeout = 4000)
    public void writeSingleByteIncrementsCount() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(-2596);
        outputStream.write(345);
        assertEquals("Byte count should increment by 1", 1L, outputStream.getByteCount());
    }

    @Test(timeout = 4000)
    public void checkThresholdAfterWriteDoesNotResetCount() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(-2596);
        byte[] data = new byte[1];
        outputStream.write(data);
        outputStream.checkThreshold(0);
        assertEquals("Byte count should remain unchanged", 1L, outputStream.getByteCount());
    }

    @Test(timeout = 4000)
    public void writeWithNegativeLengthStillUpdatesCount() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(76);
        byte[] data = new byte[3];
        outputStream.write(data, -2302, -58);
        assertEquals("Byte count should reflect negative length write", -58L, outputStream.getByteCount());
    }

    @Test(timeout = 4000)
    public void closeCompletesWithoutException() throws Throwable {
        ThresholdingOutputStream outputStream = new ThresholdingOutputStream(76);
        outputStream.close();
        assertEquals("Threshold should remain unchanged", 76, outputStream.getThreshold());
    }
}