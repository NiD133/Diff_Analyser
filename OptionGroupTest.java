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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation") // tests some deprecated classes (PosixParser, OptionBuilder)
class OptionGroupTest {

    // Short option constants
    private static final String OPT_R = "r";
    private static final String OPT_F = "f";
    private static final String OPT_D = "d";
    private static final String OPT_S = "s";
    private static final String OPT_C = "c";

    // Long-only option constants (no short names)
    private static final String OPT_IMPORT = "import";
    private static final String OPT_EXPORT = "export";

    // Universe of all options used in this test suite
    private static final Set<String> ALL_OPTION_NAMES = new HashSet<>(
            Arrays.asList(OPT_R, OPT_F, OPT_D, OPT_S, OPT_C, OPT_IMPORT, OPT_EXPORT));

    private Options options;
    private final Parser parser = new PosixParser();

    /*
     Test topology:

     - Group 1 (mutually exclusive): -f/--file OR -d/--directory
     - Group 2 (mutually exclusive): -s/--section OR -c/--chapter
     - Group 3 (mutually exclusive): --import OR --export (long-only options)
     - Independent option         : -r/--revision
     */
    @BeforeEach
    void setUp() {
        options = new Options();

        // Group 1: file vs directory
        options.addOptionGroup(group(
                new Option(OPT_F, "file", false, "file to process"),
                new Option(OPT_D, "directory", false, "directory to process")));

        // Group 2: section vs chapter
        options.addOptionGroup(group(
                new Option(OPT_S, "section", false, "section to process"),
                new Option(OPT_C, "chapter", false, "chapter to process")));

        // Group 3: import vs export (long-only)
        options.addOptionGroup(group(
                new Option(null, OPT_IMPORT, false, "section to process"),
                new Option(null, OPT_EXPORT, false, "chapter to process")));

        // Independent option
        options.addOption(OPT_R, "revision", false, "revision number");
    }

    // ----------------------- Helpers -----------------------

    private static OptionGroup group(final Option... opts) {
        final OptionGroup g = new OptionGroup();
        for (final Option o : opts) {
            g.addOption(o);
        }
        return g;
    }

    private CommandLine parse(final String... args) throws Exception {
        return parser.parse(options, args);
    }

    private CommandLine parse(final String[] args, final Properties props) throws Exception {
        return parser.parse(options, args, props);
    }

    /**
     * Asserts that only the provided options are set in the parsed command line.
     * Any option not listed in expectedSet must be absent.
     */
    private static void assertOnlyOptionsSet(final CommandLine cl, final String... expectedSet) {
        final Set<String> expected = new HashSet<>(Arrays.asList(expectedSet));
        for (final String name : ALL_OPTION_NAMES) {
            if (expected.contains(name)) {
                assertTrue(cl.hasOption(name), "Expected option '" + name + "' to be set");
            } else {
                assertFalse(cl.hasOption(name), "Expected option '" + name + "' to be NOT set");
            }
        }
    }

    // ----------------------- Tests -----------------------

    @Test
    void testGetNames() {
        final OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isSelected(), "No option should be selected by default");

        // Using deprecated OptionBuilder to mirror legacy usage covered by this suite
        optionGroup.addOption(OptionBuilder.create('a'));
        optionGroup.addOption(OptionBuilder.create('b'));

