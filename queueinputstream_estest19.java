package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertSame;

import java.time.Duration;

/**
 * Tests for the fluent API of {@link QueueInputStream.Builder}.
 */
public class QueueInputStreamBuilderTest {

    /**
     * Tests that the setTimeout() method returns the same builder instance,
     * allowing for method chaining (a fluent API).
     */
    @Test
    public void setTimeoutShouldReturnSameBuilderInstanceForChaining() {
        // Arrange
        final QueueInputStream.Builder builder = QueueInputStream.builder();
        final Duration timeout = Duration.ofSeconds(5);

        // Act
        final QueueInputStream.Builder returnedBuilder = builder.setTimeout(timeout);

        // Assert
        assertSame("The builder's setTimeout() method should return the same instance to support a fluent API.",
                   builder, returnedBuilder);
    }
}