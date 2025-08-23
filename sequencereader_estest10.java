package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.Reader;
import org.junit.Test;

/**
 * Unit tests for the {@link SequenceReader} class.
 */
public class SequenceReaderTest {

    /**
     * Tests that the constructor throws a NullPointerException when the provided
     * iterable of readers is null, as this is an invalid argument.
     */
    @Test
    public void constructorWithNullIterableShouldThrowNullPointerException() {
        // Arrange: No arrangement is necessary as we are testing a null input.

        // Act & Assert: We expect a NullPointerException when constructing a
        // SequenceReader with a null iterable. The assertThrows method cleanly
        // handles this check.
        NullPointerException thrown = assertThrows(
            NullPointerException.class,
            () -> new SequenceReader((Iterable<? extends Reader>) null)
        );

        // Assert: For a more robust test, we also verify the exception message.
        // The source class uses Objects.requireNonNull(readers, "readers"),
        // which provides a specific message.
        assertEquals("readers", thrown.getMessage());
    }
}