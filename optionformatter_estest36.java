package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertNull;

// The test class name is kept from the original for consistency, but in a real-world scenario,
// it would be renamed to something more descriptive, like "OptionFormatterBuilderTest".
public class OptionFormatter_ESTestTest36 extends OptionFormatter_ESTest_scaffolding {

    /**
     * Tests that calling get() on a Builder initialized from an existing OptionFormatter returns null.
     * <p>
     * This behavior is expected because the Builder(OptionFormatter) constructor is designed to copy
     * only the formatting settings (like prefixes and delimiters), not the underlying Option object.
     * Since the get() method (from the Supplier interface) cannot construct a new OptionFormatter
     * without an Option, it returns null.
     */
    @Test
    public void getShouldReturnNullWhenBuilderIsCreatedFromFormatter() {
        // Arrange: Create a base Option and an OptionFormatter from it.
        Option option = new Option(null, "long-opt");
        OptionFormatter sourceFormatter = OptionFormatter.from(option);

        // Create a Builder, initializing it with the settings from the source formatter.
        OptionFormatter.Builder builder = new OptionFormatter.Builder(sourceFormatter);

        // Act: Call the get() method on the builder.
        OptionFormatter result = builder.get();

        // Assert: The result should be null, as the builder lacks the necessary Option
        // to construct a new formatter instance.
        assertNull("Expected null because the builder was not provided with an Option.", result);
    }
}