package org.apache.commons.io.input;

import org.apache.commons.io.output.QueueOutputStream;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link QueueInputStream} class.
 */
public class QueueInputStreamTest {

    /**
     * Tests that calling {@link QueueInputStream#newQueueOutputStream()}
     * successfully creates and returns a non-null instance of
     * {@link QueueOutputStream}.
     */
    @Test
    public void newQueueOutputStreamShouldReturnNonNullInstance() {
        // Arrange: Create a new QueueInputStream instance.
        final QueueInputStream inputStream = new QueueInputStream();

        // Act: Create a corresponding QueueOutputStream from the input stream.
        final QueueOutputStream outputStream = inputStream.newQueueOutputStream();

        // Assert: Verify that the created output stream is not null.
        assertNotNull("The new QueueOutputStream should not be null.", outputStream);
    }
}