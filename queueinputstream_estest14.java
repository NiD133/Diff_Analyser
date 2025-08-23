package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.Duration;
import org.junit.Test;

/**
 * Tests for {@link QueueInputStream.Builder}.
 */
public class QueueInputStreamBuilderTest {

    /**
     * Tests that {@link QueueInputStream.Builder#setTimeout(Duration)} throws
     * an {@link IllegalArgumentException} when given a negative duration.
     */
    @Test
    public void setTimeoutShouldThrowIllegalArgumentExceptionForNegativeDuration() {
        // Arrange: Create a builder and a negative timeout duration.
        final QueueInputStream.Builder builder = QueueInputStream.builder();
        final Duration negativeTimeout = Duration.ofMillis(-1);
        final String expectedErrorMessage = "timeout must not be negative";

        // Act & Assert: Attempt to set the negative timeout and verify the exception.
        try {
            builder.setTimeout(negativeTimeout);
            fail("Expected an IllegalArgumentException to be thrown for a negative timeout.");
        } catch (final IllegalArgumentException e) {
            // Verify that the caught exception has the expected error message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}