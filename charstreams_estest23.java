package com.google.common.io;

import org.junit.Test;
import java.io.Reader;

/**
 * Unit tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    @Test(expected = NullPointerException.class)
    public void copyReaderToBuilder_whenReaderIsNull_throwsNullPointerException() {
        // Arrange: A destination StringBuilder is prepared. The source Reader is null.
        StringBuilder destination = new StringBuilder();

        // Act: Attempt to copy from a null reader to the StringBuilder.
        CharStreams.copyReaderToBuilder(null, destination);

        // Assert: The test expects a NullPointerException to be thrown, which is
        // handled by the @Test(expected = NullPointerException.class) annotation.
    }
}