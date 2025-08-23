package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests the behavior of the Parser when handling mutually exclusive options defined in an OptionGroup.
 */
class OptionGroupParsingTest {

    /**
     * This test verifies that the parser correctly identifies when two options from the same
     * mutually exclusive group are provided in the command line arguments.
     *
     * It expects an {@link AlreadySelectedException} to be thrown, and it checks that the
     * exception details accurately report the conflict.
     */
    @Test
    void shouldThrowAlreadySelectedExceptionWhenTwoOptionsFromSameGroupAreProvided() {
        // Arrange: Create a group for mutually exclusive file/directory options.
        final Option fileOption = new Option("f", "file", false, "Path to a file.");
        final Option dirOption = new Option("d", "directory", false, "Path to a directory.");

        final OptionGroup fileOrDirGroup = new OptionGroup();
        fileOrDirGroup.addOption(fileOption);
        fileOrDirGroup.addOption(dirOption);

        final Options options = new Options();
        options.addOptionGroup(fileOrDirGroup);

        // These arguments contain two options from the same mutually exclusive group.
        final String[] args = {"-f", "-d"};
        final Parser parser = new PosixParser();

        // Act & Assert: Expect an exception because -f and -d cannot be used together.
        final AlreadySelectedException exception = assertThrows(
            AlreadySelectedException.class,
            () -> parser.parse(options, args),
            "Parsing should fail when two options from the same group are provided."
        );

        // Assert on exception details to ensure the conflict was correctly identified.
        final OptionGroup exceptionGroup = exception.getOptionGroup();
        assertNotNull(exceptionGroup, "Exception should contain the conflicting option group.");

        // The group should report that the first option ('f') was selected before the conflict.
        assertEquals("f", exceptionGroup.getSelected(),
            "The first option provided ('f') should be the one marked as selected.");

        // The exception should identify the option that caused the conflict ('d').
        assertEquals(dirOption, exception.getOption(),
            "The second option ('d') should be identified as the conflicting option.");
    }
}