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
    public void testRead() throws Exception {
        final int size = 5;
        final TestNullReader reader = new TestNullReader(size);
        for (int i = 0; i < size; i++) {
            assertEquals(i, reader.read(), "Check Value [" + i + "]");
        }
        // Check End of File
        assertEquals(-1, reader.read(), "End of File");
        // Test reading after the end of file
        try {
            final int result = reader.read();
            fail("Should have thrown an IOException, value=[" + result + "]");
        } catch (final IOException e) {
            assertEquals("Read after end of file", e.getMessage());
        }
        // Close - should reset
        reader.close();
        assertEquals(0, reader.getPosition(), "Available after close");
    }
}
