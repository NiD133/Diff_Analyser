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
    public void testSkip() throws Exception {
        try (Reader reader = new TestNullReader(10, true, false)) {
            assertEquals(0, reader.read(), "Read 1");
            assertEquals(1, reader.read(), "Read 2");
            assertEquals(5, reader.skip(5), "Skip 1");
            assertEquals(7, reader.read(), "Read 3");
            // only 2 left to skip
            assertEquals(2, reader.skip(5), "Skip 2");
            // End of file
            assertEquals(-1, reader.skip(5), "Skip 3 (EOF)");
            final IOException e = assertThrows(IOException.class, () -> reader.skip(5));
            assertEquals("Skip after end of file", e.getMessage(), "Skip after EOF IOException message");
        }
    }
}
