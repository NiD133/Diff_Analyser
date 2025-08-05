package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.function.BiFunction;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.help.OptionFormatter;

/**
 * Test suite for OptionFormatter class functionality.
 * Tests various formatting scenarios for command-line options.
 */
public class OptionFormatterTest {

    // ========== Builder Configuration Tests ==========
    
    @Test
    public void testCustomLongOptPrefix() {
        Option option = new Option("f", "file", true, "Input file");
        
        OptionFormatter formatter = OptionFormatter.builder()
            .setLongOptPrefix("?Wf")
            .build(option);
        
        assertEquals("?Wffile", formatter.getLongOpt());
    }
    
    @Test
    public void testCustomDefaultArgName() {
        Option optionWithArgs = new Option(null, null, true, null);
        
        OptionFormatter formatter = OptionFormatter.builder()
            .setDefaultArgName("[Deprecated")
            .build(optionWithArgs);
        
        assertEquals("[ <[Deprecated>]", formatter.toSyntaxOption());
    }
    
    @Test
    public void testCustomOptArgSeparator() {
        Option optionWithArgs = new Option(null, null, true, null);
        
        OptionFormatter formatter = OptionFormatter.builder()
            .setOptArgSeparator("Deprecated")
            .build(optionWithArgs);
        
        assertEquals("[Deprecated<arg>]", formatter.toSyntaxOption());
    }
    
    @Test
    public void testCustomSyntaxFormatFunction() {
        Option option = new Option("v", "verbose");
        BiFunction<OptionFormatter, Boolean, String> customFunction = 
            (formatter, required) -> null;
        
        OptionFormatter formatter = OptionFormatter.builder()
            .setSyntaxFormatFunction(customFunction)
            .build(option);
        
        assertNull(formatter.toSyntaxOption(false));
    }

    // ========== Option Property Tests ==========
    
    @Test
    public void testRequiredOptionFlag() {
        Option requiredOption = new Option("f", "file", true, "Input file");
        requiredOption.setRequired(true);
        
        OptionFormatter formatter = OptionFormatter.from(requiredOption);
        
        assertTrue(formatter.isRequired());
    }
    
    @Test
    public void testOptionalOptionFlag() {
        Option optionalOption = new Option(null, null, true, null);
        
        OptionFormatter formatter = OptionFormatter.from(optionalOption);
        
        assertFalse(formatter.isRequired());
    }
    
    @Test
    public void testGetDescription() {
        Option optionWithDescription = new Option("v", "verbose", false, "Enable verbose output");
        
        OptionFormatter formatter = OptionFormatter.from(optionWithDescription);
        
        assertEquals("Enable verbose output", formatter.getDescription());
    }
    
    @Test
    public void testGetDescriptionEmpty() {
        Option optionWithoutDescription = new Option(null, null);
        
        OptionFormatter formatter = OptionFormatter.from(optionWithoutDescription);
        
        assertEquals("", formatter.getDescription());
    }

    // ========== Option Name Formatting Tests ==========
    
    @Test
    public void testGetShortOptWithPrefix() {
        Option shortOption = new Option("v", "verbose", false, "");
        
        OptionFormatter formatter = OptionFormatter.from(shortOption);
        
        assertEquals("-v", formatter.getOpt());
    }
    
    @Test
    public void testGetShortOptEmpty() {
        Option optionWithoutShort = new Option(null, null);
        
        OptionFormatter formatter = OptionFormatter.from(optionWithoutShort);
        
        assertEquals("", formatter.getOpt());
    }
    
    @Test
    public void testGetLongOptEmpty() {
        Option optionWithoutLong = new Option("o", "o");
        
        OptionFormatter formatter = OptionFormatter.from(optionWithoutLong);
        
        assertEquals("", formatter.getLongOpt());
    }
    
    @Test
    public void testGetBothOptShortOnly() {
        Option shortOnlyOption = new Option("v", "verbose");
        
        OptionFormatter formatter = OptionFormatter.from(shortOnlyOption);
        
        assertEquals("-v", formatter.getBothOpt());
    }
    
    @Test
    public void testGetBothOptShortAndLong() {
        Option bothOption = new Option("f", "file", true, "");
        
        OptionFormatter formatter = OptionFormatter.from(bothOption);
        
        assertEquals("-f, --file", formatter.getBothOpt());
    }
    
    @Test
    public void testGetBothOptEmpty() {
        Option emptyOption = new Option(null, null);
        
        OptionFormatter formatter = OptionFormatter.from(emptyOption);
        
        assertEquals("", formatter.getBothOpt());
    }

    // ========== Argument Name Tests ==========
    
