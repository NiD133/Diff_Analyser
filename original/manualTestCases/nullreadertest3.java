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
    public void testMarkNotSupported() throws Exception {
        final Reader reader = new TestNullReader(100, false, true);
        assertFalse(reader.markSupported(), "Mark Should NOT be Supported");
        try {
            reader.mark(5);
            fail("mark() should throw UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            assertEquals(MARK_RESET_NOT_SUPPORTED, e.getMessage(), "mark() error message");
        }
        try {
            reader.reset();
            fail("reset() should throw UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            assertEquals(MARK_RESET_NOT_SUPPORTED, e.getMessage(), "reset() error message");
        }
        reader.close();
    }
}
