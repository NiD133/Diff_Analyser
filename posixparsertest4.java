package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link PosixParser} that override and disable unsupported
 * behaviors inherited from the {@link AbstractParserTestCase} base class.
 */
public class PosixParserUnsupportedFeaturesTest extends AbstractParserTestCase {

    @Override
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setUp() {
        super.setUp();
        parser = new PosixParser();
    }

    /**
     * Overrides the base test for stopping option processing with a double dash ('--').
     *
     * <p>The standard behavior, tested in the base class, is that '--' signals the
     * end of options. Any subsequent tokens, even if they start with a hyphen, should
     * be treated as plain arguments.
     *
     * <p>This test is disabled because {@link PosixParser} does not support this
     * feature. It continues to parse options after encountering '--'. The test body
     * is provided to clearly document the exact behavior that is unsupported.
     *
     * @throws Exception if parsing fails (though this test is disabled).
     */
    @Override
    @Test
    @Disabled("Not supported: PosixParser does not stop option processing after '--'")
    void testDoubleDash2() throws Exception {
        // Arrange: Define an option and arguments where '--' should stop parsing.
        final Options options = new Options();
        options.addOption("v", "verbose", false, "Enable verbose mode");
        final String[] arguments = {"--", "--verbose"};

        // Act: Parse the arguments.
        final CommandLine cl = parser.parse(options, arguments);

        // Assert: The base test expects '--verbose' to be a non-option argument.
        assertFalse(cl.hasOption("verbose"), "Option 'verbose' should not be recognized after '--'");
        assertEquals(1, cl.getArgList().size(), "There should be one non-option argument");
        assertEquals("--verbose", cl.getArgList().get(0), "The non-option argument should be '--verbose'");
    }
}