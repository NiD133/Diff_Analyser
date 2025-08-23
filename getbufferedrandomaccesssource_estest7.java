package com.itextpdf.text.io;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.fail;

/**
 * Test suite for {@link GetBufferedRandomAccessSource}.
 * Note: The original test class name "GetBufferedRandomAccessSource_ESTestTest7"
 * suggests it was auto-generated. A more conventional name would be "GetBufferedRandomAccessSourceTest".
 */
public class GetBufferedRandomAccessSource_ESTestTest7 extends GetBufferedRandomAccessSource_ESTest_scaffolding {

    /**
     * Verifies that attempting to get the length of the source after it has been closed
     * results in a NullPointerException. This tests the resource-handling contract,
     * ensuring that the source is unusable after being closed.
     */
    @Test(timeout = 4000)
    public void length_afterClose_throwsNullPointerException() throws IOException {
        // Arrange: Create a buffered source wrapping a simple byte array source.
        byte[] sourceData = new byte[1];
        RandomAccessSource underlyingSource = new ArrayRandomAccessSource(sourceData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(underlyingSource);

        // Act: Close the source. This is expected to release its underlying resources.
        bufferedSource.close();

        // Assert: Attempting to access the length of the closed source should fail.
        try {
            bufferedSource.length();
            fail("Expected a NullPointerException to be thrown when calling length() on a closed source.");
        } catch (NullPointerException e) {
            // This is the expected behavior. The test passes.
            // The underlying ArrayRandomAccessSource nullifies its internal data on close(),
            // which causes this exception when length() is subsequently called.
        }
    }
}