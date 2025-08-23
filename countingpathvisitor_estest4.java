package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for the fluent API of {@link CountingPathVisitor.Builder}.
 */
public class CountingPathVisitorBuilderTest {

    /**
     * Verifies that the {@code setFileFilter} method returns the same builder instance,
     * confirming its fluent interface design.
     */
    @Test
    public void setFileFilterShouldReturnSameBuilderInstanceForChaining() {
        // Arrange: Create a new builder instance.
        final CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        final PathFilter defaultFilter = builder.getFileFilter();

        // Act: Call the method under test.
        final CountingPathVisitor.Builder resultBuilder = builder.setFileFilter(defaultFilter);

        // Assert: The method should return the same instance to allow for method chaining.
        assertSame("The builder instance returned by setFileFilter should be the same as the original.", builder, resultBuilder);
    }
}