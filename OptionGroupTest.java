/*
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for OptionGroup functionality including mutually exclusive options,
 * option selection validation, and proper parsing behavior.
 */
@SuppressWarnings("deprecation") // tests some deprecated classes
class OptionGroupTest {

    // Test fixture constants for better readability
    private static final String FILE_OPTION = "f";
    private static final String DIRECTORY_OPTION = "d";
    private static final String SECTION_OPTION = "s";
    private static final String CHAPTER_OPTION = "c";
    private static final String REVISION_OPTION = "r";
    private static final String IMPORT_OPTION = "import";
    private static final String EXPORT_OPTION = "export";

    private Options cliOptions;
    private final Parser commandLineParser = new PosixParser();

    /**
     * Sets up test fixture with three mutually exclusive option groups:
     * 1. File processing group: --file (-f) OR --directory (-d)
     * 2. Content processing group: --section (-s) OR --chapter (-c)  
     * 3. Data operation group: --import OR --export
     * Plus one standalone option: --revision (-r)
     */
    @BeforeEach
    public void setUp() {
        cliOptions = new Options();
        
        // Group 1: File processing options (mutually exclusive)
        OptionGroup fileProcessingGroup = createFileProcessingGroup();
        cliOptions.addOptionGroup(fileProcessingGroup);

        // Group 2: Content processing options (mutually exclusive)
        OptionGroup contentProcessingGroup = createContentProcessingGroup();
        cliOptions.addOptionGroup(contentProcessingGroup);

        // Group 3: Data operation options (mutually exclusive, long-form only)
        OptionGroup dataOperationGroup = createDataOperationGroup();
        cliOptions.addOptionGroup(dataOperationGroup);

        // Standalone option (not part of any group)
        cliOptions.addOption(REVISION_OPTION, "revision", false, "revision number");
    }

    private OptionGroup createFileProcessingGroup() {
        Option fileOption = new Option(FILE_OPTION, "file", false, "file to process");
        Option directoryOption = new Option(DIRECTORY_OPTION, "directory", false, "directory to process");
        
        OptionGroup group = new OptionGroup();
        group.addOption(fileOption);
        group.addOption(directoryOption);
        return group;
    }

    private OptionGroup createContentProcessingGroup() {
        Option sectionOption = new Option(SECTION_OPTION, "section", false, "section to process");
        Option chapterOption = new Option(CHAPTER_OPTION, "chapter", false, "chapter to process");
        
        OptionGroup group = new OptionGroup();
        group.addOption(sectionOption);
        group.addOption(chapterOption);
        return group;
    }

    private OptionGroup createDataOperationGroup() {
        Option importOption = new Option(null, IMPORT_OPTION, false, "import data");
        Option exportOption = new Option(null, EXPORT_OPTION, false, "export data");
        
        OptionGroup group = new OptionGroup();
        group.addOption(importOption);
        group.addOption(exportOption);
        return group;
    }

