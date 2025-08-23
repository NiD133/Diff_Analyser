package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

/**
 * This test class focuses on the behavior of GetBufferedRandomAccessSource
 * when its underlying data source is closed.
 */
// The original test class extended an EvoSuite scaffolding class, which is removed
// here to focus on the core test logic and improve readability.
public class GetBufferedRandomAccessSource_ESTestTest10 {

    /**
     * Verifies that attempting to read from a GetBufferedRandomAccessSource
     * after its underlying source has been closed throws an IllegalStateException.
     */
    @Test(timeout = 4000)
    public void get_whenUnderlyingSourceIsClosed_throwsIllegalStateException() throws IOException {
        // Arrange: Create a buffered source that wraps an underlying source.
        // We use a simple ArrayRandomAccessSource as the base.
        byte[] emptyData = new byte[0];
        RandomAccessSource underlyingSource = new ArrayRandomAccessSource(emptyData);
        GetBufferedRandomAccessSource sourceUnderTest = new GetBufferedRandomAccessSource(underlyingSource);

        // Act: Close the underlying source directly.
        underlyingSource.close();

        // Assert: Any subsequent read attempt on the wrapper should fail.
        try {
            // The specific parameters for get() are not critical; any read attempt should fail.
            sourceUnderTest.get(0L, new byte[10], 0, 1);
            fail("Expected an IllegalStateException because the underlying source is closed.");
        } catch (IllegalStateException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("Already closed", e.getMessage());
        }
    }
}