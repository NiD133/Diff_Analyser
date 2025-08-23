package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import org.junit.After;
import org.junit.Test;

/**
 * Test suite for the {@link OptionBuilder} class.
 * This suite focuses on verifying the behavior of creating options with optional arguments.
 */
public class OptionBuilderTest {

    /**
     * The OptionBuilder class uses static fields to configure and build Option objects.
     * This can lead to state from one test affecting subsequent tests. To ensure each
     * test runs in isolation, this method is executed after each test to reset the
     * OptionBuilder's internal state. It uses reflection to invoke the private
     * static {@code reset()} method within OptionBuilder.
     */
    @After
    public void tearDown() throws Exception {
        Method resetMethod = OptionBuilder.class.getDeclaredMethod("reset");
        resetMethod.setAccessible(true);
        resetMethod.invoke(null);
    }

    @Test
    public void hasOptionalArgsShouldCreateOptionWithUnlimitedOptionalArguments() {
        // Arrange: Configure the builder to create an option that can have an
        // unlimited number of optional arguments.
        OptionBuilder.hasOptionalArgs();

        // Act: Create the option with the short name '7'.
        Option option = OptionBuilder.create('7');

        // Assert: Verify that the created option has the expected properties.
        assertTrue("The option should be marked as having optional arguments.", option.hasOptionalArg());
        
        // The number of arguments should be set to UNLIMITED_VALUES (-2).
        assertEquals("The number of arguments should be unlimited.", Option.UNLIMITED_VALUES, option.getArgs());
        
        // The option's ID is its character code.
        assertEquals("The option's ID should match the character used for creation.", '7', option.getId());
    }
}