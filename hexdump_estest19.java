package org.apache.commons.io;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Contains tests for the {@link HexDump} class.
 */
public class HexDumpTest {

    /**
     * Tests that calling HexDump.dump() with an empty byte array throws an
     * ArrayIndexOutOfBoundsException. This behavior suggests a potential bug,
     * as the method attempts to access an element from an empty array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testDumpWithEmptyByteArrayThrowsException() throws IOException {
        // Arrange: Create an empty byte array and a writer.
        final byte[] emptyData = new byte[0];
        final StringWriter outputWriter = new StringWriter();

        // Act: Call the dump method, which is expected to throw the exception.
        HexDump.dump(emptyData, outputWriter);

        // Assert: The test passes if the expected exception is thrown.
    }
}