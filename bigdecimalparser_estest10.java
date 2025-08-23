package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * Contains tests for the {@link BigDecimalParser} class, focusing on edge cases and invalid inputs.
 */
public class BigDecimalParserTest {

    /**
     * Verifies that {@code BigDecimalParser.parse(char[])} throws a {@code NullPointerException}
     * when passed a null character array. This ensures the method correctly handles null inputs
     * as a precondition failure.
     */
    @Test
    public void parseWithNullCharArrayThrowsNullPointerException() {
        // The method under test is an internal helper. It is not expected to perform
        // extensive validation, but passing null is a fundamental error that should
        // result in a NullPointerException.
        assertThrows(NullPointerException.class, () -> {
            BigDecimalParser.parse((char[]) null);
        });
    }
}