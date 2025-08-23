package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the deprecated {@link OptionBuilder} class.
 */
public class OptionBuilderTest {

    /**
     * Verifies that the withType() method returns an OptionBuilder instance,
     * which is essential for enabling the fluent, chained-method API style.
     */
    @Test
    public void withTypeShouldReturnBuilderInstanceForChaining() {
        // Arrange: The OptionBuilder uses a static, fluent-style API.
        // We are simply verifying that a call to one of its methods returns
        // the builder instance as expected.

        // Act: Call the method under test.
        OptionBuilder builder = OptionBuilder.withType(Object.class);

        // Assert: The returned instance must not be null to allow for
        // subsequent method calls in a chain (e.g., .withLongOpt(...).create()).
        assertNotNull("The builder instance should not be null.", builder);
    }
}