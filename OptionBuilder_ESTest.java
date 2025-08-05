package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class OptionBuilder_ESTest extends OptionBuilder_ESTest_scaffolding {

    // ================== TESTS FOR OPTION CREATION ==================
    
    @Test(timeout = 4000)
    public void createOptionWithLongOptAndArgName() {
        OptionBuilder.withLongOpt("=09QQ7 7&");
        OptionBuilder.withArgName("=09QQ7 7&");
        Option option = OptionBuilder.create();
        assertEquals(-1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void createOptionWithLongOptAndZeroArgs() {
        OptionBuilder.withLongOpt(":}gwm &");
        OptionBuilder.hasArgs(0);
        Option option = OptionBuilder.create();
        assertEquals(0, option.getArgs());
    }

    @Test(timeout = 4000)
    public void createOptionWithRequiredAndLongOpt() {
        OptionBuilder.isRequired();
        OptionBuilder.withLongOpt("");
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
        assertEquals(-1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void createOptionWithDefaultValueSeparatorAndLongOpt() {
        OptionBuilder.withValueSeparator();
        OptionBuilder.withLongOpt("");
        Option option = OptionBuilder.create();
        assertEquals('=', option.getValueSeparator());
        assertEquals(-1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void createOptionWithOptionalArgAndLongOpt() {
        OptionBuilder.hasOptionalArg();
        OptionBuilder.withLongOpt("[ARG...]");
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }

    // ================== TESTS FOR OPTION CREATION (STRING INPUT) ==================
    
    @Test(timeout = 4000)
    public void createOptionAfterSettingRequired() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create("converterMap");
        assertEquals(-1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void createOptionWithValueSeparator() {
        OptionBuilder.withValueSeparator('>');
        Option option = OptionBuilder.create(null);
        assertEquals('>', option.getValueSeparator());
        assertEquals(-1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void createOptionWithLongOpt() {
        OptionBuilder.withLongOpt("5");
        Option option = OptionBuilder.create("5");
        assertEquals(-1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void createOptionWithArgName() {
        OptionBuilder.withArgName("converterMap");
        Option option = OptionBuilder.create("converterMap");
        assertEquals(-1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void createOptionWithOptionalArgsZero() {
        OptionBuilder.hasOptionalArgs(0);
        Option option = OptionBuilder.create(null);
        assertEquals(0, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test(timeout = 4000)
    public void createOptionWithMultipleArgs() {
        OptionBuilder.hasArgs(235);
        Option option = OptionBuilder.create(null);
        assertEquals(235, option.getArgs());
    }

    // ================== TESTS FOR OPTION CREATION (CHAR INPUT) ==================
    
    @Test(timeout = 4000)
    public void createCharOptionWithValueSeparator() {
        OptionBuilder.withValueSeparator('S');
        Option option = OptionBuilder.create('S');
        assertEquals(-1, option.getArgs());
        assertEquals('S', option.getValueSeparator());
        assertEquals("S", option.getKey());
    }

    @Test(timeout = 4000)
    public void createCharOptionWithLongOpt() {
        OptionBuilder.withLongOpt("");
        Option option = OptionBuilder.create('R');
        assertEquals(-1, option.getArgs());
        assertEquals("R", option.getKey());
    }

    @Test(timeout = 4000)
    public void createCharOptionWithArgName() {
        OptionBuilder.withArgName("org.apache.commons.cli.Converter");
        Option option = OptionBuilder.create('S');
        assertEquals(-1, option.getArgs());
        assertEquals("S", option.getOpt());
    }

    @Test(timeout = 4000)
    public void createCharOptionWithOptionalArgsZero() {
        OptionBuilder.hasOptionalArgs(0);
        Option option = OptionBuilder.create('w');
        assertTrue(option.hasOptionalArg());
        assertEquals(119, option.getId());
        assertEquals(0, option.getArgs());
    }

    @Test(timeout = 4000)
    public void createCharOptionWithHasArgTrue() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create('S');
        assertEquals(1, option.getArgs());
        assertEquals(83, option.getId());
    }

    @Test(timeout = 4000)
    public void createCharOptionWithUnlimitedOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create('7');
        assertTrue(option.hasOptionalArg());
        assertEquals(-2, option.getArgs());
        assertEquals(55, option.getId());
    }

    @Test(timeout = 4000)
    public void createCharOptionWithRequired() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create('');
        assertEquals(-1, option.getArgs());
        assertEquals(127, option.getId());
    }

    // ================== TESTS FOR OPTION CREATION (EXCEPTION CASES) ==================
    
    @Test(timeout = 4000)
    public void createOptionWithInvalidCharThrowsException() {
        try {
            OptionBuilder.create(']');
            fail("Expected IllegalArgumentException for invalid option name");
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal option name ']'", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void createOptionWithEmptyStringThrowsException() {
        try {
            OptionBuilder.create("");
            fail("Expected IllegalArgumentException for empty option name");
        } catch (IllegalArgumentException e) {
            assertEquals("Empty option name", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void createWithoutLongOptThrowsException() {
        try {
            OptionBuilder.create();
            fail("Expected IllegalArgumentException when long option not specified");
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }

    // ================== TESTS FOR BUILDER CONFIGURATION METHODS ==================
    
    @Test(timeout = 4000)
    public void withTypeUsingClassObject() {
        Class<String> stringClass = String.class;
        OptionBuilder builder = OptionBuilder.withType(stringClass);
        assertNotNull(builder);
    }

    @Test(timeout = 4000)
    public void hasArgFalse() {
        OptionBuilder builder = OptionBuilder.hasArg(false);
        assertNotNull(builder);
    }

    @Test(timeout = 4000)
    public void withDescription() {
        OptionBuilder builder = OptionBuilder.withDescription(":}gwm &");
        assertNotNull(builder);
    }

    @Test(timeout = 4000)
    public void withTypeUsingClass() {
        Class<Object> objectClass = Object.class;
        OptionBuilder builder = OptionBuilder.withType(objectClass);
        assertNotNull(builder);
    }

    @Test(timeout = 4000)
    public void hasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        assertNotNull(builder);
    }

    @Test(timeout = 4000)
    public void createOptionWithUnlimitedArgsAndLongOpt() {
        OptionBuilder.hasArgs();
        OptionBuilder.withLongOpt("");
        Option option = OptionBuilder.create();
        assertEquals(-2, option.getArgs());
    }

    // ================== TESTS FOR TYPE HANDLING ==================
    
    @Test(timeout = 4000)
    public void withTypeUsingNonClassObjectThrowsException() {
        try {
            OptionBuilder.withType((Object) "");
            fail("Expected ClassCastException when using non-Class type");
        } catch (ClassCastException e) {
            assertTrue(e.getMessage().contains("java.lang.String cannot be cast to java.lang.Class"));
        }
    }
}