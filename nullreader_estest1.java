package org.apache.commons.io.input;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

/**
 * Tests for {@link NullReader}.
 */
public class NullReaderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that calling reset() before mark() results in an IOException.
     * The contract of Reader requires that reset() can only be called after
     * a position has been marked.
     */
    @Test
    public void reset_whenMarkNotCalled_throwsIOException() throws IOException {
        // Arrange: Set up the expectation for an IOException with a specific message.
        thrown.expect(IOException.class);
        thrown.expectMessage("No position has been marked");

        // Act: Attempt to reset the reader without having marked a position first.
        NullReader.INSTANCE.reset();
    }
}