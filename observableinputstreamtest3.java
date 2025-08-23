package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the behavior of the {@link ObservableInputStream#available()} method.
 */
public class ObservableInputStreamAvailableTest {

    @Test
    @DisplayName("available() should return the correct number of bytes before and after reads")
    void availableReturnsCorrectCountBeforeAndAfterReads() throws IOException {
        // Arrange: Create a stream with a known, small amount of data.
        final byte[] inputData = {'a', 'b', 'c'};
        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(inputData))) {

            // Assert 1: Before any reads, all bytes should be available.
            assertEquals(3, ois.available(), "Initially, all bytes should be available.");

            // Act & Assert 2: Read one byte and check that the available count decreases.
            assertNotEquals(IOUtils.EOF, ois.read(), "Reading the first byte should succeed.");
            assertEquals(2, ois.available(), "After reading one byte, available count should be 2.");

            // Act & Assert 3: Read the remaining bytes and ensure availability becomes zero.
            ois.read();
            ois.read();
            assertEquals(0, ois.available(), "After reading all bytes, available count should be 0.");
        }
    }
}