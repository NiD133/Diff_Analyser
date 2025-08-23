package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

// Note: The original test was auto-generated. This version is refactored for clarity.
public class PosixParser_ESTestTest8 extends PosixParser_ESTest_scaffolding {

    /**
     * Tests that the flatten method correctly handles an array of arguments
     * containing nulls and an unrecognized long option (e.g., "--K").
     *
     * <p>The method should filter out the null values. Since no options are defined
     * and stopAtNonOption is false, it should treat the unrecognized long option
     * as a regular argument. The resulting array should contain only this argument.
     */
    @Test
    public void testFlattenWithUnrecognizedLongOptionAndNulls() throws ParseException {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options(); // No options are defined.
        
        // Input with nulls and an unrecognized long option.
        String[] arguments = {null, "--K", null};

        // Act
        String[] flattenedArguments = parser.flatten(options, arguments, false);

        // Assert
        String[] expected = {"--K"};
        assertArrayEquals("The flattened array should contain only the unrecognized option.",
                          expected, flattenedArguments);
    }
}