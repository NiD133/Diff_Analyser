package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link OptionBuilder}.
 */
public class OptionBuilderTest {

    @Test
    public void withDescriptionShouldReturnBuilderInstanceForChaining() {
        // Arrange
        String description = "A sample option description";

        // Act
        // Call the method under test, which should return the builder instance.
        OptionBuilder builder = OptionBuilder.withDescription(description);

        // Assert
        // Verify that a non-null builder instance is returned to ensure
        // the fluent API contract is met (i.e., method calls can be chained).
        assertNotNull("Expected withDescription() to return the builder instance", builder);
    }
}