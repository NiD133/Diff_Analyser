package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link OptionBuilder} class, focusing on its fluent interface.
 */
public class OptionBuilderTest {

    /**
     * Verifies that calling hasArg(false) returns a non-null builder instance,
     * which is essential for enabling the fluent method-chaining pattern.
     */
    @Test
    public void hasArgWithFalseShouldReturnBuilderInstance() {
        // The OptionBuilder uses a static, fluent API. Each configuration method
        // should return the builder instance to allow for chaining calls.
        OptionBuilder builder = OptionBuilder.hasArg(false);

        // Assert that a valid, non-null instance is returned.
        assertNotNull("The builder instance should not be null after calling hasArg(false).", builder);
    }
}