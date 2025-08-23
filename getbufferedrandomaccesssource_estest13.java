package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.channels.FileChannel;

// This class is a placeholder for the original test's scaffolding.
// In a real-world scenario, you would use your project's actual test base class.
class GetBufferedRandomAccessSource_ESTest_scaffolding {}

public class GetBufferedRandomAccessSourceTest extends GetBufferedRandomAccessSource_ESTest_scaffolding {

    /**
     * Tests that the get() method correctly propagates an IOException thrown by the
     * underlying source. This can happen if the underlying source is closed or invalid.
     */
    @Test
    public void get_whenUnderlyingSourceIsInvalid_throwsIOException() {
        // Arrange: Create a source that will throw an exception.
        // We simulate an invalid or "unopened" source by passing a null FileChannel
        // to the MappedChannelRandomAccessSource constructor.
        RandomAccessSource invalidSource = new MappedChannelRandomAccessSource(null, 0L, 0L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(invalidSource);
        byte[] destinationBuffer = new byte[10];

        // Act & Assert
        try {
            bufferedSource.get(0L, destinationBuffer, 0, destinationBuffer.length);
            fail("Expected an IOException because the underlying source is not opened.");
        } catch (IOException e) {
            // Verify that the expected exception was caught and has the correct message.
            // This confirms the exception originated from the underlying MappedChannelRandomAccessSource
            // and was propagated correctly by the GetBufferedRandomAccessSource.
            assertEquals("RandomAccessSource not opened", e.getMessage());
        }
    }
}