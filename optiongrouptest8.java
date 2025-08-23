package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for interactions between different OptionGroups when parsing commands.
 */
class OptionGroupInteractionTest {

    @Test
    void shouldAllowOptionsFromDifferentGroups() throws ParseException {
        // Arrange
        // Group 1: Mutually exclusive options for file or directory
        OptionGroup fileOrDirectoryGroup = new OptionGroup();
        fileOrDirectoryGroup.addOption(new Option("f", "file", false, "file to process"));
        fileOrDirectoryGroup.addOption(new Option("d", "directory", false, "directory to process"));

        // Group 2: Mutually exclusive options for section or chapter
        OptionGroup sectionOrChapterGroup = new OptionGroup();
        sectionOrChapterGroup.addOption(new Option("s", "section", false, "section to process"));
        sectionOrChapterGroup.addOption(new Option("c", "chapter", false, "chapter to process"));

        Options options = new Options();
        options.addOptionGroup(fileOrDirectoryGroup);
        options.addOptionGroup(sectionOrChapterGroup);

        String[] args = {"-f", "-s"};
        Parser parser = new PosixParser();

        // Act
        CommandLine cmd = parser.parse(options, args);

        // Assert
        assertAll("Parsed command line should reflect one selected option from each group",
            () -> assertTrue(cmd.hasOption("f"), "Option '-f' should be set as it was provided."),
            () -> assertFalse(cmd.hasOption("d"), "Option '-d' should not be set as it's in the same group as the selected '-f' option."),
            () -> assertTrue(cmd.hasOption("s"), "Option '-s' should be set as it was provided."),
            () -> assertFalse(cmd.hasOption("c"), "Option '-c' should not be set as it's in the same group as the selected '-s' option."),
            () -> assertTrue(cmd.getArgList().isEmpty(), "Argument list should be empty as all inputs were valid options.")
        );
    }
}