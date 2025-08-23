package com.itextpdf.text.pdf;

import org.junit.Test;
import org.evosuite.runtime.mock.java.net.MockURL;

import java.io.IOException;
import java.net.URL;

/**
 * Test suite for the URL constructor of {@link RandomAccessFileOrArray}.
 */
public class RandomAccessFileOrArrayUrlConstructorTest {

    /**
     * Verifies that the RandomAccessFileOrArray constructor throws an IOException
     * when attempting to open a URL that points to a non-existent resource.
     *
     * @throws IOException because the constructor is expected to fail with this exception.
     */
    @Test(expected = IOException.class)
    public void constructorWithNonExistentUrlShouldThrowIOException() throws IOException {
        // Arrange: Create a mock URL that is well-formed but points to a resource
        // that cannot be reached.
        URL nonExistentUrl = MockURL.getHttpExample();

        // Act & Assert: Attempting to create a RandomAccessFileOrArray from this URL
        // should fail and throw an IOException. The @Test(expected) annotation handles the assertion.
        new RandomAccessFileOrArray(nonExistentUrl);
    }
}