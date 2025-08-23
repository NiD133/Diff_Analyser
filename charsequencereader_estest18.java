package com.google.common.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

/**
 * Unit tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    // A JUnit Rule for declaratively testing exceptions.
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void read_afterBeingClosed_throwsIOException() throws IOException {
        // Arrange: Create a reader and immediately close it.
        // The actual content of the CharSequence is irrelevant for this test.
        CharSequenceReader reader = new CharSequenceReader("test data");
        reader.close();

        // Assert: Expect an IOException with a specific message.
        thrown.expect(IOException.class);
        thrown.expectMessage("reader closed");

        // Act: Attempt to read from the already closed reader.
        reader.read();
    }
}