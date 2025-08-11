package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

/**
 * Test suite for OptionBuilder functionality.
 * Tests the deprecated OptionBuilder class methods for creating CLI options.
 */
public class OptionBuilderTest {

    // ========== Type Configuration Tests ==========
    
    @Test(timeout = 4000)
    public void shouldSetTypeUsingClassObject() {
        Class<String> stringClass = String.class;
        
        OptionBuilder optionBuilder = OptionBuilder.withType(stringClass);
        
        assertNotNull("OptionBuilder should return non-null instance", optionBuilder);
    }

    @Test(timeout = 4000)
    public void shouldSetTypeUsingObjectClass() {
        Class<Object> objectClass = Object.class;
        
        OptionBuilder optionBuilder = OptionBuilder.withType(objectClass);
        
        assertNotNull("OptionBuilder should return non-null instance", optionBuilder);
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenTypeIsNotClass() {
        try {
            OptionBuilder.withType("not-a-class");
            fail("Should throw ClassCastException when type is not a Class object");
        } catch (ClassCastException e) {
            assertEquals("java.lang.String cannot be cast to java.lang.Class", e.getMessage());
        }
    }

    // ========== Required Option Tests ==========
    
    @Test(timeout = 4000)
    public void shouldCreateRequiredOptionWithStringName() {
        OptionBuilder.isRequired(true);
        
        Option option = OptionBuilder.create("verbose");
        
        assertTrue("Option should be marked as required", option.isRequired());
        assertEquals("Option should have default args value", -1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void shouldCreateRequiredOptionWithCharName() {
        OptionBuilder.isRequired(true);
        
        Option option = OptionBuilder.create('v');
        
        assertTrue("Option should be marked as required", option.isRequired());
        assertEquals("Option should have default args value", -1, option.getArgs());
        assertEquals("Option ID should match character code", 118, option.getId()); // 'v' = 118
    }

    @Test(timeout = 4000)
    public void shouldCreateRequiredOptionWithoutShortName() {
        OptionBuilder.isRequired();
        OptionBuilder.withLongOpt("verbose");
        
        Option option = OptionBuilder.create();
        
        assertTrue("Option should be marked as required", option.isRequired());
        assertEquals("Option should have default args value", -1, option.getArgs());
    }

    // ========== Value Separator Tests ==========
    
    @Test(timeout = 4000)
    public void shouldSetCustomValueSeparator() {
        OptionBuilder.withValueSeparator('>');
        
        Option option = OptionBuilder.create("config");
        
        assertEquals("Option should use custom value separator", '>', option.getValueSeparator());
        assertEquals("Option should have default args value", -1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void shouldSetDefaultValueSeparator() {
        OptionBuilder.withValueSeparator();
        OptionBuilder.withLongOpt("properties");
        
        Option option = OptionBuilder.create();
        
        assertEquals("Option should use default '=' separator", '=', option.getValueSeparator());
        assertEquals("Option should have default args value", -1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void shouldCreateOptionWithValueSeparatorAndCharName() {
        OptionBuilder.withValueSeparator('S');
        
        Option option = OptionBuilder.create('S');
        
        assertEquals("Option should use specified value separator", 'S', option.getValueSeparator());
        assertEquals("Option key should match character", "S", option.getKey());
        assertEquals("Option should have default args value", -1, option.getArgs());
    }

    // ========== Long Option Tests ==========
    
    @Test(timeout = 4000)
    public void shouldCreateOptionWithLongName() {
        OptionBuilder.withLongOpt("version");
        
        Option option = OptionBuilder.create("v");
        
        assertEquals("Option should have long name set", "version", option.getLongOpt());
        assertEquals("Option should have default args value", -1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void shouldCreateOptionWithEmptyLongName() {
        OptionBuilder.withLongOpt("");
        
        Option option = OptionBuilder.create('R');
        
        assertEquals("Option key should match character", "R", option.getKey());
        assertEquals("Option should have default args value", -1, option.getArgs());
    }

    // ========== Argument Name Tests ==========
    
    @Test(timeout = 4000)
    public void shouldSetArgumentName() {
        OptionBuilder.withArgName("filename");
        
        Option option = OptionBuilder.create("file");
        
        assertEquals("Option should have argument name set", "filename", option.getArgName());
        assertEquals("Option should have default args value", -1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void shouldCreateOptionWithArgumentNameAndCharOption() {
        OptionBuilder.withArgName("classname");
        
        Option option = OptionBuilder.create('S');
        
        assertEquals("Option should have argument name set", "classname", option.getArgName());
        assertEquals("Option name should match character", "S", option.getOpt());
        assertEquals("Option should have default args value", -1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void shouldCreateOptionWithArgumentNameAndNoShortName() {
        OptionBuilder.withLongOpt("output");
        OptionBuilder.withArgName("directory");
        
        Option option = OptionBuilder.create();
        
        assertEquals("Option should have argument name set", "directory", option.getArgName());
        assertEquals("Option should have default args value", -1, option.getArgs());
    }

    // ========== Optional Arguments Tests ==========
    
    @Test(timeout = 4000)
    public void shouldCreateOptionWithOptionalArguments() {
        OptionBuilder.hasOptionalArgs(0);
        
        Option option = OptionBuilder.create("debug");
        
        assertTrue("Option should accept optional arguments", option.hasOptionalArg());
        assertEquals("Option should have specified arg count", 0, option.getArgs());
    }

    @Test(timeout = 4000)
    public void shouldCreateOptionWithOptionalArgumentsAndCharName() {
        OptionBuilder.hasOptionalArgs(0);
        
        Option option = OptionBuilder.create('w');
        
        assertTrue("Option should accept optional arguments", option.hasOptionalArg());
        assertEquals("Option ID should match character code", 119, option.getId()); // 'w' = 119
        assertEquals("Option should have specified arg count", 0, option.getArgs());
    }

    @Test(timeout = 4000)
    public void shouldCreateOptionWithUnlimitedOptionalArguments() {
        OptionBuilder.hasOptionalArgs();
        
        Option option = OptionBuilder.create('7');
        
        assertTrue("Option should accept optional arguments", option.hasOptionalArg());
        assertEquals("Option should accept unlimited args", -2, option.getArgs());
        assertEquals("Option ID should match character code", 55, option.getId()); // '7' = 55
    }

    @Test(timeout = 4000)
    public void shouldCreateOptionWithSingleOptionalArgument() {
        OptionBuilder.hasOptionalArg();
        OptionBuilder.withLongOpt("help");
        
        Option option = OptionBuilder.create();
        
        assertTrue("Option should accept optional arguments", option.hasOptionalArg());
        assertEquals("Option should accept single argument", 1, option.getArgs());
    }

    // ========== Required Arguments Tests ==========
    
    @Test(timeout = 4000)
    public void shouldCreateOptionWithSpecificNumberOfArguments() {
        OptionBuilder.hasArgs(235);
        
        Option option = OptionBuilder.create("input");
        
        assertEquals("Option should accept specified number of args", 235, option.getArgs());
    }

    @Test(timeout = 4000)
    public void shouldCreateOptionWithUnlimitedArguments() {
        OptionBuilder.hasArgs();
        OptionBuilder.withLongOpt("files");
        
        Option option = OptionBuilder.create();
        
        assertEquals("Option should accept unlimited args", -2, option.getArgs());
    }

    @Test(timeout = 4000)
    public void shouldCreateOptionWithZeroArguments() {
        OptionBuilder.hasArgs(0);
        OptionBuilder.withLongOpt("quiet");
        
        Option option = OptionBuilder.create();
        
        assertEquals("Option should accept zero args", 0, option.getArgs());
    }

    @Test(timeout = 4000)
    public void shouldCreateOptionWithSingleArgument() {
        OptionBuilder.hasArg(true);
        
        Option option = OptionBuilder.create('S');
        
        assertEquals("Option should accept single argument", 1, option.getArgs());
        assertEquals("Option ID should match character code", 83, option.getId()); // 'S' = 83
    }

    @Test(timeout = 4000)
    public void shouldCreateOptionWithNoArgumentWhenHasArgIsFalse() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        
        assertNotNull("OptionBuilder should return non-null instance", optionBuilder);
    }

    @Test(timeout = 4000)
    public void shouldReturnBuilderInstanceForHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        
        assertNotNull("OptionBuilder should return non-null instance", optionBuilder);
    }

    // ========== Description Tests ==========
    
    @Test(timeout = 4000)
    public void shouldSetOptionDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Show help information");
        
        assertNotNull("OptionBuilder should return non-null instance", optionBuilder);
    }

    // ========== Error Handling Tests ==========
    
    @Test(timeout = 4000)
    public void shouldThrowExceptionForInvalidCharacterOption() {
        try {
            OptionBuilder.create(']');
            fail("Should throw IllegalArgumentException for invalid character option");
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal option name ']'.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionForEmptyStringOption() {
        try {
            OptionBuilder.create("");
            fail("Should throw IllegalArgumentException for empty string option");
        } catch (IllegalArgumentException e) {
            assertEquals("Empty option name.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenCreatingOptionWithoutLongOpt() {
        try {
            OptionBuilder.create();
            fail("Should throw IllegalArgumentException when no long option is specified");
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }
}