package com.itextpdf.text.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Tests for the {@link GetBufferedRandomAccessSource} class.
 */
class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that when the underlying source is not open, any attempt to read from it
     * via GetBufferedRandomAccessSource propagates the resulting IOException.
     */
    @Test
    void get_whenUnderlyingSourceIsUnopened_throwsIOException() {
        // Arrange: Create a source that simulates an unopened state by passing a null FileChannel.
        // This is a dependency of the class under test.
        RandomAccessSource unopenedSource = new MappedChannelRandomAccessSource(null, 0L, 0L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(unopenedSource);

        // Act & Assert: Verify that calling get() throws the expected IOException.
        IOException thrownException = assertThrows(
            IOException.class,
            () -> bufferedSource.get(0L),
            "An IOException should be thrown when the underlying source is not open."
        );

        // Further Assert: Check the exception message to confirm it originates from the underlying source.
        assertEquals("RandomAccessSource not opened", thrownException.getMessage());
    }
}