package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link Uncheck#apply(IOTriFunction, Object, Object, Object)}.
 */
class UncheckApplyTriFunctionTest {

    private static final byte[] TEST_DATA = {'a', 'b'};

    /**
     * Tests that Uncheck.apply() correctly invokes a three-argument, IO-throwing
     * function and returns its result when the operation succeeds.
     */
    @Test
    void shouldApplyTriFunctionAndReturnResultOnSuccess() {
        // Arrange
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        final byte[] buffer = new byte[TEST_DATA.length];
        // The IOTriFunction to test is InputStream#read(byte[], int, int)
        final IOTriFunction<byte[], Integer, Integer, Integer> readFunction = inputStream::read;

        // Act
        // Uncheck.apply should wrap the call and return the result from the read function.
        final int bytesRead = Uncheck.apply(readFunction, buffer, 0, 1);

        // Assert
        assertEquals(1, bytesRead, "Should return the number of bytes read.");
        assertEquals('a', buffer[0], "The first byte should be read into the buffer.");
    }
}