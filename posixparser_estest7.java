package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This test class contains refactored tests for the PosixParser class,
 * focusing on clarity and maintainability.
 */
public class PosixParserTest {

    /**
     * Tests that flatten() throws a ParseException when a long option
     * token is provided in a way that matches all defined long options.
     * <p>
     * The PosixParser interprets an argument like "--=value" as a search for a
     * long option with an empty name (""). This matches every long option defined.
     * If more than one long option exists, the parser cannot resolve the ambiguity
     * and throws an exception.
     */
    @Test(timeout = 4000)
    public void flattenShouldThrowExceptionForAmbiguousLongOptionFromEmptyPrefix() throws Exception {
        // Arrange
        Options options = new Options();
        options.addOption("a", "alpha", false, "The first option");
        options.addOption("b", "beta", false, "The second option");

        // An argument starting with "--=" is treated as a long option with an empty name,
        // which ambiguously matches all long options defined above.
        String[] arguments = {"--=some-value"};
        PosixParser parser = new PosixParser();

        // Act & Assert
        try {
            parser.flatten(options, arguments, false);
            fail("Expected a ParseException to be thrown for an ambiguous option.");
        } catch (ParseException e) {
            // Verify that the exception message correctly identifies the ambiguity.
            // The order of 'alpha' and 'beta' in the message is not guaranteed,
            // so we check for the presence of all expected parts.
            String message = e.getMessage();
            assertTrue("Message should indicate an ambiguous option for '--'.",
                message.startsWith("Ambiguous option: '--'"));
            assertTrue("Message should list 'alpha' as a possibility.", message.contains("'alpha'"));
            assertTrue("Message should list 'beta' as a possibility.", message.contains("'beta'"));
        }
    }
}