        final Collection<String> names = optionGroup.getNames();
        assertNotNull(names, "Group names must not be null");
        assertEquals(2, names.size(), "Exactly two names are expected");
        assertTrue(names.contains("a"), "Name 'a' should be present");
        assertTrue(names.contains("b"), "Name 'b' should be present");
    }

    @Test
    void testNoOptionsExtraArgs() throws Exception {
        final CommandLine cl = parse("arg1", "arg2");
        assertOnlyOptionsSet(cl /* none expected */);
        assertEquals(2, cl.getArgList().size(), "Confirm TWO extra args");
    }

    @Test
    void testSingleLongOption() throws Exception {
        final CommandLine cl = parse("--file");
        assertOnlyOptionsSet(cl, OPT_F);
        assertTrue(cl.getArgList().isEmpty(), "Confirm no extra args");
    }

    @Test
    void testSingleOption() throws Exception {
        final CommandLine cl = parse("-r");
        assertOnlyOptionsSet(cl, OPT_R);
        assertTrue(cl.getArgList().isEmpty(), "Confirm no extra args");
    }

    @Test
    void testSingleOptionFromGroup() throws Exception {
        final CommandLine cl = parse("-f");
        assertOnlyOptionsSet(cl, OPT_F);
        assertTrue(cl.getArgList().isEmpty(), "Confirm no extra args");
    }

    @Test
    void testToString() {
        // Long-only options
        final OptionGroup longOnly = new OptionGroup();
        longOnly.addOption(new Option(null, "foo", false, "Foo"));
        longOnly.addOption(new Option(null, "bar", false, "Bar"));
        final String longOnlyStr = longOnly.toString();
        assertTrue("[--bar Bar, --foo Foo]".equals(longOnlyStr) || "[--foo Foo, --bar Bar]".equals(longOnlyStr),
                "Unexpected OptionGroup toString: " + longOnlyStr);

        // Short options with long names
        final OptionGroup shortOpts = new OptionGroup();
        shortOpts.addOption(new Option("f", "foo", false, "Foo"));
        shortOpts.addOption(new Option("b", "bar", false, "Bar"));
        final String shortOptsStr = shortOpts.toString();
        assertTrue("[-b Bar, -f Foo]".equals(shortOptsStr) || "[-f Foo, -b Bar]".equals(shortOptsStr),
                "Unexpected OptionGroup toString: " + shortOptsStr);
    }

    @Test
    void testTwoLongOptionsFromGroup() {
        final String[] args = {"--file", "--directory"};
        final AlreadySelectedException e =
                assertThrows(AlreadySelectedException.class, () -> parse(args));
        assertNotNull(e.getOptionGroup(), "Exception should reference an OptionGroup");
        assertTrue(e.getOptionGroup().isSelected(), "OptionGroup should be marked as selected");
        assertEquals(OPT_F, e.getOptionGroup().getSelected(), "First selected option should be 'f'");
        assertEquals(OPT_D, e.getOption().getOpt(), "Conflicting option should be 'd'");
    }

    @Test
    void testTwoOptionsFromDifferentGroup() throws Exception {
        final CommandLine cl = parse("-f", "-s");
        assertOnlyOptionsSet(cl, OPT_F, OPT_S);
        assertTrue(cl.getArgList().isEmpty(), "Confirm NO extra args");
    }

    @Test
    void testTwoOptionsFromGroup() {
        final String[] args = {"-f", "-d"};
        final AlreadySelectedException e =
                assertThrows(AlreadySelectedException.class, () -> parse(args));
        assertNotNull(e.getOptionGroup(), "Exception should reference an OptionGroup");
        assertTrue(e.getOptionGroup().isSelected(), "OptionGroup should be marked as selected");
        assertEquals(OPT_F, e.getOptionGroup().getSelected(), "First selected option should be 'f'");
        assertEquals(OPT_D, e.getOption().getOpt(), "Conflicting option should be 'd'");
    }

    @Test
    void testTwoOptionsFromGroupWithProperties() throws Exception {
        // Args pick -f, properties try to add -d (same group) → properties must NOT override
        final Properties properties = new Properties();
        properties.put(OPT_D, "true");

        final CommandLine cl = parse(new String[] {"-f"}, properties);
        assertTrue(cl.hasOption(OPT_F), "Option 'f' should remain set from args");
        assertFalse(cl.hasOption(OPT_D), "Conflicting option 'd' from properties must be ignored");
    }

    @Test
    void testTwoValidLongOptions() throws Exception {
        final CommandLine cl = parse("--revision", "--file");
        assertOnlyOptionsSet(cl, OPT_R, OPT_F);
        assertTrue(cl.getArgList().isEmpty(), "Confirm no extra args");
    }

    @Test
    void testTwoValidOptions() throws Exception {
        final CommandLine cl = parse("-r", "-f");
        assertOnlyOptionsSet(cl, OPT_R, OPT_F);
        assertTrue(cl.getArgList().isEmpty(), "Confirm no extra args");
    }

    @Test
    void testValidLongOnlyOptions() throws Exception {
        final CommandLine clExport = parse("--export");
        assertOnlyOptionsSet(clExport, OPT_EXPORT);

        final CommandLine clImport = parse("--import");
        assertOnlyOptionsSet(clImport, OPT_IMPORT);
    }
}