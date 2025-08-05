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

@SuppressWarnings("deprecation") // tests some deprecated classes
class OptionGroupTest {

    // Short option constants
    private static final String REVISION_OPT = "r";
    private static final String FILE_OPT = "f";
    private static final String DIRECTORY_OPT = "d";
    private static final String SECTION_OPT = "s";
    private static final String CHAPTER_OPT = "c";
    
    // Long-only option constants
    private static final String IMPORT_OPT = "import";
    private static final String EXPORT_OPT = "export";

    private Options options;
    private final Parser parser = new PosixParser();

    @BeforeEach
    void setUp() {
        // Group 1: File/Directory options (mutually exclusive)
        Option file = new Option(FILE_OPT, "file", false, "file to process");
        Option dir = new Option(DIRECTORY_OPT, "directory", false, "directory to process");
        OptionGroup fileGroup = new OptionGroup();
        fileGroup.addOption(file);
        fileGroup.addOption(dir);

        // Group 2: Section/Chapter options (mutually exclusive)
        Option section = new Option(SECTION_OPT, "section", false, "section to process");
        Option chapter = new Option(CHAPTER_OPT, "chapter", false, "chapter to process");
        OptionGroup sectionGroup = new OptionGroup();
        sectionGroup.addOption(section);
        sectionGroup.addOption(chapter);

        // Group 3: Import/Export options (long-only, mutually exclusive)
        Option importOpt = new Option(null, IMPORT_OPT, false, "import data");
        Option exportOpt = new Option(null, EXPORT_OPT, false, "export data");
        OptionGroup importExportGroup = new OptionGroup();
        importExportGroup.addOption(importOpt);
        importExportGroup.addOption(exportOpt);

        // Global options setup
        options = new Options()
            .addOptionGroup(fileGroup)
            .addOptionGroup(sectionGroup)
            .addOptionGroup(importExportGroup)
            .addOption(REVISION_OPT, "revision", false, "revision number");
    }

    @Test
    void getNames_ShouldReturnAllOptionNamesInGroup() {
        OptionGroup group = new OptionGroup();
        group.addOption(OptionBuilder.create('a'));
        group.addOption(OptionBuilder.create('b'));

        Collection<String> names = group.getNames();
        assertEquals(2, names.size(), "Group should contain two options");
        assertTrue(names.contains("a"), "Group should contain option 'a'");
        assertTrue(names.contains("b"), "Group should contain option 'b'");
    }

    @Test
    void parse_ShouldNotSetOptionsAndRetainArguments_WhenNoOptionsProvided() throws Exception {
        String[] args = {"arg1", "arg2"};
        CommandLine cl = parser.parse(options, args);

        assertNoOptionsSelected(cl);
        assertEquals(2, cl.getArgList().size(), "All arguments should be preserved");
    }

    @Test
    void parse_ShouldSetLongOption_WhenSingleLongOptionProvided() throws Exception {
        CommandLine cl = parser.parse(options, new String[]{"--file"});

        assertOptionSelected(cl, FILE_OPT);
        assertNoExtraArguments(cl);
    }

    @Test
    void parse_ShouldSetOption_WhenSingleOptionProvided() throws Exception {
        CommandLine cl = parser.parse(options, new String[]{"-r"});

        assertOptionSelected(cl, REVISION_OPT);
        assertNoExtraArguments(cl);
    }

    @Test
    void parse_ShouldSetOptionFromGroup_WhenSingleGroupOptionProvided() throws Exception {
        CommandLine cl = parser.parse(options, new String[]{"-f"});

        assertOptionSelected(cl, FILE_OPT);
        assertNoExtraArguments(cl);
    }

    @Test
    void toString_ShouldReturnFormattedOptionList() {
        // Group with long options only
        OptionGroup longOptGroup = new OptionGroup();
        longOptGroup.addOption(new Option(null, "foo", false, "Foo"));
        longOptGroup.addOption(new Option(null, "bar", false, "Bar"));
        assertEquals("[--foo Foo, --bar Bar]", longOptGroup.toString());

        // Group with short options
        OptionGroup shortOptGroup = new OptionGroup();
        shortOptGroup.addOption(new Option("f", "foo", false, "Foo"));
        shortOptGroup.addOption(new Option("b", "bar", false, "Bar"));
        assertEquals("[-f Foo, -b Bar]", shortOptGroup.toString());
    }

