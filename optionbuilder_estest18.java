package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the deprecated {@link OptionBuilder} class.
 */
public class OptionBuilderTest {

    /**
     * This test verifies that calling OptionBuilder.create() without first specifying
     * an option name (e.g., via withLongOpt()) results in an IllegalArgumentException.
     * The OptionBuilder requires at least a long option name to be set before an
     * Option can be created.
     */
    @Test
    public void createShouldThrowExceptionWhenLongOptionIsMissing() {
        // Expectation: An IllegalArgumentException should be thrown.
        try {
            // Action: Call create() on the builder in its initial, empty state.
            OptionBuilder.create();
            
            // Verification: If this line is reached, the test fails because no exception was thrown.
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (final IllegalArgumentException e) {
            // Verification: Check if the exception message is correct.
            assertEquals("must specify longopt", e.getMessage());
        }
    }
}