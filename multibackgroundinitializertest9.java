package org.apache.commons.lang3.concurrent;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the isSuccessful() method of MultiBackgroundInitializer's result object.
 */
public class MultiBackgroundInitializerIsSuccessfulTest extends AbstractLangTest {

    private MultiBackgroundInitializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new MultiBackgroundInitializer();
    }

    @Test
    @DisplayName("isSuccessful() should return false when a child initializer throws an exception")
    void isSuccessfulShouldReturnFalseWhenChildInitializerFails() throws ConcurrentException {
        // Arrange: Add a child initializer that is designed to fail.
        final BackgroundInitializer<Object> failingInitializer = new BackgroundInitializer<>() {
            @Override
            protected Object initialize() throws Exception {
                throw new Exception("This initializer is designed to fail for the test.");
            }
        };
        initializer.addInitializer("failingChild", failingInitializer);

        // Act: Start the initializers and retrieve the results.
        initializer.start();
        final MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.get();

        // Assert: The overall result should not be successful.
        assertFalse(results.isSuccessful(), "isSuccessful() should be false because a child initializer failed.");
    }
}