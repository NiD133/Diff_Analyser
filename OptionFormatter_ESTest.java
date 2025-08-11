package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.function.BiFunction;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.help.OptionFormatter;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class OptionFormatter_ESTest extends OptionFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testLongOptPrefix() throws Throwable {
        Option option = new Option("?Wf", "?Wf", true, "?Wf");
        OptionFormatter optionFormatter = OptionFormatter.builder()
            .setLongOptPrefix("?Wf")
            .build(option);
        String longOpt = optionFormatter.getLongOpt();
        assertEquals("?Wf?Wf", longOpt);
    }

    @Test(timeout = 4000)
    public void testDefaultArgName() throws Throwable {
        Option option = new Option(null, null, true, null);
        OptionFormatter optionFormatter = OptionFormatter.builder()
            .setDefaultArgName("[Deprecated")
            .build(option);
        String syntaxOption = optionFormatter.toSyntaxOption();
        assertEquals("[ <[Deprecated>]", syntaxOption);
    }

    @Test(timeout = 4000)
    public void testOptArgSeparator() throws Throwable {
        Option option = new Option(null, null, true, null);
        OptionFormatter optionFormatter = OptionFormatter.builder()
            .setOptArgSeparator("Deprecated")
            .build(option);
        String syntaxOption = optionFormatter.toSyntaxOption();
        assertEquals("[Deprecated<arg>]", syntaxOption);
    }

    @Test(timeout = 4000)
    public void testSyntaxFormatFunctionWithMock() throws Throwable {
        Option option = new Option("NO_ARGS_ALLOWED", "iRi[{-|Um");
        OptionFormatter.Builder builder = new OptionFormatter.Builder(OptionFormatter.from(option));
        BiFunction<OptionFormatter, Boolean, String> mockFunction = mock(BiFunction.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(mockFunction).apply(any(OptionFormatter.class), anyBoolean());
        builder.setSyntaxFormatFunction(mockFunction);
        OptionFormatter optionFormatter = builder.build(option);
        String syntaxOption = optionFormatter.toSyntaxOption(false);
        assertNull(syntaxOption);
    }

    @Test(timeout = 4000)
    public void testEmptySyntaxOption() throws Throwable {
        Option option = new Option(null, null);
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        String syntaxOption = optionFormatter.toSyntaxOption(false);
        assertEquals("", syntaxOption);
    }

    @Test(timeout = 4000)
    public void testNullOptionFormatter() throws Throwable {
        OptionFormatter optionFormatter = OptionFormatter.from(null);
        
        // Test toSyntaxOption with true
        try {
            optionFormatter.toSyntaxOption(true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.cli.help.OptionFormatter", e);
        }

        // Test toSyntaxOption without parameter
        try {
            optionFormatter.toSyntaxOption();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.cli.help.OptionFormatter", e);
        }

        // Test isRequired
        try {
            optionFormatter.isRequired();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.cli.help.OptionFormatter", e);
        }

        // Test getOpt
        try {
            optionFormatter.getOpt();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.cli.help.OptionFormatter", e);
        }

        // Test getLongOpt
        try {
            optionFormatter.getLongOpt();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.cli.help.OptionFormatter", e);
        }

        // Test getDescription
        try {
            optionFormatter.getDescription();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.cli.help.OptionFormatter", e);
        }

        // Test getBothOpt
        try {
            optionFormatter.getBothOpt();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.cli.help.OptionFormatter", e);
        }

        // Test getArgName
        try {
            optionFormatter.getArgName();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.cli.help.OptionFormatter", e);
        }

        // Test getSince
        try {
            optionFormatter.getSince();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.cli.help.OptionFormatter", e);
        }
    }

    @Test(timeout = 4000)
    public void testRequiredOption() throws Throwable {
        Option option = new Option("?Wf", "?Wf", true, "?Wf");
        option.setRequired(true);
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertTrue(optionFormatter.isRequired());
    }

    @Test(timeout = 4000)
    public void testGetSinceWithNullOption() throws Throwable {
        Option option = new Option(null, null, true, null);
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("--", optionFormatter.getSince());
    }

    @Test(timeout = 4000)
    public void testGetDescriptionWithNullOption() throws Throwable {
        Option option = new Option(null, null);
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("", optionFormatter.getDescription());
    }

    @Test(timeout = 4000)
    public void testToOptionalWithEmptyString() throws Throwable {
        Option option = new Option(null, null);
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("", optionFormatter.toOptional(""));
    }

    @Test(timeout = 4000)
    public void testToOptionalWithComma() throws Throwable {
        OptionFormatter optionFormatter = OptionFormatter.from(null);
        assertEquals("[, ]", optionFormatter.toOptional(", "));
    }

    @Test(timeout = 4000)
    public void testGetOptWithNullOption() throws Throwable {
        Option option = new Option(null, null);
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("", optionFormatter.getOpt());
    }

    @Test(timeout = 4000)
    public void testGetOptWithNonNullOption() throws Throwable {
        Option option = new Option("7", "TjJ+)m", false, "");
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("-7", optionFormatter.getOpt());
    }

    @Test(timeout = 4000)
    public void testGetLongOptWithNonNullOption() throws Throwable {
        Option option = new Option("o", "o");
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("", optionFormatter.getLongOpt());
    }

    @Test(timeout = 4000)
    public void testGetArgNameWithNullOption() throws Throwable {
        Option option = new Option(null, null, true, "");
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("<arg>", optionFormatter.getArgName());
    }

    @Test(timeout = 4000)
    public void testGetArgNameWithEmptyOption() throws Throwable {
        Option option = new Option(null, null);
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("", optionFormatter.getArgName());
    }

    @Test(timeout = 4000)
    public void testSyntaxFormatFunctionNotRequired() throws Throwable {
        Option option = new Option("?Wf", true, "?Wf");
        OptionFormatter.Builder builder = OptionFormatter.builder();
        BiFunction<OptionFormatter, Boolean, String> mockFunction = mock(BiFunction.class, new ViolatedAssumptionAnswer());
        builder.setSyntaxFormatFunction(mockFunction);
        OptionFormatter optionFormatter = builder.build(option);
        assertFalse(optionFormatter.isRequired());
    }

    @Test(timeout = 4000)
    public void testIsRequiredWithNullOption() throws Throwable {
        Option option = new Option(null, null, true, "");
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertFalse(optionFormatter.isRequired());
    }

    @Test(timeout = 4000)
    public void testToSyntaxOptionWithRequired() throws Throwable {
        Option option = new Option("?Wf", "?Wf", true, "");
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("-?Wf <arg>", optionFormatter.toSyntaxOption(true));
    }

    @Test(timeout = 4000)
    public void testGetDescriptionWithNonNullOption() throws Throwable {
        Option option = new Option("NO_ARGS_ALLOWED", "iRi[{-|Um");
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("iRi[{-|Um", optionFormatter.getDescription());
    }

    @Test(timeout = 4000)
    public void testGetBothOptWithShortOption() throws Throwable {
        Option option = new Option("?Wf", "?Wf");
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("-?Wf", optionFormatter.getBothOpt());
    }

    @Test(timeout = 4000)
    public void testGetBothOptWithLongOption() throws Throwable {
        Option option = new Option("?Wf", "?Wf", true, "");
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("-?Wf, --?Wf", optionFormatter.getBothOpt());
    }

    @Test(timeout = 4000)
    public void testGetBothOptWithNullOption() throws Throwable {
        Option option = new Option(null, null);
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("", optionFormatter.getBothOpt());
    }

    @Test(timeout = 4000)
    public void testToSyntaxOptionWithNullOption() throws Throwable {
        Option option = new Option(null, null);
        OptionFormatter optionFormatter = OptionFormatter.from(option);
        assertEquals("", optionFormatter.toSyntaxOption());
    }

    @Test(timeout = 4000)
    public void testSetOptionalDelimiters() throws Throwable {
        Option option = new Option(null, null);
        OptionFormatter.Builder builder = new OptionFormatter.Builder(OptionFormatter.from(option));
        assertSame(builder, builder.setOptionalDelimiters("U\"7]OxTl:%M:~HkH", " ]"));
    }

    @Test(timeout = 4000)
    public void testSetArgumentNameDelimiters() throws Throwable {
        Option option = new Option("NO_ARGS_ALLOWED", "iRi[{-|Um");
        OptionFormatter.Builder builder = new OptionFormatter.Builder(OptionFormatter.from(option));
        assertSame(builder, builder.setArgumentNameDelimiters("<EEE MMM dd HH:mm:ss zzz yyyy>", "usage: "));
    }

    @Test(timeout = 4000)
    public void testToArgName() throws Throwable {
        Option option = new Option(null, "n> #GB(6W.[&nb>qttA");
        OptionFormatter.Builder builder = new OptionFormatter.Builder(OptionFormatter.from(option));
        assertEquals("<n6mk~>", builder.toArgName("n6mk~"));
    }

    @Test(timeout = 4000)
    public void testBuilderGetWithNullOption() throws Throwable {
        Option option = new Option(null, null);
        OptionFormatter.Builder builder = new OptionFormatter.Builder(OptionFormatter.from(option));
        assertNull(builder.get());
    }

    @Test(timeout = 4000)
    public void testSetOptSeparator() throws Throwable {
        Option option = new Option(null, null, true, "");
        OptionFormatter.Builder builder = new OptionFormatter.Builder(OptionFormatter.from(option));
        assertSame(builder, builder.setOptSeparator(""));
    }

    @Test(timeout = 4000)
    public void testSetDeprecatedFormatFunction() throws Throwable {
        Option option = new Option(null, null);
        OptionFormatter.Builder builder = new OptionFormatter.Builder(OptionFormatter.from(option));
        assertSame(builder, builder.setDeprecatedFormatFunction(OptionFormatter.COMPLEX_DEPRECATED_FORMAT));
    }

    @Test(timeout = 4000)
    public void testSetOptPrefix() throws Throwable {
        Option option = new Option(null, null);
        OptionFormatter.Builder builder = new OptionFormatter.Builder(OptionFormatter.from(option));
        assertSame(builder, builder.setOptPrefix("-"));
    }
}