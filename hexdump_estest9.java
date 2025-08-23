package org.apache.commons.io;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Contains tests for the {@link HexDump} class.
 * This test case was improved for understandability from an auto-generated test.
 */
public class HexDumpTest {

    /**
     * Tests that HexDump.dump() throws a NullPointerException when the input byte array is null.
     * <p>
     * This test corresponds to the original auto-generated test case named {@code test08}.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void dumpWithNullDataArrayShouldThrowNullPointerException() throws Exception {
        // Arrange: Prepare the arguments for the method call.
        // The data array is intentionally null to trigger the exception.
        final byte[] data = null;
        
        // The other parameters are required by the method signature but their values are
        // irrelevant for this test, as the method should fail before they are used.
        final long irrelevantOffset = 0L;
        final int irrelevantIndex = 0;
        final OutputStream dummyStream = new ByteArrayOutputStream();

        // Act: Call the dump method with the null data array.
        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
        HexDump.dump(data, irrelevantOffset, dummyStream, irrelevantIndex);
    }
}