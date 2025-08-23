package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    @Test
    public void skip_withZeroArgument_returnsZeroAndDoesNotAdvanceReader() throws IOException {
        // Arrange
        // Using a non-empty sequence to confirm the reader's position is unaffected.
        CharSequenceReader reader = new CharSequenceReader("abc");

        // Act
        long skippedChars = reader.skip(0);

        // Assert
        assertEquals("Calling skip(0) should always return 0.", 0L, skippedChars);
        assertEquals("The reader's position should not change.", 'a', reader.read());
    }
}