package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NullReaderTest {

    @Test
    void testResettingBeyondMarkedLimitThrowsIOException() throws IOException {
        // Arrange: Create a NullReader (a reader that always returns -1, indicating end-of-stream).
        NullReader reader = new NullReader();

        // Act: Mark a position.  Note that the parameter to `mark()` is ignored in NullReader,
        // but we include it to demonstrate the intended usage of `mark()` and `reset()`.
        // The negative value passed to mark sets up a scenario where the `reset()`
        // will trigger an exception.
        reader.mark(-1791);

        // Assert: Verify that attempting to reset beyond the marked limit throws an IOException.
        // The expected exception message clearly describes the reason for the exception.
        IOException exception = assertThrows(IOException.class, () -> {
            reader.reset();
        });

        assertEquals("Marked position [0] is no longer valid - passed the read limit [-1791]", exception.getMessage());
    }
}