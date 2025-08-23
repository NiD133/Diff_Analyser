package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import java.util.concurrent.ExecutorService;
import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link BackgroundInitializer} class, focusing on its
 * executor management.
 */
public class BackgroundInitializerTest {

    /**
     * Verifies that getExternalExecutor() returns null when the initializer is
     * created with the default constructor (i.e., without providing an executor).
     */
    @Test
    public void getExternalExecutorShouldReturnNullWhenNotProvided() {
        // Arrange: Create a BackgroundInitializer using its default constructor.
        // The generic type is not relevant for this test, so Object is a suitable choice.
        BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();

        // Act: Retrieve the external executor service from the initializer.
        final ExecutorService externalExecutor = initializer.getExternalExecutor();

        // Assert: The external executor should be null, as none was configured.
        assertNull("The external executor should be null by default", externalExecutor);
    }
}