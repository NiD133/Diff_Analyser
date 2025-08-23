package org.apache.commons.io.input;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

/**
 * Unit tests for the {@link ProxyReader} class.
 */
public class ProxyReaderTest {

    /**
     * Tests that the default implementation of the {@code afterRead()} hook method
     * is a no-op and can be called without throwing an exception.
     * <p>
     * The {@code afterRead()} method is a protected "hook" intended for subclasses
     * to override. This test verifies the behavior of the base implementation.
     * </p>
     */
    @Test
    public void afterReadDefaultImplementationIsNoOp() throws Exception {
        // Arrange: Create a concrete instance of ProxyReader to test the hook method.
        // CloseShieldReader is a simple, concrete subclass suitable for this purpose.
        final Reader originalReader = new StringReader("test data");
        final ProxyReader proxyReader = CloseShieldReader.wrap(originalReader);
        final int arbitraryCharsReadCount = 42;

        // Act: Call the method under test.
        proxyReader.afterRead(arbitraryCharsReadCount);

        // Assert: The test passes if the method call completes without throwing an
        // exception, confirming the default implementation is safe to call.
        // No explicit "assert" is needed for this type of test.
    }
}