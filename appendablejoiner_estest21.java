package org.apache.commons.lang3;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Unit tests for {@link AppendableJoiner}.
 */
public class AppendableJoinerTest {

    /**
     * Tests that the join() method throws a NullPointerException when the target
     * StringBuilder is null.
     */
    @Test(expected = NullPointerException.class)
    public void testJoinWithNullStringBuilderThrowsNullPointerException() {
        // Arrange
        // Create a default joiner instance.
        final AppendableJoiner<String> joiner = AppendableJoiner.builder().get();
        
        // The list of elements to join can be empty for this test's purpose.
        final List<String> elements = Collections.emptyList();
        
        // Act & Assert
        // The join method should throw a NullPointerException when the target StringBuilder is null.
        joiner.join((StringBuilder) null, elements);
    }
}