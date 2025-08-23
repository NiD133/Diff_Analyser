package org.apache.commons.io.file;

import org.junit.Test;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for the default factory methods in {@link CountingPathVisitor}.
 */
public class CountingPathVisitorDefaultsTest {

    /**
     * Tests that the {@link CountingPathVisitor#defaultDirectoryTransformer()} method
     * returns a non-null operator, ensuring a safe default is always provided.
     */
    @Test
    public void defaultDirectoryTransformer_shouldReturnNonNullOperator() {
        // Act: Call the static factory method to get the default transformer.
        final UnaryOperator<Path> transformer = CountingPathVisitor.defaultDirectoryTransformer();

        // Assert: Verify that the returned transformer is not null.
        assertNotNull("The default directory transformer should never be null.", transformer);
    }
}