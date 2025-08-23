package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

/**
 * Test suite for the {@link PatternOptionBuilder} class.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that {@code parsePattern} correctly interprets a complex pattern string.
     * The pattern includes simple flags, a flag requiring a file argument, a mandatory flag,
     * and flags represented by non-alphanumeric characters.
     */
    @Test
    public void parsePatternWithComplexPatternShouldCreateCorrectOptions() {
        // GIVEN a complex pattern string.
        // This pattern defines the following options:
        // - 'y>': an option 'y' that takes a file argument.
        // - '!Q': a mandatory option 'Q'.
        // - '!': a simple flag named '!'. The first '!' is treated as an option name.
        // - '0', 'd', 'p', 'm', 'b', '*', '1', '_': simple flags with no arguments.
        final String pattern = "0dpy>mb!!Q*1_";

        // WHEN the pattern is parsed.
        final Options options = PatternOptionBuilder.parsePattern(pattern);

        // THEN the resulting Options object should be correctly configured.
        assertNotNull("The created Options object should not be null.", options);
        
        // It should contain 11 distinct options based on the pattern.
        assertEquals("The total number of options should be 11.", 11, options.getOptions().size());

        // 1. Verify a simple flag ('d').
        Option simpleOption = options.getOption("d");
        assertNotNull("Option 'd' should exist.", simpleOption);
        assertFalse("Simple flag 'd' should not have an argument.", simpleOption.hasArg());
        assertFalse("Simple flag 'd' should not be required.", simpleOption.isRequired());

        // 2. Verify the flag with a file type argument ('y>').
        Option fileOption = options.getOption("y");
        assertNotNull("Option 'y' should exist.", fileOption);
        assertTrue("Option 'y' should require an argument.", fileOption.hasArg());
        assertEquals("Argument type for option 'y' should be File.class.", File.class, fileOption.getType());

        // 3. Verify the mandatory flag ('!Q').
        Option requiredOption = options.getOption("Q");
        assertNotNull("Option 'Q' should exist.", requiredOption);
        assertTrue("Option 'Q' should be mandatory.", requiredOption.isRequired());
        assertFalse("Option 'Q' should not have an argument.", requiredOption.hasArg());

        // 4. Verify the flag using a non-alphanumeric character ('!').
        Option nonAlphanumericOption = options.getOption("!");
        assertNotNull("Option '!' should exist.", nonAlphanumericOption);
        assertFalse("Option '!' should not have an argument.", nonAlphanumericOption.hasArg());
        assertFalse("Option '!' should not be required.", nonAlphanumericOption.isRequired());
    }
}