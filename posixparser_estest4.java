package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the PosixParser class, focusing on its token processing capabilities.
 */
public class PosixParserTest {

    /**
     * Tests that flatten() correctly "bursts" a token composed of multiple
     * unrecognized short options into individual options. For example, "-abc"
     * should be expanded to "-a", "-b", "-c".
     *
     * This behavior occurs when stopAtNonOption is false and the characters
     * within the token do not correspond to any defined Options.
     */
    @Test
    public void testFlattenBurstsCombinedTokenOfUnrecognizedOptions() throws ParseException {
        // Arrange
        Options options = new Options();
        // Add an option that is not part of the token to be burst.
        options.addOption("z", "zoom", false, "Zoom factor");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-abc"};
        boolean stopAtNonOption = false;

        // Act
        String[] flattenedArguments = parser.flatten(options, arguments, stopAtNonOption);

        // Assert
        String[] expected = {"-a", "-b", "-c"};
        assertArrayEquals("The combined token '-abc' should be burst into three separate tokens",
                          expected, flattenedArguments);
    }
}