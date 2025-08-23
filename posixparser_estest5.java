package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the flatten method in {@link PosixParser}.
 */
public class PosixParserTest {

    /**
     * Tests that the flatten method stops processing arguments when it encounters an
     * unrecognized option, if the 'stopAtNonOption' flag is true.
     *
     * The parser should treat the unrecognized option and all subsequent arguments
     * as non-option arguments, returning them as-is.
     */
    @Test
    public void flattenShouldStopAtUnrecognizedOptionWhenStopAtNonOptionIsTrue() throws Exception {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options(); // No options are defined.
        boolean stopAtNonOption = true;

        // Input arguments where "-j" is an unrecognized option.
        // The initial null should be skipped, but subsequent nulls are preserved.
        String[] arguments = { null, "-j", "arg1", null };

        // Act
        String[] flattenedArguments = parser.flatten(options, arguments, stopAtNonOption);

        // Assert
        // The parser should skip the initial null, then stop at "-j" because it's
        // not a defined option. It should then return "-j" and all subsequent
        // tokens without further processing.
        String[] expected = { "-j", "arg1", null };
        assertArrayEquals(expected, flattenedArguments);
    }
}