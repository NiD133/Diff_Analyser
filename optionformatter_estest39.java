package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Tests for the fluent interface of {@link OptionFormatter.Builder}.
 */
public class OptionFormatterBuilderTest {

    /**
     * Verifies that the setter methods in the Builder return the same instance,
     * allowing for fluent call chaining.
     */
    @Test
    public void setOptPrefixShouldReturnSameBuilderInstanceForChaining() {
        // Arrange: A builder instance is required to test its methods.
        // Creating an Option and Formatter is the standard way to get a Builder.
        Option dummyOption = new Option("o", "option");
        OptionFormatter formatter = OptionFormatter.from(dummyOption);
        OptionFormatter.Builder builder = new OptionFormatter.Builder(formatter);

        // Act: Call the method under test.
        OptionFormatter.Builder returnedBuilder = builder.setOptPrefix("-");

        // Assert: The method should return the same instance for fluent chaining.
        assertSame("The builder instance should be returned to allow for a fluent API.", builder, returnedBuilder);
    }
}