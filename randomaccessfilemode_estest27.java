package org.apache.commons.io;

import org.junit.Test;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import org.apache.commons.io.function.IOFunction;
import org.evosuite.runtime.mock.java.io.MockFile;

/**
 * Contains tests for the {@link RandomAccessFileMode} enum.
 * This test focuses on ensuring proper handling of null arguments.
 */
public class RandomAccessFileMode_ESTestTest27 extends RandomAccessFileMode_ESTest_scaffolding {

    /**
     * Tests that calling the apply() method with a null IOFunction throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void applyWithNullFunctionShouldThrowNullPointerException() {
        // Arrange
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        final Path path = new MockFile("any-file.txt").toPath();
        final IOFunction<RandomAccessFile, Object> nullFunction = null;

        // Act
        // This call is expected to throw a NullPointerException because the function is null.
        mode.apply(path, nullFunction);

        // Assert: The exception is verified by the @Test(expected=...) annotation.
    }
}