package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@code Uncheck.test(IOPredicate, T)} method.
 */
class UncheckTest {

    @Test
    @DisplayName("Should return the predicate's boolean result when no exception is thrown")
    void test_whenPredicateSucceeds_returnsResult() {
        // Arrange: Create a predicate that performs a side effect and returns true.
        final AtomicReference<String> sideEffectState = new AtomicReference<>();
        final String testInput = "input data";
        final IOPredicate<String> successfulPredicate = input -> {
            sideEffectState.set(input);
            return true;
        };

        // Act: Execute the predicate via Uncheck.test.
        final boolean result = Uncheck.test(successfulPredicate, testInput);

        // Assert: Verify the returned value and the side effect.
        assertTrue(result, "Expected Uncheck.test to return true for a successful predicate.");
        assertEquals(testInput, sideEffectState.get(), "The side effect of the predicate should be observable.");
    }

    @Test
    @DisplayName("Should wrap a thrown IOException in an UncheckedIOException")
    void test_whenPredicateThrowsIOException_thenWrapsInUncheckedIOException() {
        // Arrange: Create a predicate that always throws a specific IOException.
        final IOException cause = new IOException("Test I/O failure");
        final IOPredicate<Object> throwingPredicate = t -> {
            throw cause;
        };

        // Act & Assert: Verify that Uncheck.test throws UncheckedIOException and that its cause is the original IOException.
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.test(throwingPredicate, "any input");
        });
        
        assertEquals(cause, thrown.getCause(), "The cause of the UncheckedIOException should be the original IOException.");
    }
}