    @Test
    public void testGetArgNameWithArgs() {
        Option optionWithArgs = new Option(null, null, true, "");
        
        OptionFormatter formatter = OptionFormatter.from(optionWithArgs);
        
        assertEquals("<arg>", formatter.getArgName());
    }
    
    @Test
    public void testGetArgNameWithoutArgs() {
        Option optionWithoutArgs = new Option(null, null);
        
        OptionFormatter formatter = OptionFormatter.from(optionWithoutArgs);
        
        assertEquals("", formatter.getArgName());
    }

    // ========== Syntax Option Tests ==========
    
    @Test
    public void testToSyntaxOptionRequired() {
        Option requiredOption = new Option("f", "file", true, "");
        
        OptionFormatter formatter = OptionFormatter.from(requiredOption);
        
        assertEquals("-f <arg>", formatter.toSyntaxOption(true));
    }
    
    @Test
    public void testToSyntaxOptionOptional() {
        Option optionalOption = new Option(null, null, false, null);
        
        OptionFormatter formatter = OptionFormatter.from(optionalOption);
        
        assertEquals("", formatter.toSyntaxOption(false));
    }
    
    @Test
    public void testToSyntaxOptionEmpty() {
        Option emptyOption = new Option(null, null);
        
        OptionFormatter formatter = OptionFormatter.from(emptyOption);
        
        assertEquals("", formatter.toSyntaxOption());
    }

    // ========== Optional Wrapper Tests ==========
    
    @Test
    public void testToOptionalWithEmptyString() {
        Option option = new Option(null, null);
        OptionFormatter formatter = OptionFormatter.from(option);
        
        assertEquals("", formatter.toOptional(""));
    }
    
    @Test
    public void testToOptionalWithText() {
        OptionFormatter formatter = OptionFormatter.from(null);
        
        assertEquals("[, ]", formatter.toOptional(", "));
    }

    // ========== Builder Helper Tests ==========
    
    @Test
    public void testBuilderToArgName() {
        Option option = new Option(null, "longOpt");
        OptionFormatter baseFormatter = OptionFormatter.from(option);
        OptionFormatter.Builder builder = new OptionFormatter.Builder(baseFormatter);
        
        assertEquals("<testArg>", builder.toArgName("testArg"));
    }
    
    @Test
    public void testBuilderGetWithoutOption() {
        Option option = new Option(null, null);
        OptionFormatter baseFormatter = OptionFormatter.from(option);
        OptionFormatter.Builder builder = new OptionFormatter.Builder(baseFormatter);
        
        assertNull(builder.get());
    }

    // ========== Builder Fluent Interface Tests ==========
    
    @Test
    public void testBuilderFluentInterface() {
        Option option = new Option(null, null);
        OptionFormatter baseFormatter = OptionFormatter.from(option);
        OptionFormatter.Builder builder = new OptionFormatter.Builder(baseFormatter);
        
        // Test that builder methods return the same instance for fluent chaining
        assertSame(builder, builder.setOptionalDelimiters("[", "]"));
        assertSame(builder, builder.setArgumentNameDelimiters("<", ">"));
        assertSame(builder, builder.setOptSeparator(""));
        assertSame(builder, builder.setOptPrefix("-"));
        assertSame(builder, builder.setDeprecatedFormatFunction(OptionFormatter.COMPLEX_DEPRECATED_FORMAT));
    }

    // ========== Null Option Handling Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void testNullOptionToSyntaxOptionRequired() {
        OptionFormatter formatter = OptionFormatter.from(null);
        formatter.toSyntaxOption(true);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullOptionToSyntaxOption() {
        OptionFormatter formatter = OptionFormatter.from(null);
        formatter.toSyntaxOption();
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullOptionIsRequired() {
        OptionFormatter formatter = OptionFormatter.from(null);
        formatter.isRequired();
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullOptionGetOpt() {
        OptionFormatter formatter = OptionFormatter.from(null);
        formatter.getOpt();
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullOptionGetLongOpt() {
        OptionFormatter formatter = OptionFormatter.from(null);
        formatter.getLongOpt();
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullOptionGetDescription() {
        OptionFormatter formatter = OptionFormatter.from(null);
        formatter.getDescription();
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullOptionGetBothOpt() {
        OptionFormatter formatter = OptionFormatter.from(null);
        formatter.getBothOpt();
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullOptionGetArgName() {
        OptionFormatter formatter = OptionFormatter.from(null);
        formatter.getArgName();
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullOptionGetSince() {
        OptionFormatter formatter = OptionFormatter.from(null);
        formatter.getSince();
    }

    // ========== Since Attribute Test ==========
    
    @Test
    public void testGetSince() {
        Option optionWithArgs = new Option(null, null, true, null);
        OptionFormatter formatter = OptionFormatter.from(optionWithArgs);
        
        assertEquals("--", formatter.getSince());
    }
}