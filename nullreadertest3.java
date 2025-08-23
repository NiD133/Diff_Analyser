package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link NullReader}.
 */
class NullReaderTest {

    // This message is specified by java.io.Reader for unsupported mark/reset operations.
    private static final String MARK_RESET_NOT_SUPPORTED_MSG = "mark/reset not supported";

    @Nested
    @DisplayName("when mark/reset is not supported")
    class WhenMarkNotSupported {

        private Reader reader;

        @BeforeEach
        void setUp() {
            // Arrange: Create a reader configured to not support mark/reset.
            // The size and EOF behavior are not relevant for these tests.
            reader = new NullReader(100, false, false);
        }

        @Test
        @DisplayName("markSupported() should return false")
        void markSupportedShouldReturnFalse() {
            // Act & Assert
            assertFalse(reader.markSupported());
        }

        @Test
        @DisplayName("mark() should throw UnsupportedOperationException")
        void markShouldThrowException() {
            // Act & Assert
            final UnsupportedOperationException e = assertThrows(
                UnsupportedOperationException.class,
                () -> reader.mark(5)
            );
            assertEquals(MARK_RESET_NOT_SUPPORTED_MSG, e.getMessage());
        }

        @Test
        @DisplayName("reset() should throw UnsupportedOperationException")
        void resetShouldThrowException() throws IOException {
            // Act & Assert
            final UnsupportedOperationException e = assertThrows(
                UnsupportedOperationException.class,
                () -> reader.reset()
            );
            assertEquals(MARK_RESET_NOT_SUPPORTED_MSG, e.getMessage());
        }
    }
}