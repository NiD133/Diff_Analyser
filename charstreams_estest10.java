package com.google.common.io;

import org.junit.Test;
import java.io.FileDescriptor;
import java.io.Reader;
import org.evosuite.runtime.mock.java.io.MockFileReader;

public class CharStreams_ESTestTest10 extends CharStreams_ESTest_scaffolding {

    /**
     * Verifies that {@code CharStreams.skipFully()} propagates exceptions thrown by the underlying reader.
     *
     * <p><b>Test Scenario:</b>
     * <ul>
     *   <li><b>Given:</b> An invalid {@link MockFileReader} is created with a default, uninitialized
     *       {@link FileDescriptor}. Any attempt to read from this reader will cause a
     *       {@code NullPointerException}.</li>
     *   <li><b>When:</b> {@code CharStreams.skipFully()} is invoked with this invalid reader.</li>
     *   <li><b>Then:</b> The method is expected to throw a {@code NullPointerException}, demonstrating
     *       that the exception from the reader is not caught and suppressed.</li>
     * </ul>
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void skipFully_whenReaderThrowsException_propagatesException() throws Throwable {
        // Arrange: Create a reader that is guaranteed to fail upon use.
        // A MockFileReader initialized with a default FileDescriptor is in an invalid
        // state and will throw a NullPointerException on any read operation.
        FileDescriptor invalidFileDescriptor = new FileDescriptor();
        Reader faultyReader = new MockFileReader(invalidFileDescriptor);
        long charactersToSkip = 222L;

        // Act: Attempt to skip characters using the faulty reader.
        // This is expected to trigger the NullPointerException from the reader.
        CharStreams.skipFully(faultyReader, charactersToSkip);

        // Assert: The test will pass if the expected NullPointerException is thrown,
        // as specified by the 'expected' attribute in the @Test annotation.
    }
}