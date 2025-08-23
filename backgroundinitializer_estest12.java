package org.apache.commons.lang3.concurrent;

import org.apache.commons.lang3.concurrent.BackgroundInitializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class is focused on the {@link BackgroundInitializer} class.
 * Note: The original class name and inheritance from a scaffolding class,
 * common in auto-generated tests, have been preserved. A more conventional
 * name would be {@code BackgroundInitializerTest}.
 */
public class BackgroundInitializer_ESTestTest12 extends BackgroundInitializer_ESTest_scaffolding {

    /**
     * Tests that getTaskCount() returns the documented default value of 1
     * for a new BackgroundInitializer instance.
     */
    @Test
    public void getTaskCountShouldReturnOneByDefault() {
        // Arrange: Create a new BackgroundInitializer instance.
        // The generic type is not relevant for this test.
        BackgroundInitializer<Object> initializer = new BackgroundInitializer<>();

        // Act: Get the task count.
        int taskCount = initializer.getTaskCount();

        // Assert: Verify that the task count is the default value.
        assertEquals("The default task count should be 1", 1, taskCount);
    }
}