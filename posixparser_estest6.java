package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for {@link PosixParser}.
 */
public class PosixParserTest {

    /**
     * This test verifies that when the 'stopAtNonOption' flag is true, the flatten method
     * correctly handles an unrecognized long option. It should insert a double-hyphen ("--")
     * before the unrecognized option, effectively treating it and all subsequent tokens
     * as non-option arguments.
     */
    @Test
    public void flattenShouldStopAtUnrecognizedLongOptionWhenStopAtNonOptionIsTrue() throws ParseException {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("j", "known-option", false, "A known option.");

        // An array with an unrecognized long option followed by another argument.
        String[] arguments = {"--unrecognized-option", "some-value"};

        // Act
        // Call flatten with stopAtNonOption set to true.
        String[] flattenedArgs = parser.flatten(options, arguments, true);

        // Assert
        // The expected result is that the parser inserts "--" to terminate option processing.
        String[] expected = {
            "--",                      // Inserted to stop option processing.
            "--unrecognized-option",   // The unrecognized option is now a plain argument.
            "some-value"               // The subsequent token is also a plain argument.
        };

        assertArrayEquals("Flatten should insert '--' before the unrecognized long option", expected, flattenedArgs);
    }
}