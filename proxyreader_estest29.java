package org.apache.commons.io.input;

import static org.junit.Assert.assertThrows;

import java.io.PipedReader;
import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Tests for {@link ProxyReader}.
 */
public class ProxyReaderTest {

    /**
     * Tests that calling read() with a null CharBuffer throws a NullPointerException.
     * This is the expected behavior of the underlying java.io.Reader, which
     * ProxyReader should correctly delegate to.
     */
    @Test
    public void readCharBufferWithNullTargetShouldThrowNullPointerException() {
        // Arrange: Create a concrete instance of ProxyReader.
        // TaggedReader is a simple subclass suitable for this test.
        // The underlying PipedReader is a standard Reader implementation.
        final ProxyReader proxyReader = new TaggedReader(new PipedReader());

        // Act & Assert: Verify that calling read with a null buffer throws the expected exception.
        // The cast to (CharBuffer) is necessary to resolve method ambiguity for the null argument.
        assertThrows(NullPointerException.class, () -> proxyReader.read((CharBuffer) null));
    }
}