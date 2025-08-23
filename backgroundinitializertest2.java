package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BackgroundInitializer} focusing on scenarios involving its builder.
 *
 * Note: The original test class name "BackgroundInitializerTestTest2" is unconventional.
 * A name like "BackgroundInitializerBuilderTest" might be more descriptive.
 */
public class BackgroundInitializerTestTest2 extends AbstractLangTest {

    @Test
    void getShouldThrowExceptionForFailingInitializerFromBuilder() {
        // Arrange: Create an initializer using the builder, with a task designed to fail.
        final String expectedExceptionMessage = "test";
        final BackgroundInitializer<Object> initializer = BackgroundInitializer.builder()
            .setInitializer(() -> {
                throw new IllegalStateException(expectedExceptionMessage);
            })
            .get();

        // Assert: Verify the initial state of the initializer before it's started.
        // An initializer created by the builder should not be started or initialized yet.
        assertFalse(initializer.isStarted(), "Initializer should not be in a started state before start() is called.");
        assertFalse(initializer.isInitialized(), "Initializer should not be initialized before start() is called.");
        assertNull(initializer.getExternalExecutor(), "No external executor was provided, so it should be null.");
        assertThrows(IllegalStateException.class, initializer::getFuture,
            "Calling getFuture() before start() is not allowed and should throw.");

        // Act: Start the background initialization process.
        initializer.start();

        // Assert: Check that calling get() after starting propagates the failure.
        final IllegalStateException thrown = assertThrows(IllegalStateException.class, initializer::get,
            "get() should propagate the exception from the failed initializer task.");
        assertEquals(expectedExceptionMessage, thrown.getMessage(), "The exception message should match the one thrown by the initializer.");
    }
}