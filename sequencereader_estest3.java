package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;

/**
 * Unit tests for the {@link SequenceReader} class, focusing on input validation.
 */
public class SequenceReaderTest {

    /**
     * Tests that read(char[], int, int) throws a NullPointerException when the
     * provided character buffer is null. This behavior is part of the general
     * contract of the {@link java.io.Reader} class.
     */
    @Test(expected = NullPointerException.class)
    public void testReadWithNullBufferShouldThrowNullPointerException() throws IOException {
        // Arrange: Create a SequenceReader. The source of the reader is not important
        // for this test, so we use an empty collection.
        final SequenceReader sequenceReader = new SequenceReader(Collections.<Reader>emptyList());

        // Act: Attempt to read into a null buffer. The test is expected to throw
        // a NullPointerException at this point. The offset and length values are
        // irrelevant as the null check on the buffer should occur first.
        sequenceReader.read(null, 0, 0);

        // Assert: The test will pass if a NullPointerException is thrown, as
        // specified by the @Test(expected) annotation.
    }
}