    @Test
    void testGetNames_ReturnsCorrectOptionNames() {
        // Given: An option group with two options
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(OptionBuilder.create('a'));
        optionGroup.addOption(OptionBuilder.create('b'));

        // When: Getting the names
        var names = optionGroup.getNames();

        // Then: Should return both option names
        assertNotNull(names, "Option names collection should not be null");
        assertEquals(2, names.size(), "Should contain exactly 2 option names");
        assertTrue(names.contains("a"), "Should contain option 'a'");
        assertTrue(names.contains("b"), "Should contain option 'b');
    }

    @Test
    void testGetNames_EmptyGroupHasNoSelection() {
        // Given: An empty option group
        OptionGroup emptyGroup = new OptionGroup();

        // Then: No option should be selected
        assertFalse(emptyGroup.isSelected(), "Empty group should have no selection");
    }

    @Test
    void testParseWithNoOptions_OnlyExtraArgumentsRemain() throws Exception {
        // Given: Command line with only non-option arguments
        String[] commandLineArgs = {"arg1", "arg2"};

        // When: Parsing the command line
        CommandLine parsedCommand = commandLineParser.parse(cliOptions, commandLineArgs);

        // Then: No options should be set, only extra arguments remain
        assertAllOptionsNotSet(parsedCommand);
        assertEquals(2, parsedCommand.getArgList().size(), "Should have exactly 2 extra arguments");
    }

    @Test
    void testParseSingleStandaloneOption() throws Exception {
        // Given: Command line with standalone revision option
        String[] commandLineArgs = {"-r"};

        // When: Parsing the command line
        CommandLine parsedCommand = commandLineParser.parse(cliOptions, commandLineArgs);

        // Then: Only revision option should be set
        assertTrue(parsedCommand.hasOption(REVISION_OPTION), "Revision option should be set");
        assertGroupOptionsNotSet(parsedCommand);
        assertTrue(parsedCommand.getArgList().isEmpty(), "Should have no extra arguments");
    }

    @Test
    void testParseSingleOptionFromGroup_ShortForm() throws Exception {
        // Given: Command line with single file option (short form)
        String[] commandLineArgs = {"-f"};

        // When: Parsing the command line
        CommandLine parsedCommand = commandLineParser.parse(cliOptions, commandLineArgs);

        // Then: Only file option should be set
        assertFalse(parsedCommand.hasOption(REVISION_OPTION), "Revision option should not be set");
        assertTrue(parsedCommand.hasOption(FILE_OPTION), "File option should be set");
        assertOtherGroupOptionsNotSet(parsedCommand, FILE_OPTION);
        assertTrue(parsedCommand.getArgList().isEmpty(), "Should have no extra arguments");
    }

    @Test
    void testParseSingleOptionFromGroup_LongForm() throws Exception {
        // Given: Command line with single file option (long form)
        String[] commandLineArgs = {"--file"};

        // When: Parsing the command line
        CommandLine parsedCommand = commandLineParser.parse(cliOptions, commandLineArgs);

        // Then: Only file option should be set
        assertFalse(parsedCommand.hasOption(REVISION_OPTION), "Revision option should not be set");
        assertTrue(parsedCommand.hasOption(FILE_OPTION), "File option should be set");
        assertOtherGroupOptionsNotSet(parsedCommand, FILE_OPTION);
        assertTrue(parsedCommand.getArgList().isEmpty(), "Should have no extra arguments");
    }

    @Test
    void testParseOptionsFromDifferentGroups_ShouldSucceed() throws Exception {
        // Given: Command line with options from different groups (file + section)
        String[] commandLineArgs = {"-f", "-s"};

        // When: Parsing the command line
        CommandLine parsedCommand = commandLineParser.parse(cliOptions, commandLineArgs);

        // Then: Both options should be set (they're from different groups)
        assertFalse(parsedCommand.hasOption(REVISION_OPTION), "Revision option should not be set");
        assertTrue(parsedCommand.hasOption(FILE_OPTION), "File option should be set");
        assertFalse(parsedCommand.hasOption(DIRECTORY_OPTION), "Directory option should not be set");
        assertTrue(parsedCommand.hasOption(SECTION_OPTION), "Section option should be set");
        assertFalse(parsedCommand.hasOption(CHAPTER_OPTION), "Chapter option should not be set");
        assertTrue(parsedCommand.getArgList().isEmpty(), "Should have no extra arguments");
    }

    @Test
    void testParseMultipleValidOptions_ShortForm() throws Exception {
        // Given: Command line with revision and file options
        String[] commandLineArgs = {"-r", "-f"};

        // When: Parsing the command line
        CommandLine parsedCommand = commandLineParser.parse(cliOptions, commandLineArgs);

        // Then: Both options should be set
        assertTrue(parsedCommand.hasOption(REVISION_OPTION), "Revision option should be set");
        assertTrue(parsedCommand.hasOption(FILE_OPTION), "File option should be set");
        assertOtherGroupOptionsNotSet(parsedCommand, FILE_OPTION);
        assertTrue(parsedCommand.getArgList().isEmpty(), "Should have no extra arguments");
    }

    @Test
    void testParseMultipleValidOptions_LongForm() throws Exception {
        // Given: Command line with revision and file options (long form)
        String[] commandLineArgs = {"--revision", "--file"};

        // When: Parsing the command line
        CommandLine parsedCommand = commandLineParser.parse(cliOptions, commandLineArgs);

        // Then: Both options should be set
        assertTrue(parsedCommand.hasOption(REVISION_OPTION), "Revision option should be set");
        assertTrue(parsedCommand.hasOption(FILE_OPTION), "File option should be set");
        assertOtherGroupOptionsNotSet(parsedCommand, FILE_OPTION);
        assertTrue(parsedCommand.getArgList().isEmpty(), "Should have no extra arguments");
    }

    @Test
    void testParseLongOnlyOptions() throws Exception {
        // Given & When & Then: Test each long-only option separately
        CommandLine exportCommand = commandLineParser.parse(cliOptions, new String[] {"--export"});
        assertTrue(exportCommand.hasOption(EXPORT_OPTION), "Export option should be set");

        CommandLine importCommand = commandLineParser.parse(cliOptions, new String[] {"--import"});
        assertTrue(importCommand.hasOption(IMPORT_OPTION), "Import option should be set");
    }

    @Test
    void testParseMutuallyExclusiveOptions_ShortForm_ShouldThrowException() throws Exception {
        // Given: Command line with two mutually exclusive options from same group
        String[] conflictingArgs = {"-f", "-d"};

        // When & Then: Should throw AlreadySelectedException
        AlreadySelectedException exception = assertThrows(
            AlreadySelectedException.class, 
            () -> commandLineParser.parse(cliOptions, conflictingArgs),
            "Should throw AlreadySelectedException for mutually exclusive options"
        );

        // Verify exception details
        assertNotNull(exception.getOptionGroup(), "Exception should contain the option group");
        assertTrue(exception.getOptionGroup().isSelected(), "Option group should show as selected");
        assertEquals(FILE_OPTION, exception.getOptionGroup().getSelected(), "First option should be selected");
        assertEquals(DIRECTORY_OPTION, exception.getOption().getOpt(), "Conflicting option should be directory");
    }

    @Test
    void testParseMutuallyExclusiveOptions_LongForm_ShouldThrowException() throws Exception {
        // Given: Command line with two mutually exclusive options from same group (long form)
        String[] conflictingArgs = {"--file", "--directory"};

        // When & Then: Should throw AlreadySelectedException
        AlreadySelectedException exception = assertThrows(
            AlreadySelectedException.class, 
            () -> commandLineParser.parse(cliOptions, conflictingArgs),
            "Should throw AlreadySelectedException for mutually exclusive long options"
        );

        // Verify exception details
        assertNotNull(exception.getOptionGroup(), "Exception should contain the option group");
        assertTrue(exception.getOptionGroup().isSelected(), "Option group should show as selected");
        assertEquals(FILE_OPTION, exception.getOptionGroup().getSelected(), "First option should be selected");
        assertEquals(DIRECTORY_OPTION, exception.getOption().getOpt(), "Conflicting option should be directory");
    }

    @Test
    void testParseWithProperties_GroupConflictHandling() throws Exception {
        // Given: Command line with file option and properties with conflicting directory option
        String[] commandLineArgs = {"-f"};
        Properties systemProperties = new Properties();
        systemProperties.put(DIRECTORY_OPTION, "true");

        // When: Parsing with properties
        CommandLine parsedCommand = commandLineParser.parse(cliOptions, commandLineArgs, systemProperties);

        // Then: Command line option takes precedence over properties
        assertTrue(parsedCommand.hasOption(FILE_OPTION), "File option from command line should be set");
        assertFalse(parsedCommand.hasOption(DIRECTORY_OPTION), "Directory option from properties should be ignored");
    }

    @Test
    void testToString_FormatsOptionsCorrectly() {
        // Test with long-only options
        OptionGroup longOnlyGroup = new OptionGroup();
        longOnlyGroup.addOption(new Option(null, "foo", false, "Foo description"));
        longOnlyGroup.addOption(new Option(null, "bar", false, "Bar description"));
        
        String longOnlyString = longOnlyGroup.toString();
        // Accept either order since LinkedHashMap preserves insertion order but test may vary
        assertTrue(
            "[--bar Bar description, --foo Foo description]".equals(longOnlyString) ||
            "[--foo Foo description, --bar Bar description]".equals(longOnlyString),
            "Long-only options should format correctly: " + longOnlyString
        );

        // Test with short+long options
        OptionGroup shortLongGroup = new OptionGroup();
        shortLongGroup.addOption(new Option("f", "foo", false, "Foo description"));
        shortLongGroup.addOption(new Option("b", "bar", false, "Bar description"));
        
        String shortLongString = shortLongGroup.toString();
        assertTrue(
            "[-b Bar description, -f Foo description]".equals(shortLongString) ||
            "[-f Foo description, -b Bar description]".equals(shortLongString),
            "Short+long options should format correctly: " + shortLongString
        );
    }

    // Helper methods for cleaner assertions

    private void assertAllOptionsNotSet(CommandLine parsedCommand) {
        assertFalse(parsedCommand.hasOption(REVISION_OPTION), "Revision option should not be set");
        assertGroupOptionsNotSet(parsedCommand);
    }

    private void assertGroupOptionsNotSet(CommandLine parsedCommand) {
        assertFalse(parsedCommand.hasOption(FILE_OPTION), "File option should not be set");
        assertFalse(parsedCommand.hasOption(DIRECTORY_OPTION), "Directory option should not be set");
        assertFalse(parsedCommand.hasOption(SECTION_OPTION), "Section option should not be set");
        assertFalse(parsedCommand.hasOption(CHAPTER_OPTION), "Chapter option should not be set");
    }

    private void assertOtherGroupOptionsNotSet(CommandLine parsedCommand, String exceptOption) {
        if (!FILE_OPTION.equals(exceptOption)) {
            assertFalse(parsedCommand.hasOption(FILE_OPTION), "File option should not be set");
        }
        if (!DIRECTORY_OPTION.equals(exceptOption)) {
            assertFalse(parsedCommand.hasOption(DIRECTORY_OPTION), "Directory option should not be set");
        }
        if (!SECTION_OPTION.equals(exceptOption)) {
            assertFalse(parsedCommand.hasOption(SECTION_OPTION), "Section option should not be set");
        }
        if (!CHAPTER_OPTION.equals(exceptOption)) {
            assertFalse(parsedCommand.hasOption(CHAPTER_OPTION), "Chapter option should not be set");
        }
    }
}