package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for parsing options that are part of an OptionGroup.
 */
class OptionGroupTest {

    private Options options;
    private final Parser parser = new PosixParser();

    @BeforeEach
    void setUp() {
        // A group of mutually exclusive options: file or directory.
        OptionGroup fileOrDirectoryGroup = new OptionGroup();
        fileOrDirectoryGroup.addOption(new Option("f", "file", false, "file to process"));
        fileOrDirectoryGroup.addOption(new Option("d", "directory", false, "directory to process"));

        // A standalone option, not in any group.
        Option revisionOption = new Option("r", "revision", false, "revision number");

        options = new Options();
        options.addOptionGroup(fileOrDirectoryGroup);
        options.addOption(revisionOption);
    }

    @Test
    @DisplayName("When one option from a group is provided, only that option should be set.")
    void parse_whenSingleOptionFromGroupIsProvided_thenOnlyThatOptionIsSet() throws Exception {
        // Arrange: Command line arguments contain only the "-f" option.
        final String[] args = {"-f"};

        // Act: Parse the command line arguments.
        final CommandLine cmd = parser.parse(options, args);

        // Assert
        // The selected option from the group should be present.
        assertTrue(cmd.hasOption("f"), "The provided option '-f' should be set.");

        // The other, mutually exclusive option from the same group should NOT be present.
        assertFalse(cmd.hasOption("d"), "The un-provided option '-d' from the same group should not be set.");

        // Other unrelated options should not be affected.
        assertFalse(cmd.hasOption("r"), "The standalone option '-r' should not be set.");

        // There should be no remaining arguments.
        assertTrue(cmd.getArgList().isEmpty(), "There should be no unparsed arguments left.");
    }
}