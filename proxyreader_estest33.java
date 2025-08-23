package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains the refactored test case.
 * The original class name and scaffolding are preserved to maintain compatibility
 * with the existing test suite structure.
 */
public class ProxyReader_ESTestTest33 extends ProxyReader_ESTest_scaffolding {

    /**
     * Tests that calling {@code mark()} on a {@code ProxyReader} correctly
     * propagates an {@code IOException} when the underlying reader does not
     * support the mark operation.
     */
    @Test(timeout = 4000)
    public void markThrowsIOExceptionWhenUnsupportedByUnderlyingReader() {
        // Arrange: A ProxyReader wrapping a reader that doesn't support mark().
        // PipedReader is a standard Java IO class known for not supporting mark().
        final Reader underlyingReader = new PipedReader();
        // TaggedReader is a concrete subclass of the abstract ProxyReader used for testing.
        final ProxyReader proxyReader = new TaggedReader(underlyingReader);
        final int arbitraryReadAheadLimit = 1024;

        // Act & Assert: Expect an IOException with a specific message.
        try {
            proxyReader.mark(arbitraryReadAheadLimit);
            fail("An IOException should have been thrown because the underlying reader does not support mark().");
        } catch (final IOException e) {
            // The exception and its message are expected to be propagated from the underlying PipedReader.
            assertEquals("mark() not supported", e.getMessage());
        }
    }
}