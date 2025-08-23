package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link OptionBuilder} class.
 * Note: The class name and inheritance are kept from the original for context.
 * In a real-world scenario, they would likely be simplified to "OptionBuilderTest".
 */
public class OptionBuilder_ESTestTest2 extends OptionBuilder_ESTest_scaffolding {

    /**
     * Tests that an Option created via OptionBuilder has an uninitialized
     * number of arguments by default. When no argument-related methods like
     * hasArg() or hasArgs() are called, the resulting Option should have its
     * argument count set to Option.UNINITIALIZED.
     */
    @Test
    public void create_withoutSpecifyingArgs_shouldReturnOptionWithUninitializedArgCount() {
        // Arrange: Define the name for the option to be created.
        // The OptionBuilder uses static state, which is reset before each test
        // by the test scaffolding. We are intentionally not calling any methods
        // that configure arguments to test the default behavior.
        final String optionName = "converterMap";

        // Act: Create the option using the builder.
        final Option createdOption = OptionBuilder.create(optionName);

        // Assert: Verify that the number of arguments is the default uninitialized value.
        assertEquals("Default number of arguments should be UNINITIALIZED",
                Option.UNINITIALIZED, createdOption.getArgs());
    }
}