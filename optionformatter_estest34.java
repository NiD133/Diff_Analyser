package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Tests for the fluent interface of {@link OptionFormatter.Builder}.
 */
public class OptionFormatterBuilderFluencyTest {

    @Test
    public void setArgumentNameDelimitersShouldReturnSameBuilderInstanceForChaining() {
        // Arrange: Create a builder instance to test.
        // The specific option used for initialization is not relevant to this test.
        Option option = new Option("f", "file");
        OptionFormatter initialFormatter = OptionFormatter.from(option);
        OptionFormatter.Builder builder = new OptionFormatter.Builder(initialFormatter);

        // Act: Call the method under test.
        OptionFormatter.Builder resultBuilder = builder.setArgumentNameDelimiters("<", ">");

        // Assert: Verify that the method returns the same instance.
        // This confirms the method supports a fluent API (method chaining).
        assertSame("The method should return the same builder instance to enable fluent chaining.",
                builder, resultBuilder);
    }
}