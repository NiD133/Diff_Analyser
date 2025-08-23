package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the PosixParser class.
 */
public class PosixParserTest {

    /**
     * Tests that the flatten method treats a token that does not start with a
     * hyphen as a regular argument, passing it through unchanged.
     */
    @Test
    public void flattenShouldTreatTokenWithoutLeadingHyphenAsArgument() throws ParseException {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options();
        // An argument that does not start with a hyphen should be treated as a plain argument.
        String[] arguments = {"arg1", "$--", "arg2"};
        String[] expected = {"arg1", "$--", "arg2"};

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertArrayEquals("The token should be treated as a regular argument", expected, result);
    }
}