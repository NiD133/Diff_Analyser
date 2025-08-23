package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the deprecated {@link OptionBuilder} class.
 */
public class OptionBuilderTest {

    /**
     * Tests that the deprecated {@code withType(Object)} method returns a non-null
     * OptionBuilder instance, ensuring backward compatibility is maintained.
     */
    @Test
    public void withType_usingDeprecatedObjectArgument_shouldReturnBuilderInstance() {
        // Arrange
        // The cast to Object is necessary to specifically invoke the deprecated
        // method signature `withType(Object)` instead of `withType(Class<?>)`.
        Object optionType = String.class;

        // Act
        OptionBuilder builder = OptionBuilder.withType(optionType);

        // Assert
        assertNotNull("The builder instance should not be null", builder);
    }
}