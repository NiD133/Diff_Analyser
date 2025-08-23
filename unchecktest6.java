package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
class UncheckTest {

    private static final byte[] TEST_DATA = {'a', 'b'};

    /**
     * Tests that {@link Uncheck#apply(IOBiFunction, Object, Object)} correctly
     * invokes the function and returns its result when no I/O exception occurs.
     */
    @Test
    @DisplayName("Uncheck.apply with IOBiFunction should return result on success")
    void applyWithBiFunctionShouldReturnResultOnSuccess() {
        // Arrange
        final byte[] buffer = new byte[TEST_DATA.length];
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        
        // The IOBiFunction to be wrapped by Uncheck.apply
        final IOBiFunction<Integer, Integer, Integer> readOperation = 
            (offset, length) -> inputStream.read(buffer, offset, length);

        // Act
        final int bytesRead = Uncheck.apply(readOperation, 0, 1);

        // Assert
        assertEquals(1, bytesRead, "Should have read exactly one byte.");
        assertEquals('a', buffer[0], "The first byte in the buffer should be 'a'.");
    }
}