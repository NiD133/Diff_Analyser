package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link UTF8Writer} class, focusing on its constructor.
 */
public class UTF8WriterTest {

    /**
     * Verifies that the constructor throws a NullPointerException when the
     * provided IOContext is null. The IOContext is a mandatory dependency
     * used for buffer allocation, and a null value should result in an immediate failure.
     */
    @Test
    public void constructor_whenGivenNullIOContext_shouldThrowNullPointerException() {
        // Arrange: Create a valid, non-null OutputStream to isolate the test
        // to the IOContext parameter.
        OutputStream dummyOutputStream = new ByteArrayOutputStream();
        IOContext nullIOContext = null;

        // Act & Assert
        try {
            new UTF8Writer(nullIOContext, dummyOutputStream);
            fail("Expected a NullPointerException because the IOContext cannot be null.");
        } catch (NullPointerException e) {
            // This is the expected behavior, so the test passes.
            // No further assertions are needed.
        }
    }
}