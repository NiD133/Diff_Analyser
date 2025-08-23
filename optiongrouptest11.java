package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests parsing command lines that combine standalone options with options from an OptionGroup.
 */
public class OptionGroupTestTest11 {

    private Options options;
    private final Parser parser = new PosixParser();

    @BeforeEach
    void setUp() {
        // Define a group of mutually exclusive options: --file or --directory
        final OptionGroup fileOrDirGroup = new OptionGroup();
        fileOrDirGroup.addOption(new Option("f", "file", false, "file to process"));
        fileOrDirGroup.addOption(new Option("d", "directory", false, "directory to process"));

        // Configure the options for the parser
        options = new Options();
        options.addOptionGroup(fileOrDirGroup);
        options.addOption("r", "revision", false, "revision number"); // Add a standalone option
    }

    @Test
    @DisplayName("Parser should handle a standalone option alongside one option from a group")
    void parse_whenGivenStandaloneOptionAndOneOptionFromGroup_shouldSucceed() throws ParseException {
        // Arrange: Command line with a standalone option (--revision) and one from a group (--file)
        final String[] args = {"--revision", "--file"};

        // Act: Parse the command line arguments
        final CommandLine commandLine = parser.parse(options, args);

        // Assert: Verify that the correct options were detected and no others
        assertAll("Command line parsing results",
            () -> assertTrue(commandLine.hasOption("r"), "The standalone 'revision' option should be present."),
            () -> assertTrue(commandLine.hasOption("f"), "The 'file' option from the group should be present."),
            () -> assertFalse(commandLine.hasOption("d"), "The mutually exclusive 'directory' option should NOT be present."),
            () -> assertTrue(commandLine.getArgList().isEmpty(), "There should be no remaining arguments.")
        );
    }
}