package com.google.common.io;

import org.junit.Test;
import java.io.Reader;

/**
 * This test suite verifies the argument validation of the 
 * {@link CharStreams#skipFully(Reader, long)} method.
 */
public class CharStreams_ESTestTest11 {

    /**
     * Tests that calling {@code skipFully} with a null Reader argument
     * results in a {@code NullPointerException}. This is the expected behavior
     * as the method requires a valid Reader instance to operate on, and this
     * test confirms the null-check precondition.
     */
    @Test(expected = NullPointerException.class)
    public void skipFully_withNullReader_throwsNullPointerException() {
        // Attempt to skip a positive number of characters (20) from a null reader,
        // which should trigger the exception.
        CharStreams.skipFully((Reader) null, 20L);
    }
}