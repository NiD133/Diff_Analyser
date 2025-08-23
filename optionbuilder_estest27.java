package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link OptionBuilder} class, focusing on creating required options.
 */
public class OptionBuilderTest {

    /**
     * The OptionBuilder class uses static fields, which can retain state between tests.
     * This method resets the builder before each test to ensure test isolation.
     * Note: A better design would avoid static state, but we test the class as is.
     */
    @Before
    public void setUp() {
        // OptionBuilder is a static utility, so we need to reset it before each test
        // to prevent state from leaking between test cases.
        // This is done by creating a new Option, which has the side effect of resetting the builder.
        OptionBuilder.create("reset");
    }

    /**
     * Verifies that calling isRequired() correctly marks a created Option as mandatory.
     */
    @Test
    public void shouldCreateRequiredOptionWhenIsRequiredIsCalled() {
        // Arrange: Configure the builder to create a required option.
        // A long option is specified because create() without a short-opt requires it.
        OptionBuilder.isRequired();
        OptionBuilder.withLongOpt("required-option");

        // Act: Create the option based on the builder's configuration.
        Option option = OptionBuilder.create();

        // Assert: Check that the resulting option is marked as required and has the
        // default (uninitialized) number of arguments.
        assertTrue("The option should be marked as required", option.isRequired());
        assertEquals("The number of arguments should be uninitialized", Option.UNINITIALIZED, option.getArgs());
    }
}