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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation") // tests some deprecated classes
class OptionGroupTest {

    @Nested
    @DisplayName("State-based Tests")
    class StateTests {

        @Test
        @DisplayName("getNames() should return all option keys in the group")
        void getNamesShouldReturnAllOptionKeys() {
            // Given
            final OptionGroup group = new OptionGroup();
            group.addOption(Option.builder("a").build());
            group.addOption(Option.builder("b").build());

            // When
            final var names = group.getNames();

            // Then
            assertNotNull(names, "Names collection should not be null");
            assertEquals(2, names.size());
            assertTrue(names.contains("a"));
            assertTrue(names.contains("b"));
        }

        @Test
        @DisplayName("toString() should provide a clear representation of the options in the group")
        void toStringShouldReturnCorrectStringRepresentation() {
            // Given
            final OptionGroup groupWithLongOpts = new OptionGroup();
            groupWithLongOpts.addOption(new Option(null, "foo", false, "Foo description"));
            groupWithLongOpts.addOption(new Option(null, "bar", false, "Bar description"));

            final OptionGroup groupWithShortOpts = new OptionGroup();
            groupWithShortOpts.addOption(new Option("f", "foo", false, "Foo description"));
            groupWithShortOpts.addOption(new Option("b", "bar", false, "Bar description"));

            // When & Then
            assertEquals("[--foo Foo description, --bar Bar description]", groupWithLongOpts.toString());
            assertEquals("[-f Foo description, -b Bar description]", groupWithShortOpts.toString());
        }
    }

    @Nested
    @DisplayName("Parsing Tests")
    class ParsingTests {

        private final Parser parser = new PosixParser();

        @Test
        @DisplayName("Parser should throw AlreadySelectedException when two short options from the same group are provided")
        void parseWhenTwoShortOptionsFromSameGroupThenThrowException() {
            // Given
            final OptionGroup group = new OptionGroup();
            group.addOption(new Option("f", "file", false, "file to process"));
            group.addOption(new Option("d", "directory", false, "directory to process"));
            final Options options = new Options().addOptionGroup(group);
            final String[] args = {"-f", "-d"};

            // When & Then
            final AlreadySelectedException e = assertThrows(AlreadySelectedException.class,
                () -> parser.parse(options, args));

            assertEquals("d", e.getOption().getOpt(), "The conflicting option should be 'd'");
            assertNotNull(e.getOptionGroup());
            assertEquals("f", e.getOptionGroup().getSelected(), "The already selected option should be 'f'");
        }

        @Test
        @DisplayName("Parser should throw AlreadySelectedException when two long options from the same group are provided")
        void parseWhenTwoLongOptionsFromSameGroupThenThrowException() {
            // Given
            final OptionGroup group = new OptionGroup();
            group.addOption(new Option("f", "file", false, "file to process"));
            group.addOption(new Option("d", "directory", false, "directory to process"));
            final Options options = new Options().addOptionGroup(group);
            final String[] args = {"--file", "--directory"};

            // When & Then
            final AlreadySelectedException e = assertThrows(AlreadySelectedException.class,
                () -> parser.parse(options, args));

            assertEquals("d", e.getOption().getOpt(), "The conflicting option should be 'd'");
            assertNotNull(e.getOptionGroup());
            assertEquals("f", e.getOptionGroup().getSelected(), "The already selected option should be 'f'");
        }

        @Test
        @DisplayName("Parser should succeed when a single option from a group is provided")
        void parseWhenSingleOptionFromGroupThenSucceed() throws ParseException {
            // Given
            final OptionGroup group = new OptionGroup();
            group.addOption(new Option("f", "file", false, "file to process"));
            group.addOption(new Option("d", "directory", false, "directory to process"));
            final Options options = new Options().addOptionGroup(group);
            final String[] args = {"-f"};

            // When
            final CommandLine cl = parser.parse(options, args);

            // Then
            assertTrue(cl.hasOption("f"));
            assertFalse(cl.hasOption("d"));
            assertTrue(cl.getArgList().isEmpty());
        }

