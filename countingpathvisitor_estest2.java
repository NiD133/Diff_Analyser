package org.apache.commons.io.file;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Tests for the fluent API of {@link CountingPathVisitor.Builder}.
 */
public class CountingPathVisitorBuilderTest {

    /**
     * Verifies that the {@code setPathCounters} method returns the same builder instance
     * to allow for method chaining (a fluent interface).
     */
    @Test
    public void setPathCountersReturnsSameBuilderInstance() {
        // Arrange: Create a new builder and get its default counters.
        final CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        final Counters.PathCounters defaultCounters = builder.getPathCounters();

        // Act: Call the setter method.
        final CountingPathVisitor.Builder returnedBuilder = builder.setPathCounters(defaultCounters);

        // Assert: The returned instance should be the same as the original builder.
        assertSame("Setter should return 'this' to support a fluent API.", builder, returnedBuilder);
    }
}