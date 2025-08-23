package org.apache.commons.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for OptionBuilder.
 *
 * Notes:
 * - OptionBuilder is a static, stateful builder. To keep tests isolated and deterministic,
 *   we call its private reset() method via reflection before each test.
 * - Tests are organized by feature to make intent clear.
 */
public class OptionBuilderTest {

    @Before
    public void resetBuilderBefore() throws Exception {
        resetOptionBuilder();
    }

    @After
    public void resetBuilderAfter() throws Exception {
        resetOptionBuilder();
    }

    private static void resetOptionBuilder() throws Exception {
        Method m = OptionBuilder.class.getDeclaredMethod("reset");
        m.setAccessible(true);
        m.invoke(null);
    }

    // withType(Class) and withType(Object)

    @Test
    public void withType_acceptsClass_returnsBuilderAndSetsTypeOnOption() {
        OptionBuilder.withType(String.class);
        Option opt = OptionBuilder.withLongOpt("name").create();

        assertNotNull(opt);
        assertEquals(String.class, opt.getType());
    }

    @Test
    public void withType_objectOverload_acceptsClassInstance() {
        OptionBuilder.withType((Object) Integer.class);
        Option opt = OptionBuilder.withLongOpt("count").create();

        assertNotNull(opt);
        assertEquals(Integer.class, opt.getType());
    }

    @Test
    public void withType_objectOverload_rejectsNonClass() {
        try {
            OptionBuilder.withType((Object) "not a class");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            assertTrue(e.getMessage().contains("cannot be cast to java.lang.Class"));
        }
    }

    // Required flag

    @Test
    public void isRequired_true_marksOptionAsRequired_forShortOption() {
        OptionBuilder.isRequired(true);
        Option opt = OptionBuilder.create('x');

        assertTrue(opt.isRequired());
        assertEquals(-1, opt.getArgs());
    }

    @Test
    public void isRequired_withoutArgs_marksOptionAsRequired_forLongOption() {
        OptionBuilder.isRequired();
        Option opt = OptionBuilder.withLongOpt("verbose").create();

        assertTrue(opt.isRequired());
        assertEquals(-1, opt.getArgs());
    }

    // Value separator

    @Test
    public void withValueSeparator_defaultIsEqualsSign() {
        OptionBuilder.withValueSeparator();
        Option opt = OptionBuilder.withLongOpt("D").create();

        assertEquals('=', opt.getValueSeparator());
        assertEquals(-1, opt.getArgs());
    }

    @Test
    public void withValueSeparator_customCharacterIsApplied() {
        OptionBuilder.withValueSeparator('>');
        Option opt = OptionBuilder.create((String) null);

        assertEquals('>', opt.getValueSeparator());
        assertEquals(-1, opt.getArgs());
    }

    // Argument count: required args

    @Test
    public void hasArg_true_requiresExactlyOneArgument() {
        OptionBuilder.hasArg(true);
        Option opt = OptionBuilder.create('S');

        assertEquals(1, opt.getArgs());
        assertFalse(opt.hasOptionalArg());
        assertEquals(83, opt.getId()); // 'S'
    }

    @Test
    public void hasArgs_specificNumber_setsExactCount() {
        OptionBuilder.hasArgs(235);
        Option opt = OptionBuilder.create((String) null);

        assertEquals(235, opt.getArgs());
    }

    @Test
    public void hasArgs_unlimited_allowsUnlimitedValues() {
        OptionBuilder.hasArgs();
        Option opt = OptionBuilder.withLongOpt("list").create();

        assertEquals(-2, opt.getArgs()); // unlimited
    }

    // Argument count: optional args

    @Test
    public void hasOptionalArg_singleOptionalArgument() {
        OptionBuilder.hasOptionalArg();
        Option opt = OptionBuilder.withLongOpt("maybe").create();

        assertTrue(opt.hasOptionalArg());
        assertEquals(1, opt.getArgs());
    }

    @Test
    public void hasOptionalArgs_unlimitedOptionalArguments() {
        OptionBuilder.hasOptionalArgs();
        Option opt = OptionBuilder.create('7');

        assertTrue(opt.hasOptionalArg());
        assertEquals(-2, opt.getArgs()); // unlimited
        assertEquals(55, opt.getId()); // '7'
    }

    @Test
    public void hasOptionalArgs_zeroOptionalArguments() {
        OptionBuilder.hasOptionalArgs(0);
        Option opt = OptionBuilder.create('w');

        assertTrue(opt.hasOptionalArg());
        assertEquals(0, opt.getArgs());
        assertEquals(119, opt.getId()); // 'w'
    }

    // Long option, arg name, description

    @Test
    public void withArgName_and_withDescription_applyToCreatedOption() {
        OptionBuilder.withArgName("FILE");
        OptionBuilder.withDescription("input file to process");
        Option opt = OptionBuilder.create('f');

        assertEquals("FILE", opt.getArgName());
        assertEquals("input file to process", opt.getDescription());
    }

    @Test
    public void withLongOpt_allowsEmptyString() {
        Option opt = OptionBuilder.withLongOpt("").create();

        assertNotNull(opt);
        assertEquals("", opt.getLongOpt());
        assertEquals(-1, opt.getArgs());
    }

    // Validation and error cases

    @Test
    public void create_withoutLongOpt_throws() {
        try {
            OptionBuilder.create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().toLowerCase().contains("must specify longopt"));
        }
    }

    @Test
    public void create_withEmptyShortName_throws() {
        try {
            OptionBuilder.create("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Empty option name"));
        }
    }

    @Test
    public void create_withIllegalShortChar_throws() {
        try {
            OptionBuilder.create(']');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Illegal option name"));
        }
    }

    // Miscellaneous

    @Test
    public void create_shortOption_preservesKeyAndCustomValueSeparator() {
        OptionBuilder.withValueSeparator('S');
        Option opt = OptionBuilder.create('S');

        assertEquals("S", opt.getKey());
        assertEquals('S', opt.getValueSeparator());
        assertEquals(-1, opt.getArgs());
    }
}