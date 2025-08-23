package org.apache.commons.io.file;

import org.apache.commons.io.file.CountingPathVisitor.Builder;
import org.junit.Test;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

import static org.junit.Assert.assertSame;

/**
 * Tests for the fluent interface of {@link CountingPathVisitor.Builder}.
 */
public class CountingPathVisitorBuilderTest {

    /**
     * Tests that setDirectoryPostTransformer() returns the same builder instance,
     * confirming the method supports a fluent (chainable) API.
     */
    @Test
    public void testSetDirectoryPostTransformerIsFluent() {
        // Arrange: Create a new builder instance.
        final Builder builder = new Builder();

        // Act: Call the setter method. The cast is necessary because 'null' has no type.
        final Builder returnedBuilder = builder.setDirectoryPostTransformer((UnaryOperator<Path>) null);

        // Assert: The method should return the same instance to allow for method chaining.
        assertSame("The setter should return the same builder instance.", builder, returnedBuilder);
    }
}