package org.apache.commons.io.input;

import org.junit.Test;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import static org.junit.Assert.assertSame;

/**
 * Tests for the fluent interface of {@link QueueInputStream.Builder}.
 */
public class QueueInputStreamBuilderTest {

    @Test
    public void setBlockingQueueShouldReturnSameBuilderInstanceForFluentChaining() {
        // Arrange
        final QueueInputStream.Builder builder = new QueueInputStream.Builder();
        final BlockingQueue<Integer> blockingQueue = new LinkedBlockingDeque<>();

        // Act
        final QueueInputStream.Builder returnedBuilder = builder.setBlockingQueue(blockingQueue);

        // Assert
        assertSame("The method should return the same instance to support a fluent API.",
                builder, returnedBuilder);
    }
}