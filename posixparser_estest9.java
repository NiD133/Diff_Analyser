package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Test case for the PosixParser class, focusing on the flatten method's behavior.
 */
public class PosixParserTest {

    /**
     * Tests that a token resembling a long option with a value (e.g., "--opt=value"),
     * but which is not a defined option, is treated as a single argument when
     * stopAtNonOption is false.
     */
    @Test
    public void flattenShouldTreatUnrecognizedLongOptionWithEqualsAsSingleArgument() throws ParseException {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options(); // No options are defined.
        String[] arguments = {"--=<q;n"}; // The input token is an unrecognized long option with a value.

        // Act
        // The flatten method is called with stopAtNonOption set to false.
        String[] flattenedArgs = parser.flatten(options, arguments, false);

        // Assert
        // The parser should treat the entire token as a single, regular argument because
        // "--" is not a registered option and we are not stopping at non-options.
        String[] expected = {"--=<q;n"};
        assertArrayEquals("The unrecognized token should be returned as a single argument", expected, flattenedArgs);
    }
}