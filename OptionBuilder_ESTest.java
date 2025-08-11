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

    @Test(timeout = 4000)
    public void testOptionBuilderWithType() throws Throwable {
        Class<String> stringClass = String.class;
        OptionBuilder optionBuilder = OptionBuilder.withType((Object) stringClass);
        assertNotNull(optionBuilder);
    }

    @Test(timeout = 4000)
    public void testCreateRequiredOption() throws Throwable {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create("converterMap");
        assertEquals((-1), option.getArgs());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithValueSeparator() throws Throwable {
        OptionBuilder.withValueSeparator('>');
        Option option = OptionBuilder.create((String) null);
        assertEquals('>', option.getValueSeparator());
        assertEquals((-1), option.getArgs());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithLongOpt() throws Throwable {
        OptionBuilder.withLongOpt("5");
        Option option = OptionBuilder.create("5");
        assertEquals((-1), option.getArgs());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithArgName() throws Throwable {
        OptionBuilder.withArgName("converterMap");
        Option option = OptionBuilder.create("converterMap");
        assertEquals((-1), option.getArgs());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithOptionalArgs() throws Throwable {
        OptionBuilder.hasOptionalArgs(0);
        Option option = OptionBuilder.create((String) null);
        assertEquals(0, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithMultipleArgs() throws Throwable {
        OptionBuilder.hasArgs(235);
        Option option = OptionBuilder.create((String) null);
        assertEquals(235, option.getArgs());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithValueSeparatorAndKey() throws Throwable {
        OptionBuilder.withValueSeparator('S');
        Option option = OptionBuilder.create('S');
        assertEquals((-1), option.getArgs());
        assertEquals('S', option.getValueSeparator());
        assertEquals("S", option.getKey());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithEmptyLongOpt() throws Throwable {
        OptionBuilder.withLongOpt("");
        Option option = OptionBuilder.create('R');
        assertEquals((-1), option.getArgs());
        assertEquals("R", option.getKey());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithArgNameAndOpt() throws Throwable {
        OptionBuilder.withArgName("org.apache.commons.cli.Converter");
        Option option = OptionBuilder.create('S');
        assertEquals((-1), option.getArgs());
        assertEquals("S", option.getOpt());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithOptionalArgsAndId() throws Throwable {
        OptionBuilder.hasOptionalArgs(0);
        Option option = OptionBuilder.create('w');
        assertTrue(option.hasOptionalArg());
        assertEquals(119, option.getId());
        assertEquals(0, option.getArgs());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithEmptyLongOptAndArgName() throws Throwable {
        OptionBuilder.withLongOpt("");
        OptionBuilder.withArgName("=09QQ7 7&");
        Option option = OptionBuilder.create();
        assertEquals((-1), option.getArgs());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithLongOptAndArgs() throws Throwable {
        OptionBuilder.withLongOpt(":}gwm &");
        OptionBuilder.hasArgs(0);
        Option option = OptionBuilder.create();
        assertEquals(0, option.getArgs());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithInvalidCharacter() throws Throwable {
        try {
            OptionBuilder.create(']');
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.cli.OptionValidator", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithEmptyName() throws Throwable {
        try {
            OptionBuilder.create("");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.cli.OptionValidator", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithSingleArg() throws Throwable {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create('S');
        assertEquals(1, option.getArgs());
        assertEquals(83, option.getId());
    }

    @Test(timeout = 4000)
    public void testOptionBuilderHasArgFalse() throws Throwable {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertNotNull(optionBuilder);
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithoutLongOpt() throws Throwable {
        try {
            OptionBuilder.create();
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.cli.OptionBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testOptionBuilderWithDescription() throws Throwable {
        OptionBuilder optionBuilder = OptionBuilder.withDescription(":}gwm &");
        assertNotNull(optionBuilder);
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithUnlimitedOptionalArgs() throws Throwable {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create('7');
        assertTrue(option.hasOptionalArg());
        assertEquals((-2), option.getArgs());
        assertEquals(55, option.getId());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithLongOptAndUnlimitedArgs() throws Throwable {
        OptionBuilder.hasArgs();
        OptionBuilder.withLongOpt("");
        Option option = OptionBuilder.create();
        assertEquals((-2), option.getArgs());
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithValueSeparatorAndLongOpt() throws Throwable {
        OptionBuilder.withValueSeparator();
        OptionBuilder.withLongOpt("");
        Option option = OptionBuilder.create();
        assertEquals('=', option.getValueSeparator());
        assertEquals((-1), option.getArgs());
    }

    @Test(timeout = 4000)
    public void testOptionBuilderWithTypeObject() throws Throwable {
        Class<Object> objectClass = Object.class;
        OptionBuilder optionBuilder = OptionBuilder.withType(objectClass);
        assertNotNull(optionBuilder);
    }

    @Test(timeout = 4000)
    public void testCreateOptionWithOptionalArgAndLongOpt() throws Throwable {
        OptionBuilder.hasOptionalArg();
        OptionBuilder.withLongOpt("[ARG...]");
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }

    @Test(timeout = 4000)
    public void testCreateRequiredOptionWithChar() throws Throwable {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create('');
        assertEquals((-1), option.getArgs());
        assertEquals(127, option.getId());
    }

    @Test(timeout = 4000)
    public void testOptionBuilderHasArg() throws Throwable {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertNotNull(optionBuilder);
    }

    @Test(timeout = 4000)
    public void testCreateRequiredOptionWithEmptyLongOpt() throws Throwable {
        OptionBuilder.isRequired();
        OptionBuilder.withLongOpt("");
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
        assertEquals((-1), option.getArgs());
    }

    @Test(timeout = 4000)
    public void testOptionBuilderWithInvalidType() throws Throwable {
        try {
            OptionBuilder.withType((Object) "");
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.apache.commons.cli.OptionBuilder", e);
        }
    }
}