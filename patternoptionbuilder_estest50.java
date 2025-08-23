package org.apache.commons.cli;

import org.junit.Test;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link PatternOptionBuilder} class.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that {@code parsePattern} can handle a complex string containing various
     * character types and correctly builds an {@code Options} object.
     *
     * <p>This test verifies that the parser is robust and correctly interprets
     * the valid parts of the pattern while ignoring invalid characters, without
     * throwing an exception. The pattern used includes:
     * <ul>
     *   <li>Simple flags (e.g., 'W', 'D')</li>
     *   <li>A flag with an argument type specifier (e.g., 'F+')</li>
     *   <li>A duplicated flag ('f'), where the last occurrence should be used</li>
     *   <li>Invalid characters that should be ignored (e.g., a digit '2')</li>
     *   <li>A type specifier without a preceding valid option character (e.g., '2@')</li>
     * </ul>
     */
    @Test
    public void parsePatternShouldCorrectlyHandleComplexPatternWithVariousCharacterTypes() {
        // Arrange: A complex pattern string designed to test multiple parsing rules.
        // Based on the Javadoc, this should result in options:
        // W, D, S, q, U, K, b, H, F+ and the second 'f'.
        // The first 'f' is overwritten. '2' and '@' (after '2') are ignored.
        final String complexPattern = "fWDS2@qUKbfHF+";

        // Act: Parse the pattern to create an Options object.
        Options options = PatternOptionBuilder.parsePattern(complexPattern);

        // Assert: Verify that the resulting Options object is correctly configured.

        // 1. The method should always return a valid, non-null Options object.
        assertNotNull("The returned Options object should not be null.", options);

        // 2. Check the total number of options created.
        Collection<Option> createdOptions = options.getOptions();
        assertEquals("Should create 10 options from the pattern.", 10, createdOptions.size());

        // 3. Verify that all simple flags exist.
        assertTrue("Option 'W' should exist.", options.hasOption("W"));
        assertTrue("Option 'D' should exist.", options.hasOption("D"));
        assertTrue("Option 'S' should exist.", options.hasOption("S"));
        assertTrue("Option 'q' should exist.", options.hasOption("q"));
        assertTrue("Option 'U' should exist.", options.hasOption("U"));
        assertTrue("Option 'K' should exist.", options.hasOption("K"));
        assertTrue("Option 'b' should exist.", options.hasOption("b"));
        assertTrue("Option 'H' should exist.", options.hasOption("H"));
        assertTrue("Option 'f' should exist (from its second occurrence).", options.hasOption("f"));

        // 4. Verify the properties of the flag with an argument ('F+').
        assertTrue("Option 'F' should exist.", options.hasOption("F"));
        Option optionF = options.getOption("F");
        assertNotNull("Option 'F' object should not be null.", optionF);
        assertTrue("Option 'F' should be configured to have an argument.", optionF.hasArg());
        assertEquals("Option 'F' should expect a Class object as its type.", Class.class, optionF.getType());

        // 5. Verify that invalid characters from the pattern did not create options.
        assertFalse("The digit '2' should not be treated as an option.", options.hasOption("2"));
    }
}