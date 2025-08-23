package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.decorators.SerializedCache.CustomObjectInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test suite for the inner class {@link CustomObjectInputStream}.
 */
public class SerializedCache_CustomObjectInputStreamTest {

    /**
     * Verifies that the CustomObjectInputStream constructor throws an EOFException
     * when provided with an empty input stream. This is the expected behavior
     * because an ObjectInputStream requires a stream header, which is absent in an empty stream.
     */
    @Test(expected = EOFException.class)
    public void constructorShouldThrowEOFExceptionForEmptyStream() throws IOException {
        // Arrange: Create an empty input stream. This is the simplest way to
        // represent a stream with no data.
        InputStream emptyStream = new ByteArrayInputStream(new byte[0]);

        // Act: Attempt to create the custom object input stream from the empty source.
        // Assert: The @Test(expected) annotation asserts that an EOFException is thrown.
        new CustomObjectInputStream(emptyStream);
    }
}