package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Test suite for {@link MultiBackgroundInitializer}.
 */
public class MultiBackgroundInitializerTest {

    /**
     * Tests that addInitializer() throws a NullPointerException when the initializer name is null.
     */
    @Test(expected = NullPointerException.class)
    public void addInitializerShouldThrowNullPointerExceptionWhenNameIsNull() {
        // Arrange
        ExecutorService executor = Executors.newSingleThreadExecutor();
        MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer(executor);
        BackgroundInitializer<Object> childInitializer = new BackgroundInitializer<>(executor);

        // Act: Attempt to add an initializer with a null name.
        // This action is expected to throw a NullPointerException.
        multiInitializer.addInitializer(null, childInitializer);

        // Assert: The test passes if the expected NullPointerException is thrown.
        // This is handled by the @Test(expected = ...) annotation.
    }
}