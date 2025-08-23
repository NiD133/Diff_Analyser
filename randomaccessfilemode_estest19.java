package org.apache.commons.io;

import org.apache.commons.io.function.IOFunction;
import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.file.Path;

/**
 * Tests for the {@link RandomAccessFileMode} enum, focusing on its method contracts.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that calling {@code apply(Path, IOFunction)} with null arguments
     * throws a {@link NullPointerException}.
     * <p>
     * This test verifies that the method correctly validates its inputs and does not
     * permit null values for the path or the function, adhering to a robust programming contract.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void testApplyWithNullArgumentsShouldThrowNullPointerException() {
        // Arrange: Select any RandomAccessFileMode instance. The specific mode is irrelevant for this test.
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE;

        // Act & Assert: Call the apply method with null for both arguments.
        // This is expected to throw a NullPointerException immediately.
        // The casts are required to resolve method signature ambiguity for the null literals.
        mode.apply((Path) null, (IOFunction<RandomAccessFile, ?>) null);
    }
}