package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the flatten method in {@link PosixParser}.
 */
public class PosixParserFlattenTest {

    /**
     * Tests that flatten treats an unrecognized option cluster as a single, literal argument
     * when no matching options are defined and stopAtNonOption is false.
     *
     * The PosixParser's "bursting" logic should stop at the first unrecognized character
     * in a token (in this case, '/') and return the original token unmodified.
     */
    @Test
    public void flattenShouldTreatUnrecognizedOptionClusterAsSingleArgument() throws ParseException {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options(); // No options are defined for this test.

        // The input "-/C" looks like an option cluster, but since '/' is not a valid
        // option character, the parser should not "burst" it.
        String[] arguments = {"-/C"};
        boolean stopAtNonOption = false;

        // Act
        String[] flattenedArguments = parser.flatten(options, arguments, stopAtNonOption);

        // Assert
        String[] expectedArguments = {"-/C"};
        assertArrayEquals(
            "Parser should return the unrecognized token as a single argument",
            expectedArguments,
            flattenedArguments
        );
    }
}