package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for parsing command line arguments with OptionGroups.
 */
@DisplayName("OptionGroup Parsing Test")
class OptionGroupTest {

    private Options options;
    private final Parser parser = new PosixParser();

    @BeforeEach
    void setUp() {
        // A group of mutually exclusive options: -f or -d
        final OptionGroup fileOrDirGroup = new OptionGroup();
        fileOrDirGroup.addOption(new Option("f", "file", false, "file to process"));
        fileOrDirGroup.addOption(new Option("d", "directory", false, "directory to process"));

        options = new Options();
        options.addOptionGroup(fileOrDirGroup);

        // A standalone option not in any group
        options.addOption("r", "revision", false, "revision number");
    }

    @Test
    @DisplayName("Parsing should succeed with a standalone option and one option from a group")
    void parsingWithStandaloneOptionAndOptionFromGroupSucceeds() throws Exception {
        // Arrange: Command line with one standalone option and one from a group.
        final String[] args = {"-r", "-f"};

        // Act: Parse the command line arguments.
        final CommandLine cl = parser.parse(options, args);

        // Assert: Verify the correct options are present and no others.
        assertAll("Parsed command line state",
            () -> assertTrue(cl.hasOption("r"), "The standalone option '-r' should be present."),
            () -> assertTrue(cl.hasOption("f"), "The selected group option '-f' should be present."),
            () -> assertFalse(cl.hasOption("d"), "The unselected group option '-d' should NOT be present."),
            () -> assertTrue(cl.getArgList().isEmpty(), "There should be no leftover arguments.")
        );
    }
}