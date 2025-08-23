package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertSame;

import java.time.Duration;

/**
 * Tests for {@link QueueInputStream.Builder}.
 */
public class QueueInputStreamTest {

    /**
     * Tests that the {@link QueueInputStream.Builder#setTimeout(Duration)} method
     * returns the same builder instance, allowing for a fluent API (method chaining).
     */
    @Test
    public void testSetTimeoutShouldReturnSameBuilderInstanceForChaining() {
        // Arrange
        final QueueInputStream.Builder builder = QueueInputStream.builder();
        final Duration timeout = Duration.ofHours(1);

        // Act
        final QueueInputStream.Builder returnedBuilder = builder.setTimeout(timeout);

        // Assert
        assertSame("The builder returned by setTimeout should be the same instance to support a fluent API.",
                builder, returnedBuilder);
    }
}