package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the OptionGroup class.
 */
@SuppressWarnings("deprecation") // tests some deprecated classes
class OptionGroupTest {

    private Options options;
    private final Parser parser = new PosixParser();

    @BeforeEach
    public void setUp() {
        // Initialize option groups
        options = new Options();
        options.addOptionGroup(createOptionGroup("f", "file", "d", "directory"));
        options.addOptionGroup(createOptionGroup("s", "section", "c", "chapter"));
        options.addOptionGroup(createOptionGroup(null, "import", null, "export"));
        
        // Add standalone option
        options.addOption("r", "revision", false, "revision number");
    }

    /**
     * Helper method to create an OptionGroup with two options.
     */
    private OptionGroup createOptionGroup(String opt1, String longOpt1, String opt2, String longOpt2) {
        Option option1 = new Option(opt1, longOpt1, false, longOpt1 + " to process");
        Option option2 = new Option(opt2, longOpt2, false, longOpt2 + " to process");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        return group;
    }

    @Test
    void testOptionGroupNames() {
        OptionGroup group = new OptionGroup();
        assertFalse(group.isSelected(), "Group should not be selected initially");
        group.addOption(OptionBuilder.create('a'));
        group.addOption(OptionBuilder.create('b'));
        
        assertNotNull(group.getNames(), "Option names should not be null");
        assertEquals(2, group.getNames().size(), "Group should contain two options");
        assertTrue(group.getNames().contains("a"), "Option 'a' should be in the group");
        assertTrue(group.getNames().contains("b"), "Option 'b' should be in the group");
    }

    @Test
    void testNoOptionsWithExtraArgs() throws Exception {
        String[] args = {"arg1", "arg2"};
        CommandLine cl = parser.parse(options, args);
        
        assertFalse(cl.hasOption("r"), "Option -r should not be set");
        assertFalse(cl.hasOption("f"), "Option -f should not be set");
        assertFalse(cl.hasOption("d"), "Option -d should not be set");
        assertFalse(cl.hasOption("s"), "Option -s should not be set");
        assertFalse(cl.hasOption("c"), "Option -c should not be set");
        assertEquals(2, cl.getArgList().size(), "There should be two extra arguments");
    }

    @Test
    void testSingleLongOption() throws Exception {
        String[] args = {"--file"};
        CommandLine cl = parser.parse(options, args);
        
        assertFalse(cl.hasOption("r"), "Option -r should not be set");
        assertTrue(cl.hasOption("f"), "Option -f should be set");
        assertFalse(cl.hasOption("d"), "Option -d should not be set");
        assertFalse(cl.hasOption("s"), "Option -s should not be set");
        assertFalse(cl.hasOption("c"), "Option -c should not be set");
        assertTrue(cl.getArgList().isEmpty(), "There should be no extra arguments");
    }

    @Test
    void testSingleOption() throws Exception {
        String[] args = {"-r"};
        CommandLine cl = parser.parse(options, args);
        
        assertTrue(cl.hasOption("r"), "Option -r should be set");
        assertFalse(cl.hasOption("f"), "Option -f should not be set");
        assertFalse(cl.hasOption("d"), "Option -d should not be set");
        assertFalse(cl.hasOption("s"), "Option -s should not be set");
        assertFalse(cl.hasOption("c"), "Option -c should not be set");
        assertTrue(cl.getArgList().isEmpty(), "There should be no extra arguments");
    }

    @Test
    void testSingleOptionFromGroup() throws Exception {
        String[] args = {"-f"};
        CommandLine cl = parser.parse(options, args);
        
        assertFalse(cl.hasOption("r"), "Option -r should not be set");
        assertTrue(cl.hasOption("f"), "Option -f should be set");
        assertFalse(cl.hasOption("d"), "Option -d should not be set");
        assertFalse(cl.hasOption("s"), "Option -s should not be set");
        assertFalse(cl.hasOption("c"), "Option -c should not be set");
        assertTrue(cl.getArgList().isEmpty(), "There should be no extra arguments");
    }

