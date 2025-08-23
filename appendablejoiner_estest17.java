package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Test suite for {@link AppendableJoiner}.
 *
 * This class contains tests that have been improved for clarity and maintainability
 * from an auto-generated version.
 */
public class AppendableJoinerTest {

    /**
     * Tests that calling join() with a null iterable does not modify the
     * target StringBuilder and returns the original instance.
     */
    @Test
    public void joinWithNullIterableShouldNotModifyTarget() {
        // Arrange
        // A default joiner, which uses an empty delimiter, prefix, and suffix.
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().get();
        final StringBuilder target = new StringBuilder();

        // Act
        // The method under test is called with a null iterable.
        final StringBuilder result = joiner.join(target, (Iterable<Object>) null);

        // Assert
        // The target StringBuilder should remain unchanged (i.e., empty).
        assertEquals("The content of the StringBuilder should be unchanged.", "", target.toString());
        // The join method should return the same instance it was given.
        assertSame("The returned instance should be the same as the target.", target, result);
    }
}