        @Test
        @DisplayName("Parser should succeed when options from different groups are provided")
        void parseWhenOptionsFromDifferentGroupsThenSucceed() throws ParseException {
            // Given
            final OptionGroup group1 = new OptionGroup();
            group1.addOption(new Option("f", "file", false, "file to process"));
            group1.addOption(new Option("d", "directory", false, "directory to process"));

            final OptionGroup group2 = new OptionGroup();
            group2.addOption(new Option("s", "section", false, "section to process"));
            group2.addOption(new Option("c", "chapter", false, "chapter to process"));

            final Options options = new Options().addOptionGroup(group1).addOptionGroup(group2);
            final String[] args = {"-f", "-s"};

            // When
            final CommandLine cl = parser.parse(options, args);

            // Then
            assertTrue(cl.hasOption("f"));
            assertFalse(cl.hasOption("d"));
            assertTrue(cl.hasOption("s"));
            assertFalse(cl.hasOption("c"));
            assertTrue(cl.getArgList().isEmpty());
        }

        @Test
        @DisplayName("Parser should succeed when a standalone option and a group option are used")
        void parseWhenStandaloneAndGroupOptionThenSucceed() throws ParseException {
            // Given
            final OptionGroup group = new OptionGroup();
            group.addOption(new Option("f", "file", false, "file to process"));
            final Options options = new Options()
                .addOptionGroup(group)
                .addOption("r", "revision", false, "revision number");
            final String[] args = {"-r", "-f"};

            // When
            final CommandLine cl = parser.parse(options, args);

            // Then
            assertTrue(cl.hasOption("r"));
            assertTrue(cl.hasOption("f"));
            assertTrue(cl.getArgList().isEmpty());
        }

        @Test
        @DisplayName("Parser should prioritize CLI arguments over properties for group selection")
        void parseWhenCliAndPropertiesConflictThenCliWins() throws ParseException {
            // Given
            final OptionGroup group = new OptionGroup();
            group.addOption(new Option("f", "file", false, "file to process"));
            group.addOption(new Option("d", "directory", false, "directory to process"));
            final Options options = new Options().addOptionGroup(group);

            final String[] args = {"-f"};
            final Properties properties = new Properties();
            properties.setProperty("d", "true");

            // When
            final CommandLine cl = parser.parse(options, args, properties);

            // Then
            assertTrue(cl.hasOption("f"), "CLI-provided option '-f' should be selected");
            assertFalse(cl.hasOption("d"), "Property-provided option '-d' should be ignored");
        }

        @Test
        @DisplayName("Parser should handle long-only options in a group")
        void parseWhenLongOnlyOptionsInGroupThenSucceed() throws ParseException {
            // Given
            final OptionGroup group = new OptionGroup();
            group.addOption(new Option(null, "import", false, "import data"));
            group.addOption(new Option(null, "export", false, "export data"));
            final Options options = new Options().addOptionGroup(group);

            // When
            final CommandLine cl1 = parser.parse(options, new String[]{"--import"});
            final CommandLine cl2 = parser.parse(options, new String[]{"--export"});

            // Then
            assertTrue(cl1.hasOption("import"));
            assertFalse(cl1.hasOption("export"));

            assertTrue(cl2.hasOption("export"));
            assertFalse(cl2.hasOption("import"));
        }

        @Test
        @DisplayName("Parser should handle positional arguments when no options are given")
        void parseWhenNoOptionsThenHandlePositionalArgs() throws ParseException {
            // Given
            final Options options = new Options();
            final String[] args = {"arg1", "arg2"};

            // When
            final CommandLine cl = parser.parse(options, args);

            // Then
            assertEquals(2, cl.getArgList().size());
            assertEquals("arg1", cl.getArgList().get(0));
            assertEquals("arg2", cl.getArgList().get(1));
        }
    }
}