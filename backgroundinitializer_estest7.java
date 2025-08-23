package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import java.util.concurrent.ExecutorService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link BackgroundInitializer}.
 * This test class focuses on specific scenarios and edge cases.
 */
public class BackgroundInitializer_ESTestTest7 extends BackgroundInitializer_ESTest_scaffolding {

    /**
     * Verifies that setExternalExecutor() throws an IllegalStateException
     * if it is called after the initializer has already been started.
     * The executor service cannot be changed once initialization is in progress.
     */
    @Test(timeout = 4000)
    public void setExternalExecutorShouldThrowIllegalStateExceptionWhenCalledAfterStart() {
        // Arrange: Create a background initializer and start it.
        final BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();
        initializer.start();

        // Act & Assert: Attempting to set an executor after starting should fail.
        // We use assertThrows to verify that the expected exception is thrown.
        final IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> initializer.setExternalExecutor(null)
        );

        // Verify the exception has the expected message.
        assertEquals("Cannot set ExecutorService after start()!", exception.getMessage());
    }
}