package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;

/**
 * Tests the successful execution paths of the {@link Uncheck} utility class.
 */
public class UncheckTest {

    private static final byte[] TEST_DATA = {'a', 'b'};

    /**
     * Tests that {@link Uncheck#apply(IOFunction, Object)} executes a given IO-bound function,
     * returns its result, and that the function's side effects (state changes) are correctly applied.
     */
    @Test
    void applyShouldExecuteFunctionAndReturnResult() {
        // Arrange: Create an input stream with two bytes: 'a' and 'b'.
        final ByteArrayInputStream stream = new ByteArrayInputStream(TEST_DATA);

        // Act: Skip the first byte ('a') using Uncheck.apply.
        // This wraps the call to stream.skip(), which can throw an IOException.
        final Long skippedCount = Uncheck.apply(stream::skip, 1L);

        // Assert: Verify that one byte was skipped.
        assertEquals(1L, skippedCount, "The number of skipped bytes should be 1.");

        // Act: Read the next byte ('b') using Uncheck.get to verify the stream's state.
        final Integer nextByte = Uncheck.get(stream::read);

        // Assert: Verify that the correct byte was read after the skip operation.
        assertEquals('b', nextByte.intValue(), "The byte read after skipping should be 'b'.");
    }
}