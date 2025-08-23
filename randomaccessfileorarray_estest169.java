package com.itextpdf.text.pdf;

import org.junit.Test;
import org.evosuite.runtime.mock.java.net.MockURL;

import java.io.IOException;
import java.net.URL;

/**
 * This test class focuses on the constructor behavior of RandomAccessFileOrArray.
 * Note: The original test was auto-generated. This version has been refactored for clarity.
 */
public class RandomAccessFileOrArrayConstructorTest {

    /**
     * Verifies that the constructor throws a NullPointerException when initialized
     * with a malformed URL that causes an underlying error during stream creation.
     * <p>
     * This test simulates a scenario where the provided URL object is invalid in a way
     * that leads to a NullPointerException within Java's internal URL handling logic.
     * It ensures that the RandomAccessFileOrArray constructor correctly propagates this failure
     * instead of swallowing it or throwing a different exception.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void constructor_withMalformedUrl_throwsNullPointerException() throws IOException {
        // Arrange: Create a mock URL known to cause an exception upon access.
        // The MockURL.getFileExample() method provides a URL instance that is configured
        // to trigger a NullPointerException when its stream is opened, simulating a
        // problematic or corrupted URL input.
        URL malformedUrl = MockURL.getFileExample();

        // Act: Attempt to create a RandomAccessFileOrArray with the malformed URL.
        // The test expects this line to throw a NullPointerException.
        new RandomAccessFileOrArray(malformedUrl);

        // Assert: The assertion is handled by the @Test(expected) annotation.
        // If a NullPointerException is thrown, the test passes.
        // If any other exception is thrown, or if no exception is thrown, the test fails.
    }
}