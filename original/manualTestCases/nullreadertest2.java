package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import org.junit.jupiter.api.Test;

public class GeneratedTestCase {

    @Test
    public void testMarkAndReset() throws Exception {
        int position = 0;
        final int readLimit = 10;
        try (Reader reader = new TestNullReader(100, true, false)) {
            assertTrue(reader.markSupported(), "Mark Should be Supported");
            // No Mark
            final IOException resetException = assertThrows(IOException.class, reader::reset);
            assertEquals("No position has been marked", resetException.getMessage(), "No Mark IOException message");
            for (; position < 3; position++) {
                assertEquals(position, reader.read(), "Read Before Mark [" + position + "]");
            }
            // Mark
            reader.mark(readLimit);
            // Read further
            for (int i = 0; i < 3; i++) {
                assertEquals(position + i, reader.read(), "Read After Mark [" + i + "]");
            }
            // Reset
            reader.reset();
            // Read From marked position
            for (int i = 0; i < readLimit + 1; i++) {
                assertEquals(position + i, reader.read(), "Read After Reset [" + i + "]");
            }
            // Reset after read limit passed
            final IOException e = assertThrows(IOException.class, reader::reset);
            assertEquals("Marked position [" + position + "] is no longer valid - passed the read limit [" + readLimit + "]", e.getMessage(), "Read limit IOException message");
        }
    }
}
