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

/**
 * This test case focuses on verifying the behavior of a Reader (specifically, TestNullReader)
 * when it does NOT support the mark/reset functionality.  It checks that attempting to use
 * mark() and reset() throws the correct exception.
 */
public class GeneratedTestCase {

    // Constant for the expected error message when mark/reset is not supported.
    private static final String MARK_RESET_NOT_SUPPORTED = "mark() and reset() not supported";

    /**
     * Tests the behavior of a Reader when it does not support mark/reset.
     * It verifies that:
     * 1. markSupported() returns false.
     * 2. Calling mark() throws an UnsupportedOperationException with the correct message.
     * 3. Calling reset() throws an UnsupportedOperationException with the correct message.
     */
    @Test
    public void testMarkNotSupported() throws Exception {
        // Create a Reader that does NOT support mark/reset.
        //  TestNullReader(int size, boolean markSupported, boolean throwEof)
        final Reader reader = new TestNullReader(100, false, true); // Size 100, mark not supported, throws EOF.

        // Verify that markSupported() returns false, indicating that mark/reset is not supported.
        assertFalse(reader.markSupported(), "Reader should NOT support mark/reset.");

        // Attempt to call mark() and verify that it throws an UnsupportedOperationException.
        // This is the expected behavior when mark is not supported.
        try {
            reader.mark(5); // Attempt to mark the current position.
            fail("mark() should throw UnsupportedOperationException because it is not supported."); // The test should fail if no exception is thrown.
        } catch (final UnsupportedOperationException e) {
            // Verify that the exception message is the expected message.
            assertEquals(MARK_RESET_NOT_SUPPORTED, e.getMessage(), "mark() error message should indicate mark/reset is not supported.");
        }

        // Attempt to call reset() and verify that it throws an UnsupportedOperationException.
        // This is the expected behavior when reset is not supported.
        try {
            reader.reset(); // Attempt to reset the reader to the marked position.
            fail("reset() should throw UnsupportedOperationException because it is not supported."); // The test should fail if no exception is thrown.
        } catch (final UnsupportedOperationException e) {
            // Verify that the exception message is the expected message.
            assertEquals(MARK_RESET_NOT_SUPPORTED, e.getMessage(), "reset() error message should indicate mark/reset is not supported.");
        }

        // Close the reader to release resources.
        reader.close();
    }
}