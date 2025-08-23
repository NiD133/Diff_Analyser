package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Collections;

/**
 * Tests for {@link AppendableJoiner}.
 */
public class AppendableJoinerTest {

    /**
     * Tests that joining an empty iterable does not append the prefix, suffix, or any delimiters,
     * leaving the Appendable unchanged.
     */
    @Test
    public void testJoinWithEmptyIterableDoesNotAppendPrefixOrSuffix() {
        // Arrange
        // Create a joiner with a defined prefix and suffix to ensure they are correctly ignored.
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setPrefix("[")
                .setSuffix("]")
                .setDelimiter(",")
                .get();

        final StringBuilder target = new StringBuilder();
        final Iterable<Object> emptyCollection = Collections.emptyList();

        // Act
        final StringBuilder result = joiner.join(target, emptyCollection);

        // Assert
        // The join operation on an empty collection should not append anything.
        assertEquals("The StringBuilder should remain empty.", "", target.toString());
        // The method should return the same StringBuilder instance.
        assertSame("The returned StringBuilder should be the same instance as the input.", target, result);
    }
}