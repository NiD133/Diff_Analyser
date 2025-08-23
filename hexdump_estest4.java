package org.apache.commons.io;

import org.junit.Test;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * Tests for the {@link HexDump} class, focusing on exception handling.
 */
public class HexDumpTest {

    /**
     * Verifies that {@link HexDump#dump(byte[], Appendable)} throws a
     * {@link ReadOnlyBufferException} when the provided Appendable target is read-only.
     *
     * @throws IOException Not expected to be thrown in this test case.
     */
    @Test(expected = ReadOnlyBufferException.class)
    public void dumpShouldThrowExceptionWhenTargetIsReadOnlyBuffer() throws IOException {
        // Arrange: Create some arbitrary data and a read-only destination.
        // CharBuffer.wrap(CharSequence) is documented to create a read-only buffer,
        // which serves as our read-only Appendable for this test.
        byte[] dataToDump = {0x01, 0x02, 0x03};
        Appendable readOnlyTarget = CharBuffer.wrap("this is a read-only buffer");

        // Act: Attempt to dump the data to the read-only target.
        // This action is expected to throw a ReadOnlyBufferException.
        HexDump.dump(dataToDump, readOnlyTarget);

        // Assert: The test succeeds if the expected exception is thrown,
        // which is handled by the @Test(expected = ...) annotation.
    }
}