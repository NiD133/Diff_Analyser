package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains unit tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Verifies that the read(char[]) method throws a NullPointerException
     * when a null buffer is passed as an argument. This is the expected behavior
     * inherited from the base java.io.Reader class.
     */
    @Test(expected = NullPointerException.class)
    public void readWithNullCharArrayShouldThrowNullPointerException() throws IOException {
        // Arrange: Create a NullReader instance. The size is irrelevant for this test.
        final NullReader reader = new NullReader();

        // Act & Assert: Calling read with a null array should throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        reader.read(null);
    }
}