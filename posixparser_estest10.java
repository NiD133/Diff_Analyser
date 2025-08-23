package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for {@link PosixParser#flatten(Options, String[], boolean)}.
 */
public class PosixParser_ESTestTest10 extends PosixParser_ESTest_scaffolding {

    /**
     * This test verifies the behavior of the `flatten` method when the `stopAtNonOption`
     * flag is true.
     * <p>
     * 1. It checks that `flatten` correctly inserts a "--" argument terminator before the
     *    first non-option token.
     * 2. It then verifies that calling `flatten` again on the result is an idempotent
     *    operation, meaning the array is not changed further.
     */
    @Test
    public void flattenWithStopAtNonOptionAddsTerminatorAndIsIdempotent() throws ParseException {
        // Arrange
        final Options options = new Options();
        final PosixParser parser = new PosixParser();
        final boolean stopAtNonOption = true;
        final String[] originalArguments = {"arg1", "arg2"};

        // Act: First flatten operation
        // When stopAtNonOption is true, flatten should insert a "--" terminator
        // before the first token that is not a recognized option.
        final String[] flattenedOnce = parser.flatten(options, originalArguments, stopAtNonOption);

        // Assert: Check the result of the first flatten
        final String[] expectedAfterFirstFlatten = {"--", "arg1", "arg2"};
        assertArrayEquals("Expected '--' to be inserted before the first non-option.",
                          expectedAfterFirstFlatten, flattenedOnce);

        // Act: Second flatten operation
        // Running flatten again on the already-processed array should not change it.
        final String[] flattenedTwice = parser.flatten(options, flattenedOnce, stopAtNonOption);

        // Assert: Check the result of the second flatten
        assertArrayEquals("Flattening an array that already has a '--' terminator should be idempotent.",
                          flattenedOnce, flattenedTwice);
    }
}