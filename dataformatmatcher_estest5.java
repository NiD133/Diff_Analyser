package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Contains tests for the {@link DataFormatMatcher} constructor.
 */
public class DataFormatMatcher_ESTestTest5 { // NOTE: Renaming to DataFormatMatcherTest is recommended

    /**
     * Verifies that the DataFormatMatcher constructor throws a NullPointerException
     * when the provided byte buffer is null, as it needs to access the buffer's length.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNPEWhenBufferIsNull() {
        // Arrange: Create dummy arguments for the constructor.
        // The actual values are not important, as the test focuses on the null buffer.
        InputStream dummyInputStream = new ByteArrayInputStream(new byte[0]);
        JsonFactory dummyJsonFactory = new JsonFactory();
        MatchStrength dummyMatchStrength = MatchStrength.INCONCLUSIVE;
        int bufferStart = 0;
        int bufferLength = 0;

        // Act & Assert: Instantiating with a null buffer should throw a NullPointerException.
        new DataFormatMatcher(
                dummyInputStream,
                null, // The null buffer that should cause the exception
                bufferStart,
                bufferLength,
                dummyJsonFactory,
                dummyMatchStrength
        );
    }
}