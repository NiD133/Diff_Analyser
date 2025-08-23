package org.apache.commons.io;

import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Tests for {@link HexDump}.
 */
public class HexDumpTest {

    /**
     * Tests that an IOException is thrown when attempting to dump data to an Appendable
     * that has already been closed.
     */
    @Test(expected = IOException.class)
    public void testDumpToClosedAppendableThrowsIOException() throws IOException {
        // Arrange: Create test data and a writer that is immediately closed.
        final byte[] testData = new byte[16];
        final StringWriter closedWriter = new StringWriter();
        closedWriter.close();

        // Act & Assert: Attempting to dump to the closed writer should throw an IOException.
        HexDump.dump(testData, 0L, closedWriter, 0, 1);
    }
}