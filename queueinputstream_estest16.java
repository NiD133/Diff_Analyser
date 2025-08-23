package org.apache.commons.io.input;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Unit tests for {@link QueueInputStream}.
 */
public class QueueInputStreamTest {

    /**
     * Tests that calling {@link QueueInputStream#builder()} followed by {@code get()}
     * successfully creates a non-null QueueInputStream instance using default settings.
     */
    @Test
    public void builderShouldCreateInstanceWithDefaultSettings() {
        // Arrange: Create a builder with default configurations.
        final QueueInputStream.Builder builder = QueueInputStream.builder();

        // Act: Build the QueueInputStream instance.
        final QueueInputStream inputStream = builder.get();

        // Assert: Verify that the created instance is not null.
        assertNotNull("The stream created by the default builder should not be null.", inputStream);
    }
}