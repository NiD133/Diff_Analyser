package org.apache.commons.io.file;

import org.junit.Test;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

import static org.junit.Assert.assertSame;

/**
 * Tests for the fluent interface of {@link CountingPathVisitor.Builder}.
 */
public class CountingPathVisitorBuilderTest {

    /**
     * Tests that the {@code setDirectoryPostTransformer} method returns the same
     * builder instance to allow for fluent method chaining.
     */
    @Test
    public void setDirectoryPostTransformerShouldReturnSameBuilderInstance() {
        // Arrange
        final CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        final UnaryOperator<Path> identityTransformer = UnaryOperator.identity();

        // Act
        final CountingPathVisitor.Builder resultBuilder = builder.setDirectoryPostTransformer(identityTransformer);

        // Assert
        assertSame("The method should return the same builder instance for fluent chaining.",
                builder, resultBuilder);
    }
}