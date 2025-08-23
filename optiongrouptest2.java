package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the behavior of the command-line parser when options are organized into
 * mutually exclusive OptionGroups.
 */
@DisplayName("Parser with OptionGroups")
class CommandLineParserWithOptionGroupsTest {

    private Options options;
    private final Parser parser = new PosixParser();

    @BeforeEach
    void setUp() {
        // Group 1: Mutually exclusive file or directory options
        final OptionGroup fileOrDirGroup = new OptionGroup();
        fileOrDirGroup.addOption(new Option("f", "file", false, "File to process"));
        fileOrDirGroup.addOption(new Option("d", "directory", false, "Directory to process"));

        // Group 2: Mutually exclusive section or chapter options
        final OptionGroup sectionOrChapterGroup = new OptionGroup();
        sectionOrChapterGroup.addOption(new Option("s", "section", false, "Section to process"));
        sectionOrChapterGroup.addOption(new Option("c", "chapter", false, "Chapter to process"));

        // Group 3: Mutually exclusive import or export (long options only)
        final OptionGroup importOrExportGroup = new OptionGroup();
        importOrExportGroup.addOption(new Option(null, "import", false, "Import data"));
        importOrExportGroup.addOption(new Option(null, "export", false, "Export data"));

        // A standalone option not in any group
        final Option revisionOption = new Option("r", "revision", false, "Revision number");

        options = new Options()
            .addOptionGroup(fileOrDirGroup)
            .addOptionGroup(sectionOrChapterGroup)
            .addOptionGroup(importOrExportGroup)
            .addOption(revisionOption);
    }

    @Test
    @DisplayName("Parsing only positional arguments should result in no options set")
    void parse_whenOnlyPositionalArgs_thenNoOptionsAreSetAndArgsAreCaptured() throws ParseException {
        // Arrange
        final String[] args = {"arg1", "arg2"};

        // Act
        final CommandLine cmd = parser.parse(options, args);

        // Assert
        assertAll("Ensure no options were detected",
            () -> assertFalse(cmd.hasOption("r"), "Standalone option 'r' should not be set."),
            () -> assertFalse(cmd.hasOption("f"), "Option 'f' from group 1 should not be set."),
            () -> assertFalse(cmd.hasOption("d"), "Option 'd' from group 1 should not be set."),
            () -> assertFalse(cmd.hasOption("s"), "Option 's' from group 2 should not be set."),
            () -> assertFalse(cmd.hasOption("c"), "Option 'c' from group 2 should not be set."),
            () -> assertFalse(cmd.hasOption("import"), "Option 'import' from group 3 should not be set."),
            () -> assertFalse(cmd.hasOption("export"), "Option 'export' from group 3 should not be set.")
        );

        assertAll("Ensure positional arguments are correctly captured",
            () -> assertNotNull(cmd.getArgList(), "Argument list should not be null."),
            () -> assertEquals(List.of("arg1", "arg2"), cmd.getArgList(), "The positional arguments should be 'arg1' and 'arg2'.")
        );
    }
}