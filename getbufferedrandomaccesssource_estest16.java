package com.itextpdf.text.io;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// Note: The original class name and scaffolding are kept for context.
public class GetBufferedRandomAccessSource_ESTestTest16 extends GetBufferedRandomAccessSource_ESTest_scaffolding {

    /**
     * Verifies that attempting to read from a source after it has been closed
     * throws an IllegalStateException.
     */
    @Test(timeout = 4000)
    public void get_onClosedSource_throwsIllegalStateException() throws IOException {
        // Arrange: Create a source and wrap it with the buffered source.
        byte[] sourceData = new byte[16];
        ArrayRandomAccessSource underlyingSource = new ArrayRandomAccessSource(sourceData);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(underlyingSource);

        // Close the source before attempting to use it.
        bufferedSource.close();

        // Act & Assert: Expect an IllegalStateException when trying to read.
        try {
            bufferedSource.get(0L);
            fail("Expected an IllegalStateException because the source is closed.");
        } catch (IllegalStateException e) {
            // Verify that the exception has the expected message.
            assertEquals("Already closed", e.getMessage());
        }
    }
}