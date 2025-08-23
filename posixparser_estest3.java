package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the flatten method in {@link PosixParser}.
 */
public class PosixParserTest {

    /**
     * Tests that the flatten method correctly processes an argument array containing null elements.
     * It should filter out the nulls, returning only the non-null tokens.
     */
    @Test
    public void flatten_shouldFilterOutNullsFromArgumentArray() throws ParseException {
        // Arrange
        Options options = new Options();
        // Define a valid option. While not strictly necessary for this flatten() test,
        // it represents a realistic setup.
        options.addOption("j", "job", true, "Job name");

        PosixParser parser = new PosixParser();

        // An input array with nulls, which might occur if arguments are built programmatically.
        String[] argumentsWithNulls = {null, "-j", null, null};
        boolean stopAtNonOption = true;

        // Act
        String[] flattenedArguments = parser.flatten(options, argumentsWithNulls, stopAtNonOption);

        // Assert
        // The flattened array should contain only the non-null token from the input.
        String[] expectedArguments = {"-j"};
        assertArrayEquals(expectedArguments, flattenedArguments);
    }
}