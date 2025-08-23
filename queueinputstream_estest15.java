package org.apache.commons.io.input;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link QueueInputStream.Builder}.
 *
 * Note: This class name is simplified from the original auto-generated name
 * for clarity. A name like {@code QueueInputStreamBuilderTest} would be conventional.
 */
public class QueueInputStreamBuilderTest {

    /**
     * Tests that the {@link QueueInputStream.Builder#setTimeout(Duration)} method
     * returns the same builder instance, confirming support for fluent method chaining.
     */
    @Test
    public void setTimeoutShouldReturnSameBuilderInstanceForFluentChaining() {
        // Arrange
        final QueueInputStream.Builder builder = new QueueInputStream.Builder();

        // Act
        // Calling setTimeout should return the same instance to allow chaining.
        // Passing null is acceptable here; validation occurs when build() is called.
        final QueueInputStream.Builder resultBuilder = builder.setTimeout(null);

        // Assert
        assertSame("The builder should return itself to allow for a fluent API.", builder, resultBuilder);
    }
}