    @Test
    void parse_ShouldThrowException_WhenMutuallyExclusiveLongOptionsProvided() {
        String[] args = {"--file", "--directory"};
        AlreadySelectedException e = assertThrows(AlreadySelectedException.class, 
            () -> parser.parse(options, args));

        assertEquals(FILE_OPT, e.getOptionGroup().getSelected(), "Selected option mismatch");
        assertEquals(DIRECTORY_OPT, e.getOption().getOpt(), "Conflicting option mismatch");
    }

    @Test
    void parse_ShouldSetOptions_WhenOptionsFromDifferentGroups() throws Exception {
        CommandLine cl = parser.parse(options, new String[]{"-f", "-s"});

        assertOptionSelected(cl, FILE_OPT);
        assertOptionSelected(cl, SECTION_OPT);
        assertNoExtraArguments(cl);
    }

    @Test
    void parse_ShouldThrowException_WhenMutuallyExclusiveOptionsProvided() {
        String[] args = {"-f", "-d"};
        AlreadySelectedException e = assertThrows(AlreadySelectedException.class, 
            () -> parser.parse(options, args));

        assertEquals(FILE_OPT, e.getOptionGroup().getSelected(), "Selected option mismatch");
        assertEquals(DIRECTORY_OPT, e.getOption().getOpt(), "Conflicting option mismatch");
    }

    @Test
    void parse_ShouldIgnoreConflictingOptionInProperties_WhenOptionProvidedDirectly() throws Exception {
        Properties properties = new Properties();
        properties.put(DIRECTORY_OPT, "true");

        CommandLine cl = parser.parse(options, new String[]{"-f"}, properties);
        assertTrue(cl.hasOption(FILE_OPT), "File option should be set");
        assertFalse(cl.hasOption(DIRECTORY_OPT), "Directory option should be ignored");
    }

    @Test
    void parse_ShouldSetOptions_WhenValidLongOptionsProvided() throws Exception {
        CommandLine cl = parser.parse(options, new String[]{"--revision", "--file"});

        assertOptionSelected(cl, REVISION_OPT);
        assertOptionSelected(cl, FILE_OPT);
        assertNoExtraArguments(cl);
    }

    @Test
    void parse_ShouldSetOptions_WhenValidOptionsProvided() throws Exception {
        CommandLine cl = parser.parse(options, new String[]{"-r", "-f"});

        assertOptionSelected(cl, REVISION_OPT);
        assertOptionSelected(cl, FILE_OPT);
        assertNoExtraArguments(cl);
    }

    @Test
    void parse_ShouldSetLongOnlyOptions_WhenLongOnlyOptionsProvided() throws Exception {
        CommandLine cl1 = parser.parse(options, new String[]{"--export"});
        assertTrue(cl1.hasOption(EXPORT_OPT), "Export option should be set");

        CommandLine cl2 = parser.parse(options, new String[]{"--import"});
        assertTrue(cl2.hasOption(IMPORT_OPT), "Import option should be set");
    }

    // Helper assertions
    private void assertNoOptionsSelected(CommandLine cl) {
        assertFalse(cl.hasOption(REVISION_OPT), "Revision option not selected");
        assertFalse(cl.hasOption(FILE_OPT), "File option not selected");
        assertFalse(cl.hasOption(DIRECTORY_OPT), "Directory option not selected");
        assertFalse(cl.hasOption(SECTION_OPT), "Section option not selected");
        assertFalse(cl.hasOption(CHAPTER_OPT), "Chapter option not selected");
    }

    private void assertOptionSelected(CommandLine cl, String option) {
        assertTrue(cl.hasOption(option), () -> option + " option should be selected");
    }

    private void assertNoExtraArguments(CommandLine cl) {
        assertTrue(cl.getArgList().isEmpty(), "No extra arguments expected");
    }
}