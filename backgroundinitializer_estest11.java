package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link BackgroundInitializer}.
 */
public class BackgroundInitializerTest {

    /**
     * Tests that calling getFuture() before start() throws an IllegalStateException.
     */
    @Test
    public void getFutureShouldThrowIllegalStateExceptionWhenNotStarted() {
        // Arrange: Create a BackgroundInitializer but do not start it.
        final ExecutorService executor = new ForkJoinPool();
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>(executor);

        // Act & Assert: Verify that calling getFuture() throws the expected exception.
        final IllegalStateException thrown = assertThrows(
            "getFuture() should throw an exception if start() has not been called.",
            IllegalStateException.class,
            initializer::getFuture
        );

        // Further assert on the exception message to ensure it's the correct one.
        assertEquals("start() must be called first!", thrown.getMessage());
    }
}