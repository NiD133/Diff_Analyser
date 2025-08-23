package org.apache.commons.cli.help;

import org.apache.commons.cli.help.OptionFormatter.Builder;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Tests for the fluent API of {@link OptionFormatter.Builder}.
 */
public class OptionFormatterBuilderTest {

    /**
     * Verifies that the setOptSeparator() method returns the same builder instance,
     * which is essential for enabling a fluent method-chaining style.
     */
    @Test
    public void setOptSeparatorShouldReturnSameInstanceForFluentChaining() {
        // Arrange: Create a new OptionFormatter builder instance.
        final Builder builder = OptionFormatter.builder();

        // Act: Call the method under test.
        final Builder result = builder.setOptSeparator("");

        // Assert: The returned instance should be the same as the original.
        assertSame("The builder method should return 'this' to allow for fluent chaining.", builder, result);
    }
}