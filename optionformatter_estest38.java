package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This test class contains tests for the {@link OptionFormatter.Builder}.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class OptionFormatter_ESTestTest38 extends OptionFormatter_ESTest_scaffolding {

    /**
     * Tests that the {@code setDeprecatedFormatFunction} method on the {@code OptionFormatter.Builder}
     * returns the same instance to allow for fluent method chaining.
     */
    @Test
    public void setDeprecatedFormatFunctionShouldReturnSameBuilderInstanceForChaining() {
        // Arrange: Create a new builder instance.
        final OptionFormatter.Builder builder = OptionFormatter.builder();

        // Act: Call the setter method.
        final OptionFormatter.Builder result = builder.setDeprecatedFormatFunction(OptionFormatter.COMPLEX_DEPRECATED_FORMAT);

        // Assert: The method should return the same builder instance.
        assertSame("The setter method should return 'this' to support a fluent API.", builder, result);
    }
}