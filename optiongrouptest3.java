package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the parser's handling of options within OptionGroups.
 */
@DisplayName("OptionGroup Parsing Test")
class OptionGroupTest {

    private Options options;
    private final Parser parser = new PosixParser();

    @BeforeEach
    void setUp() {
        options = new Options();

        // Group 1: Mutually exclusive source options (file or directory)
        final OptionGroup sourceGroup = new OptionGroup();
        sourceGroup.addOption(new Option("f", "file", false, "file to process"));
        sourceGroup.addOption(new Option("d", "directory", false, "directory to process"));
        options.addOptionGroup(sourceGroup);

        // Group 2: Mutually exclusive format options (section or chapter)
        final OptionGroup formatGroup = new OptionGroup();
        formatGroup.addOption(new Option("s", "section", false, "section to process"));
        formatGroup.addOption(new Option("c", "chapter", false, "chapter to process"));
        options.addOptionGroup(formatGroup);

        // Group 3: Mutually exclusive operation options (import or export)
        final OptionGroup operationGroup = new OptionGroup();
        operationGroup.addOption(new Option(null, "import", false, "import data"));
        operationGroup.addOption(new Option(null, "export", false, "export data"));
        options.addOptionGroup(operationGroup);

        // A standalone option, not in any group
        options.addOption("r", "revision", false, "revision number");
    }

    @Test
    @DisplayName("Providing a long option from a group should select it and exclude all others")
    void whenLongOptionFromGroupIsProvided_thenOnlyThatOptionIsSelected() throws Exception {
        // Arrange
        final String[] args = {"--file"};

        // Act
        final CommandLine cmd = parser.parse(options, args);

        // Assert
        assertAll("Verify that only the '--file' option is selected",
            () -> assertTrue(cmd.hasOption("f"),
                "The provided '--file' option should be present."),
            () -> assertFalse(cmd.hasOption("d"),
                "The mutually exclusive '--directory' option from the same group should NOT be present."),
            () -> assertFalse(cmd.hasOption("s"),
                "An option from a different group ('--section') should NOT be present."),
            () -> assertFalse(cmd.hasOption("c"),
                "Another option from a different group ('--chapter') should NOT be present."),
            () -> assertFalse(cmd.hasOption("r"),
                "A standalone option ('--revision') should NOT be present."),
            () -> assertTrue(cmd.getArgList().isEmpty(),
                "The argument list should be empty, as all arguments were parsed as options.")
        );
    }
}