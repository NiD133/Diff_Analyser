package org.apache.commons.lang3.stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Contains tests for {@link LangCollectors}.
 * This class focuses on edge cases and invalid arguments for the collect method.
 */
public class LangCollectorsTest {

    /**
     * Tests that {@link LangCollectors#collect(java.util.stream.Collector, Object...)}
     * throws a NullPointerException when the collector argument is null.
     */
    @Test
    public void collectShouldThrowNullPointerExceptionWhenCollectorIsNull() {
        // Arrange: The method under test should reject a null collector.
        // The array argument can be anything; an empty array is used for simplicity.
        final String[] inputArray = {};

        // Act & Assert: Call the method and verify the expected exception.
        final NullPointerException thrown = assertThrows(
            NullPointerException.class,
            () -> LangCollectors.collect(null, inputArray)
        );

        // Further Assert: The implementation uses Objects.requireNonNull with a specific message.
        // Verifying this message makes the test more robust and documents the expected behavior.
        assertEquals("collector", thrown.getMessage());
    }
}