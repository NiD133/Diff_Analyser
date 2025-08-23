package com.google.common.io;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}, primarily through the {@link ByteSource#concat(Iterator)} factory method.
 */
public class MultiInputStreamTest {

    /**
     * Verifies that concatenating a single ByteSource results in a new ByteSource
     * that has the same content as the original.
     */
    @Test
    public void concat_withSingleSource_shouldHaveSameContentAsOriginalSource() throws IOException {
        // Arrange: Use descriptive names and explicit, non-default test data.
        byte[] originalContent = new byte[] {10, 20, 30};
        ByteSource originalSource = ByteSource.wrap(originalContent);
        Iterator<ByteSource> sources = Collections.singletonList(originalSource).iterator();

        // Act: The action being tested is the concatenation.
        // This indirectly creates and uses a MultiInputStream.
        ByteSource concatenatedSource = ByteSource.concat(sources);

        // Assert: The assertion is now meaningful. It verifies that the concatenated
        // source has the expected content by comparing it to the original source,
        // not just itself. A descriptive message is added for clarity on failure.
        assertTrue(
            "A source concatenated from a single element should have the same content as the original.",
            concatenatedSource.contentEquals(originalSource));
    }
}