package org.apache.commons.io.input;

import org.apache.commons.io.input.ProxyReader;
import org.apache.commons.io.input.TaggedReader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Tests for {@link ProxyReader}.
 * This test focuses on the proxying behavior of the reset() method.
 */
// The original test class name and inheritance are preserved for context.
public class ProxyReader_ESTestTest3 extends ProxyReader_ESTest_scaffolding {

    /**
     * Tests that calling reset() on a proxied reader delegates the call correctly,
     * and throws an IOException if the underlying reader does not support reset()
     * without a prior mark().
     *
     * @see StringReader#reset()
     */
    @Test(expected = IOException.class)
    public void resetShouldThrowIOExceptionWhenUnderlyingReaderIsNotMarked() throws IOException {
        // Arrange: Create a ProxyReader wrapping a StringReader.
        // A StringReader will throw an IOException if reset() is called before mark().
        StringReader underlyingReader = new StringReader("test-data");
        // TaggedReader is a concrete implementation of the abstract ProxyReader.
        ProxyReader proxyReader = new TaggedReader(underlyingReader);

        // Act & Assert: Calling reset() should be proxied to the underlying
        // StringReader, which is expected to throw an IOException.
        proxyReader.reset();
    }
}