    @Test
    void testOptionGroupToString() {
        OptionGroup group1 = new OptionGroup();
        group1.addOption(new Option(null, "foo", false, "Foo"));
        group1.addOption(new Option(null, "bar", false, "Bar"));
        assertEquals("[--foo Foo, --bar Bar]", group1.toString(), "String representation mismatch");

        OptionGroup group2 = new OptionGroup();
        group2.addOption(new Option("f", "foo", false, "Foo"));
        group2.addOption(new Option("b", "bar", false, "Bar"));
        assertEquals("[-f Foo, -b Bar]", group2.toString(), "String representation mismatch");
    }

    @Test
    void testTwoLongOptionsFromSameGroup() {
        String[] args = {"--file", "--directory"};
        AlreadySelectedException e = assertThrows(AlreadySelectedException.class, () -> parser.parse(options, args));
        
        assertNotNull(e.getOptionGroup(), "Option group should not be null");
        assertTrue(e.getOptionGroup().isSelected(), "Option group should be selected");
        assertEquals("f", e.getOptionGroup().getSelected(), "Selected option mismatch");
        assertEquals("d", e.getOption().getOpt(), "Option mismatch");
    }

    @Test
    void testTwoOptionsFromDifferentGroups() throws Exception {
        String[] args = {"-f", "-s"};
        CommandLine cl = parser.parse(options, args);
        
        assertFalse(cl.hasOption("r"), "Option -r should not be set");
        assertTrue(cl.hasOption("f"), "Option -f should be set");
        assertFalse(cl.hasOption("d"), "Option -d should not be set");
        assertTrue(cl.hasOption("s"), "Option -s should be set");
        assertFalse(cl.hasOption("c"), "Option -c should not be set");
        assertTrue(cl.getArgList().isEmpty(), "There should be no extra arguments");
    }

    @Test
    void testTwoOptionsFromSameGroup() {
        String[] args = {"-f", "-d"};
        AlreadySelectedException e = assertThrows(AlreadySelectedException.class, () -> parser.parse(options, args));
        
        assertNotNull(e.getOptionGroup(), "Option group should not be null");
        assertTrue(e.getOptionGroup().isSelected(), "Option group should be selected");
        assertEquals("f", e.getOptionGroup().getSelected(), "Selected option mismatch");
        assertEquals("d", e.getOption().getOpt(), "Option mismatch");
    }

    @Test
    void testTwoOptionsFromGroupWithProperties() throws Exception {
        String[] args = {"-f"};
        Properties properties = new Properties();
        properties.put("d", "true");
        CommandLine cl = parser.parse(options, args, properties);
        
        assertTrue(cl.hasOption("f"), "Option -f should be set");
        assertFalse(cl.hasOption("d"), "Option -d should not be set");
    }

    @Test
    void testTwoValidLongOptions() throws Exception {
        String[] args = {"--revision", "--file"};
        CommandLine cl = parser.parse(options, args);
        
        assertTrue(cl.hasOption("r"), "Option -r should be set");
        assertTrue(cl.hasOption("f"), "Option -f should be set");
        assertFalse(cl.hasOption("d"), "Option -d should not be set");
        assertFalse(cl.hasOption("s"), "Option -s should not be set");
        assertFalse(cl.hasOption("c"), "Option -c should not be set");
        assertTrue(cl.getArgList().isEmpty(), "There should be no extra arguments");
    }

    @Test
    void testTwoValidOptions() throws Exception {
        String[] args = {"-r", "-f"};
        CommandLine cl = parser.parse(options, args);
        
        assertTrue(cl.hasOption("r"), "Option -r should be set");
        assertTrue(cl.hasOption("f"), "Option -f should be set");
        assertFalse(cl.hasOption("d"), "Option -d should not be set");
        assertFalse(cl.hasOption("s"), "Option -s should not be set");
        assertFalse(cl.hasOption("c"), "Option -c should not be set");
        assertTrue(cl.getArgList().isEmpty(), "There should be no extra arguments");
    }

    @Test
    void testValidLongOnlyOptions() throws Exception {
        CommandLine cl1 = parser.parse(options, new String[]{"--export"});
        assertTrue(cl1.hasOption("export"), "Option --export should be set");
        
        CommandLine cl2 = parser.parse(options, new String[]{"--import"});
        assertTrue(cl2.hasOption("import"), "Option --import should be set");
    }
}